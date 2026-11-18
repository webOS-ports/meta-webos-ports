DESCRIPTION = "LuneOS package for an Android recovery: the fastboot package's images and a shell installer"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "zip-native coreutils-native openssl-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_BASENAME = "luneos"
IMAGE_NAME = "${IMAGE_BASENAME}-image"

ZIP_BASENAME = "${IMAGE_BASENAME}-recovery-package-${MACHINE}"
ZIP_NAME = "${ZIP_BASENAME}${IMAGE_VERSION_SUFFIX}"

FILESEXTRAPATHS:prepend := "${THISDIR}/luneos-recovery-package:"
SRC_URI = " \
    file://update-binary \
    file://sign-recovery-zip.py \
    file://luneos-recovery.x509.pem \
"

inherit kernel-artifact-names deploy nopackages

# For a device that keeps its stock LineageOS-based recovery. luneos-package
# does not work there: its updater is 32-bit edify using /sbin/mount, it unpacks
# a tarball into /data, which such a recovery formats as encrypted f2fs, and it
# writes boot to one fixed partition. This package writes partition images
# instead, the same ones as luneos-fastboot-package and from the same variables,
# to the active slot.
#
# The recovery's own device name, which the installer checks before it writes
# anything. A machine opts in by setting it.
RECOVERY_PACKAGE_DEVICE ??= ""

# A LineageOS recovery of a user build refuses an unsigned package, with no way
# to confirm it. So the package is signed, and the machine's initramfs adds
# luneos-recovery.x509.pem to the recovery's otacerts.zip (see meta-brax's
# initramfs-scripts-halium). The private key is in luneos-secrets; this is its path.
RECOVERY_PACKAGE_SIGNING_KEY ??= ""

# The recovery's unzip holds a whole entry in memory, so userdata goes into the
# zip in pieces of this many MiB. The installer writes them back to back.
RECOVERY_PACKAGE_PART_MIB ?= "256"

do_deploy[vardepsexclude] += "RECOVERY_PACKAGE_SIGNING_KEY"

FASTBOOT_VBMETA_IMAGE ?= "vbmeta-disabled.img"
FASTBOOT_VBMETA_PARTITION ?= "vbmeta"
FASTBOOT_BOOT_IMAGE ?= "${KERNEL_IMAGETYPE}.fastboot"
FASTBOOT_BOOT_DEPENDS ?= "virtual/kernel:do_deploy"
FASTBOOT_INIT_BOOT_IMAGE ?= ""

python () {
    if not d.getVar('RECOVERY_PACKAGE_DEVICE'):
        raise bb.parse.SkipRecipe('the machine does not set RECOVERY_PACKAGE_DEVICE')
    if not d.getVar('RECOVERY_PACKAGE_SIGNING_KEY'):
        raise bb.parse.SkipRecipe('RECOVERY_PACKAGE_SIGNING_KEY is not set (luneos-secrets)')
}

do_deploy() {
    USERDATA="${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}${IMAGE_NAME_SUFFIX}.userdataimg"
    BOOT="${DEPLOY_DIR_IMAGE}/${FASTBOOT_BOOT_IMAGE}"
    INSTALLER=META-INF/com/google/android

    [ -e "$USERDATA" ] || bbfatal "No userdata image at $USERDATA. Add userdataimg to IMAGE_FSTYPES for this machine."
    [ -e "$BOOT" ] || bbfatal "Required boot image $BOOT doesn't exist."

    rm -rf ${WORKDIR}/build
    mkdir -p ${WORKDIR}/build/$INSTALLER

    cp "$BOOT" ${WORKDIR}/build/boot.img
    USERDATA_SHA=$(sha256sum "$USERDATA" | cut -d' ' -f1)
    USERDATA_SIZE=$(stat -L -c %s "$USERDATA")
    split -b ${RECOVERY_PACKAGE_PART_MIB}M -d -a 3 "$USERDATA" ${WORKDIR}/build/userdata.img.
    USERDATA_PARTS=$(ls ${WORKDIR}/build/userdata.img.* | wc -l)

    if [ -n "${FASTBOOT_INIT_BOOT_IMAGE}" ] ; then
        [ -e "${DEPLOY_DIR_IMAGE}/${FASTBOOT_INIT_BOOT_IMAGE}" ] || \
            bbfatal "init_boot image ${DEPLOY_DIR_IMAGE}/${FASTBOOT_INIT_BOOT_IMAGE} doesn't exist."
        cp "${DEPLOY_DIR_IMAGE}/${FASTBOOT_INIT_BOOT_IMAGE}" ${WORKDIR}/build/init_boot.img
    fi

    # The installer writes vbmeta as it is, without fastboot's --disable-verity
    # --disable-verification, so the flags have to be 3 in the image itself.
    if [ -n "${FASTBOOT_VBMETA_PARTITION}" ] ; then
        [ "${FASTBOOT_VBMETA_PARTITION}" = "vbmeta" ] || bbfatal "The installer only writes a partition named vbmeta."
        VBMETA="${DEPLOY_DIR_IMAGE}/${FASTBOOT_VBMETA_IMAGE}"
        [ -e "$VBMETA" ] || VBMETA=""
        python3 - ${WORKDIR}/build/vbmeta.img $VBMETA <<'PYEOF'
import struct, sys
if len(sys.argv) > 2:
    image = bytearray(open(sys.argv[2], 'rb').read())
    if image[:4] != b'AVB0':
        sys.exit('not a vbmeta image: ' + sys.argv[2])
else:
    fmt = '>4sIIQQI10QQII48s80x'
    image = bytearray(struct.pack(fmt, b'AVB0', 1, 0, 0, 0, 0,
                                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                  0, 0, 0, b'avbtool 1.2.0').ljust(4096, b'\0'))
# AvbVBMetaImageHeader.flags: HASHTREE_DISABLED | VERIFICATION_DISABLED
struct.pack_into('>I', image, 120, 3)
open(sys.argv[1], 'wb').write(image)
PYEOF
    fi

    sed -e 's:###DEVICE###:${RECOVERY_PACKAGE_DEVICE}:g' \
        -e 's:###PART_MIB###:${RECOVERY_PACKAGE_PART_MIB}:g' ${UNPACKDIR}/update-binary \
        > ${WORKDIR}/build/$INSTALLER/update-binary
    chmod 0755 ${WORKDIR}/build/$INSTALLER/update-binary
    echo "# The installer is update-binary, a shell script." > ${WORKDIR}/build/$INSTALLER/updater-script

    # "<sha256> <size> <parts> <name>", which the installer checks each partition
    # against. The sum is of the whole image, as it ends up on the partition.
    (cd ${WORKDIR}/build
     for image in *.img ; do
         echo "$(sha256sum $image | cut -d' ' -f1) $(stat -L -c %s $image) 0 $image"
     done > SHA256SUMS
     echo "$USERDATA_SHA $USERDATA_SIZE $USERDATA_PARTS userdata.img" >> SHA256SUMS)

    (cd ${WORKDIR}/build ; zip -r ${DEPLOYDIR}/${ZIP_NAME}.zip *)
    python3 ${UNPACKDIR}/sign-recovery-zip.py "${RECOVERY_PACKAGE_SIGNING_KEY}" \
        ${UNPACKDIR}/luneos-recovery.x509.pem ${DEPLOYDIR}/${ZIP_NAME}.zip
}

do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy[depends] += "${IMAGE_NAME}:do_image_complete ${FASTBOOT_BOOT_DEPENDS}"
addtask deploy after do_install before do_package
