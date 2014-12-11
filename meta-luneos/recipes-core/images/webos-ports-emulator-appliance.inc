DESCRIPTION = "webOS Ports emulator appliance"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "zip-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/webos-ports-emulator-appliance:"
SRC_URI = "file://webos-ports-emulator.ovf"

IMAGE_BASENAME = "webos-ports"
IMAGE_NAME = "${IMAGE_BASENAME}-image"

ZIP_BASENAME = "${IMAGE_BASENAME}-emulator-${MACHINE}"
ZIP_NAME = "${ZIP_BASENAME}-${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}.zip"

VMDK_BASENAME = "${IMAGE_BASENAME}-emulator-${MACHINE}"
VMDK_NAME = "${VMDK_BASENAME}-${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}.vmdk"

OVF_BASENAME = "${IMAGE_BASENAME}-emulator-${MACHINE}"
OVF_NAME = "${OVF_BASENAME}-${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}.ovf"

do_deploy() {
    if [ ! -e ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.vmdk ] ; then
        bbfatal "Required base image is not available as vmdk image!"
    fi

    rm -rf ${WORKDIR}/appliance
    mkdir -p ${WORKDIR}/appliance
    sed "s/webos-ports-emulator-disk.vmdk/${VMDK_NAME}/g" ${WORKDIR}/webos-ports-emulator.ovf > ${WORKDIR}/appliance/${OVF_NAME}
    sed -i "s/webOS ports Emulator/${DESCRIPTION} ${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}/g" ${WORKDIR}/appliance/${OVF_NAME}
    ln -sf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.vmdk ${WORKDIR}/appliance/${VMDK_NAME}

    (cd ${WORKDIR}/appliance ; zip ${DEPLOY_DIR_IMAGE}/${ZIP_NAME} ${OVF_NAME} ${VMDK_NAME})

    ln -sf ${ZIP_NAME} ${DEPLOY_DIR_IMAGE}/${ZIP_BASENAME}.zip
}

do_deploy[depends] += "${IMAGE_NAME}:do_build"

addtask deploy after do_install before do_package
