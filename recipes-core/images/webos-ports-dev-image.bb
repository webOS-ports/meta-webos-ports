DESCRIPTION = "Developers webOS Ports image."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

IMAGE_FEATURES += "${WEBOS_PORTS_IMAGE_DEFAULT_FEATURES} webos-ports-development"

inherit webos-ports-image

webos_enable_devmode() {
    touch ${IMAGE_ROOTFS}/var/usb-debugging-enabled
}

ROOTFS_POSTPROCESS_COMMAND += "webos_enable_devmode;"
