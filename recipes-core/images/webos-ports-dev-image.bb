require webos-ports-image.inc

DESCRIPTION = "Developers webOS Ports image"

IMAGE_FEATURES += "webos-ports-development"

webos_enable_devmode() {
    install -d ${IMAGE_ROOTFS}/var/luna
    touch ${IMAGE_ROOTFS}/var/luna/dev-mode-enabled
    touch ${IMAGE_ROOTFS}/var/usb-debugging-enabled
}

ROOTFS_POSTPROCESS_COMMAND += "webos_enable_devmode;"
