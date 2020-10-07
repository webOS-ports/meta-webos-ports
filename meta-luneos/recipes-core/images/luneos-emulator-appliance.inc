DESCRIPTION = "LuneOS emulator appliance"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS:prepend := "${THISDIR}/luneos-emulator-appliance:"
SRC_URI = "file://luneos-emulator.ovf"

IMAGE_BASENAME = "luneos"

SRC_VMDK_NAME = "${IMAGE_BASENAME}-image-${MACHINE}${IMAGE_NAME_SUFFIX}.wic.vmdk"
VMDK_NAME     = "${IMAGE_BASENAME}-emulator-${MACHINE}${IMAGE_VERSION_SUFFIX}.vmdk"
OVF_NAME      = "${IMAGE_BASENAME}-emulator-${MACHINE}${IMAGE_VERSION_SUFFIX}.ovf"
ZIP_NAME      = "${IMAGE_BASENAME}-emulator-${MACHINE}${IMAGE_VERSION_SUFFIX}.tar.gz"

do_deploy() {
    if [ ! -e ${DEPLOY_DIR_IMAGE}/${SRC_VMDK_NAME} ] ; then
        bbfatal "Required base image ${DEPLOY_DIR_IMAGE}/${SRC_VMDK_NAME} is not available!"
    fi

    rm -rf ${WORKDIR}/appliance
    mkdir -p ${WORKDIR}/appliance
    sed "s/luneos-emulator-disk.vmdk/${VMDK_NAME}/g" ${WORKDIR}/luneos-emulator.ovf > ${WORKDIR}/appliance/${OVF_NAME}
    sed -i "s/LuneOS Emulator/LuneOS-${MACHINE}${IMAGE_VERSION_SUFFIX}/g" ${WORKDIR}/appliance/${OVF_NAME}
    ln -sf ${DEPLOY_DIR_IMAGE}/${SRC_VMDK_NAME} ${WORKDIR}/appliance/${VMDK_NAME}

    (cd ${WORKDIR}/appliance ; tar cvhf - ${OVF_NAME} ${VMDK_NAME} | pigz > ${DEPLOYDIR}/${ZIP_NAME} )
}

# only qemux86, qemux86-64 provide luneos-emulator.ovf file
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:qemux86 = "(.*)"
COMPATIBLE_MACHINE:qemux86-64 = "(.*)"

INHIBIT_DEFAULT_DEPS = "1"

inherit deploy nopackages

do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy[depends] += "${IMAGE_BASENAME}-image:do_image_complete virtual/kernel:do_deploy pigz-native:do_populate_sysroot"

addtask deploy after do_install before do_package
