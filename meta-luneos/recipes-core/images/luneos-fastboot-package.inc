DESCRIPTION = "Fastboot-flashable LuneOS package: boot image, userdata image and a flash script"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "zip-native coreutils-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_BASENAME = "luneos"
IMAGE_NAME = "${IMAGE_BASENAME}-image"

ZIP_BASENAME = "${IMAGE_BASENAME}-fastboot-package-${MACHINE}"
ZIP_NAME = "${ZIP_BASENAME}${IMAGE_VERSION_SUFFIX}"

inherit kernel-artifact-names deploy nopackages

# A device with no custom recovery cannot take the update zip, so the whole
# install is a set of partition images written from fastboot. The userdata
# image is the payload: an ext4 holding the rootfs as rootfs.img, which is
# where the halium initramfs looks for it (see the userdataimg image type).
#
# vbmeta: the machine's own vbmeta-disabled.img when it deploys one, otherwise
# one generated here - an AvbVBMetaImageHeader with no key, no signature and no
# descriptors and flags 3 (HASHTREE_DISABLED | VERIFICATION_DISABLED), which is
# what "avbtool make_vbmeta_image --flags 3" writes. Droidian and FuriOS ship
# the same image. flash.sh writes either one with --disable-verity
# --disable-verification, as UBports and Sailfish OS do, so a machine's own
# vbmeta ends at flags 3 too. (luneos-odin-package keeps flags 2: Odin has no
# such options.) Only meaningful with the bootloader unlocked. A machine with
# no vbmeta partition sets FASTBOOT_VBMETA_PARTITION = "".
FASTBOOT_VBMETA_IMAGE ?= "vbmeta-disabled.img"
FASTBOOT_VBMETA_PARTITION ?= "vbmeta"

# The boot image, and the dependency that produces it. Kernel recipes deploy
# <KERNEL_IMAGETYPE>.fastboot; a GKI machine's come from luneos-bootimg-gki
# (meta-android's meta-android-halium.inc sets these for it). The debug image,
# when there is one, is shipped too and flashed with "flash.sh --debug".
FASTBOOT_BOOT_IMAGE ?= "${KERNEL_IMAGETYPE}.fastboot"
FASTBOOT_DEBUG_BOOT_IMAGE ?= ""
FASTBOOT_BOOT_DEPENDS ?= "virtual/kernel:do_deploy"

# The init_boot image, on a device that launched with Android 13 or later. There
# boot holds only the kernel and our initramfs is in init_boot, so a package
# that wrote boot alone would start our kernel on the stock generic ramdisk.
# luneos-bootimg-gki deploys it from the same task as the boot image, so
# FASTBOOT_BOOT_DEPENDS covers it (meta-android-halium.inc sets it for
# ANDROID_BOOTIMG_INIT_BOOT = "1"). It goes to init_<partition> next to every
# boot partition, in both modes: the debug image differs only in its cmdline,
# which lives in the boot header, not in init_boot.
FASTBOOT_INIT_BOOT_IMAGE ?= ""

# Where the boot image goes. Both slots by default, so it boots whichever slot
# the bootloader picks; a machine that keeps Android on the other slot says
# "boot", which fastboot writes to the current slot.
FASTBOOT_BOOT_PARTITIONS ?= "boot_a boot_b"

