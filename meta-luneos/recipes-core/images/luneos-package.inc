DESCRIPTION = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "zip-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_BASENAME = "luneos"
IMAGE_NAME = "${IMAGE_BASENAME}-image"

ZIP_BASENAME = "${IMAGE_BASENAME}-package-${MACHINE}"
ZIP_NAME = "${ZIP_BASENAME}-${IMAGE_VERSION_SUFFIX}"

inherit webos_ports_repo kernel-artifact-names

PV = "1.0.2+git${SRCPV}"
SRCREV = "12c8eb4d79e4504a7683b8dc18266a9c460d2537"

WEBOS_REPO_NAME = "android-update-package"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_deploy() {
    IMG="${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.tar.gz"
    FASTBOOT="${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${KERNEL_IMAGE_LINK_NAME}.fastboot"
    if [ ! -e $IMG ] ; then
        bbfatal "Required base image is not available as tar.gz image! $IMG doesn't exist."
    fi
    if [ ! -e $FASTBOOT ] ; then
        bbfatal "Required kernel image is not available as zImage fastboot image! $FASTBOOT doesn't exist."
    fi

    rm -rf ${WORKDIR}/build
    mkdir -p ${WORKDIR}/build
    ln -sf $IMG ${WORKDIR}/build/webos-rootfs.tar.gz
    ln -sf $FASTBOOT ${WORKDIR}/build/boot.img

    cp -r ${WORKDIR}/git/* ${WORKDIR}/build
    chmod +x ${WORKDIR}/build/webos_deploy.sh

    sed -i -e s:\#\#\#BOOT_PARTITION\#\#\#:${ANDROID_BOARD_BOOTIMAGE_PARTITION}:g \
        ${WORKDIR}/build/META-INF/com/google/android/updater-script

    (cd ${WORKDIR}/build ; zip -r ${DEPLOY_DIR_IMAGE}/${ZIP_NAME}.zip *)

    ln -sf ${ZIP_NAME}.zip ${DEPLOY_DIR_IMAGE}/${ZIP_BASENAME}.zip
}

do_deploy[depends] += "${IMAGE_NAME}:do_image_complete virtual/kernel:do_deploy"

addtask deploy after do_install before do_package
