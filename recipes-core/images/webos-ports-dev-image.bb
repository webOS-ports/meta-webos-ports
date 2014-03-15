require webos-ports-image.inc

DESCRIPTION = "Developers webOS Ports image"

IMAGE_FEATURES += "webos-ports-development"

webos_enable_devmode() {
    touch ${IMAGE_ROOTFS}/var/usb-debugging-enabled
}

ROOTFS_POSTPROCESS_COMMAND += "webos_enable_devmode;"
