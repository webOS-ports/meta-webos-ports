DESCRIPTION = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "zip-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_NAME = "webos-ports-dev-image"

PV = "1.0+gitr${SRCPV}"

SRC_URI = "git://github.com/webOS-ports/android-update-package.git;protocol=git;branch=master"
SRCREV = "9af548e4cae44f2139cfb8fdc34e9b63fa8848a6"

do_deploy() {
    if [ ! -e ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.tar.gz ] ; then
        bbfatal "Required base image is not available as tar.gz image!"
    fi

    rm -rf ${WORKDIR}/build
    mkdir -p ${WORKDIR}/build
    ln -sf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.tar.gz ${WORKDIR}/build/webos-rootfs.tar.gz

    cp -r ${WORKDIR}/git/* ${WORKDIR}/build
    chmod +x ${WORKDIR}/build/webos_deploy.sh

    (cd ${WORKDIR}/build ; zip -r ${DEPLOY_DIR_IMAGE}/webos-ports-package-${MACHINE}-${DATETIME}.zip *)

    ln -sf webos-ports-package-${MACHINE}-${DATETIME}.zip ${DEPLOY_DIR_IMAGE}/webos-ports-package-${MACHINE}.zip
}

do_deploy[depends] += "${IMAGE_NAME}:do_build"
DEPENDS += "${IMAGE_NAME}"

addtask deploy after do_install before do_package
