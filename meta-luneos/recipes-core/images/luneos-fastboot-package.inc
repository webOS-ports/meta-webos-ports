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
# vbmeta is not built here - it is derived from the device's own stock vbmeta
# with the verification flags set - so it is picked up from the deploy
# directory when the machine ships one, and the flash script leaves it out
# when it does not.
FASTBOOT_VBMETA_IMAGE ?= "vbmeta-disabled.img"

do_deploy() {
    USERDATA="${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}${IMAGE_NAME_SUFFIX}.userdataimg"
    FASTBOOT="${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}.fastboot"

    if [ ! -e $USERDATA ] ; then
        bbfatal "No userdata image at $USERDATA. Add userdataimg to IMAGE_FSTYPES for this machine."
    fi
    if [ ! -e $FASTBOOT ] ; then
        bbfatal "Required kernel image is not available as a fastboot boot image! $FASTBOOT doesn't exist."
    fi

    rm -rf ${WORKDIR}/build
    mkdir -p ${WORKDIR}/build

    cp $FASTBOOT ${WORKDIR}/build/boot.img
    cp $USERDATA ${WORKDIR}/build/userdata.img

    vbmeta_line=""
    if [ -e "${DEPLOY_DIR_IMAGE}/${FASTBOOT_VBMETA_IMAGE}" ] ; then
        cp "${DEPLOY_DIR_IMAGE}/${FASTBOOT_VBMETA_IMAGE}" ${WORKDIR}/build/vbmeta.img
        vbmeta_line='fastboot flash vbmeta   vbmeta.img'
    else
        bbnote "No ${FASTBOOT_VBMETA_IMAGE} in the deploy directory; the flash script will say so."
        vbmeta_line='echo "vbmeta.img is not part of this package - flash the device'"'"'s own patched vbmeta once, by hand."'
    fi

    cat > ${WORKDIR}/build/flash.sh <<FLASHEOF
#!/bin/sh
# Flash LuneOS to ${MACHINE} over fastboot.
#
# The bootloader has to be unlocked first, which wipes the device:
#
#     fastboot flashing unlock
#
# vbmeta only needs writing once - it stays patched across updates - while
# boot and userdata are what an update replaces. userdata carries the rootfs,
# so flashing it discards everything on the device.
set -e

$vbmeta_line
fastboot flash boot_a   boot.img
fastboot flash boot_b   boot.img
fastboot flash userdata userdata.img
fastboot reboot
FLASHEOF
    chmod +x ${WORKDIR}/build/flash.sh

    (cd ${WORKDIR}/build ; zip -r ${DEPLOYDIR}/${ZIP_NAME}.zip *)
}

do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy[depends] += "${IMAGE_NAME}:do_image_complete virtual/kernel:do_deploy"
addtask deploy after do_install before do_package
