DESCRIPTION = "webOS Ports emulator appliance"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "zip-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://webos-ports-emulator.ovf"

IMAGE_NAME = "webos-ports-dev-image"

do_deploy() {
    if [ ! -e ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.vmdk ] ; then
        bbfatal "Required base image is not available as vmdk image!"
    fi

    rm -rf ${WORKDIR}/appliance
    mkdir -p ${WORKDIR}/appliance
    cp ${WORKDIR}/webos-ports-emulator.ovf ${WORKDIR}/appliance
    ln -sf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.vmdk ${WORKDIR}/appliance/webos-ports-emulator-disk.vmdk

    (cd ${WORKDIR}/appliance ; zip ${DEPLOY_DIR_IMAGE}/webos-ports-emulator-${MACHINE}-${DATETIME}.zip \
        webos-ports-emulator.ovf webos-ports-emulator-disk.vmdk)

    ln -sf webos-ports-emulator-${MACHINE}-${DATETIME}.zip ${DEPLOY_DIR_IMAGE}/webos-ports-emulator-${MACHINE}.zip
}

do_deploy[depends] += "webos-ports-dev-image:do_build"
DEPENDS += "webos-ports-dev-image"

addtask deploy after do_install before do_package
