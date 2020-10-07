DESCRIPTION = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "zip-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_BASENAME = "luneos"
IMAGE_NAME = "${IMAGE_BASENAME}-image"

ZIP_BASENAME = "${IMAGE_BASENAME}-package-${MACHINE}"
ZIP_NAME = "${ZIP_BASENAME}${IMAGE_VERSION_SUFFIX}"

inherit webos_ports_repo kernel-artifact-names deploy nopackages

PV = "1.0.3+git${SRCPV}"
SRCREV = "b69bf00f602138628521184b7f6d0773736d5f8b"

WEBOS_REPO_NAME = "android-update-package"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_deploy() {
    IMG="${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}${IMAGE_NAME_SUFFIX}.tar.gz"
    FASTBOOT="${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}.fastboot"
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

    cp ${DEPLOY_DIR_IMAGE}/busybox-static ${WORKDIR}/build/

    sed -i -e s:\#\#\#BOOT_PARTITION\#\#\#:${ANDROID_BOARD_BOOTIMAGE_PARTITION}:g \
        ${WORKDIR}/build/META-INF/com/google/android/updater-script

    (cd ${WORKDIR}/build ; zip -r ${DEPLOYDIR}/${ZIP_NAME}.zip *)
}

do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy[depends] += "${IMAGE_NAME}:do_image_complete virtual/kernel:do_deploy busybox-static:do_deploy"

addtask deploy after do_install before do_package