do_deploy() {
    USERDATA="${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}${IMAGE_NAME_SUFFIX}.userdataimg"
    FASTBOOT="${DEPLOY_DIR_IMAGE}/${FASTBOOT_BOOT_IMAGE}"

    if [ ! -e $USERDATA ] ; then
        bbfatal "No userdata image at $USERDATA. Add userdataimg to IMAGE_FSTYPES for this machine."
    fi
    if [ ! -e $FASTBOOT ] ; then
        bbfatal "Required boot image $FASTBOOT doesn't exist."
    fi

    rm -rf ${WORKDIR}/build
    mkdir -p ${WORKDIR}/build

    cp $FASTBOOT ${WORKDIR}/build/boot.img
    cp $USERDATA ${WORKDIR}/build/userdata.img

    if [ -n "${FASTBOOT_DEBUG_BOOT_IMAGE}" ] ; then
        if [ ! -e "${DEPLOY_DIR_IMAGE}/${FASTBOOT_DEBUG_BOOT_IMAGE}" ] ; then
            bbfatal "Debug boot image ${DEPLOY_DIR_IMAGE}/${FASTBOOT_DEBUG_BOOT_IMAGE} doesn't exist."
        fi
        cp "${DEPLOY_DIR_IMAGE}/${FASTBOOT_DEBUG_BOOT_IMAGE}" ${WORKDIR}/build/boot-debug.img
    fi

    if [ -n "${FASTBOOT_INIT_BOOT_IMAGE}" ] ; then
        if [ ! -e "${DEPLOY_DIR_IMAGE}/${FASTBOOT_INIT_BOOT_IMAGE}" ] ; then
            bbfatal "init_boot image ${DEPLOY_DIR_IMAGE}/${FASTBOOT_INIT_BOOT_IMAGE} doesn't exist."
        fi
        cp "${DEPLOY_DIR_IMAGE}/${FASTBOOT_INIT_BOOT_IMAGE}" ${WORKDIR}/build/init_boot.img
    fi

    vbmeta_line=""
    if [ -n "${FASTBOOT_VBMETA_PARTITION}" ] ; then
        if [ -e "${DEPLOY_DIR_IMAGE}/${FASTBOOT_VBMETA_IMAGE}" ] ; then
            cp "${DEPLOY_DIR_IMAGE}/${FASTBOOT_VBMETA_IMAGE}" ${WORKDIR}/build/vbmeta.img
        else
            python3 - ${WORKDIR}/build/vbmeta.img <<'PYEOF'
import struct, sys
fmt = '>4sIIQQI10QQII48s80x'
hdr = struct.pack(fmt, b'AVB0', 1, 0, 0, 0, 0,
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  0, 3, 0, b'avbtool 1.2.0')
assert len(hdr) == 256
open(sys.argv[1], 'wb').write(hdr.ljust(4096, b'\0'))
PYEOF
        fi
        vbmeta_line="fastboot --disable-verity --disable-verification flash ${FASTBOOT_VBMETA_PARTITION} vbmeta.img"
    fi

    boot_lines=""
    flashed="vbmeta, boot"
    [ -n "${FASTBOOT_INIT_BOOT_IMAGE}" ] && flashed="$flashed, init_boot"
    for part in ${FASTBOOT_BOOT_PARTITIONS} ; do
        boot_lines="$boot_lines
fastboot flash $part \$BOOT"
        if [ -n "${FASTBOOT_INIT_BOOT_IMAGE}" ] ; then
            case $part in
            *_a|*_b)
                boot_lines="$boot_lines
fastboot flash init_$part init_boot.img"
                ;;
            *)
                # A bare "boot" is the current slot. fastboot adds the suffix
                # only for partitions the bootloader reports as slotted, and the
                # BraX3's lk does not report init_boot at all: a bare
                # "flash init_boot" fails with "partition does not exist", while
                # init_boot_b by name is written. So name the slot.
                boot_lines="$boot_lines
SLOT=\$(fastboot getvar current-slot 2>&1 | sed -n 's/^current-slot: *//p')
[ -n \"\$SLOT\" ] || { echo \"cannot read the current slot\"; exit 1; }
fastboot flash init_${part}_\$SLOT init_boot.img"
                ;;
            esac
        fi
    done

    cat > ${WORKDIR}/build/flash.sh <<FLASHEOF
#!/bin/sh
# Flash LuneOS to ${MACHINE} over fastboot: $flashed and userdata.
#
# The bootloader has to be unlocked first, which wipes the device:
#
#     fastboot flashing unlock
#
# userdata carries the rootfs, so flashing it discards everything on the device.
# "flash.sh --debug" flashes boot-debug.img (if this package has one), which stops
# in the initramfs with adb enabled.
set -e
cd "\$(dirname "\$0")"

BOOT=boot.img
if [ "\$1" = "--debug" ] ; then
    [ -e boot-debug.img ] || { echo "this package has no debug boot image"; exit 1; }
    BOOT=boot-debug.img
fi

fastboot getvar product
$vbmeta_line$boot_lines
fastboot flash userdata userdata.img
fastboot reboot
FLASHEOF
    chmod +x ${WORKDIR}/build/flash.sh

    (cd ${WORKDIR}/build ; zip -r ${DEPLOYDIR}/${ZIP_NAME}.zip *)
}

do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy[depends] += "${IMAGE_NAME}:do_image_complete ${FASTBOOT_BOOT_DEPENDS}"
addtask deploy after do_install before do_package
