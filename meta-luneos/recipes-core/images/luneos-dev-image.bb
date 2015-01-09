require luneos-image.inc

DESCRIPTION = "Developers LuneOS image"

IMAGE_FEATURES += "luneos-development"

webos_enable_devmode() {
    install -d ${IMAGE_ROOTFS}/var/luna
    touch ${IMAGE_ROOTFS}/var/luna/dev-mode-enabled
    touch ${IMAGE_ROOTFS}/var/usb-debugging-enabled
    echo "LUNA_NEXT_DEBUG=1" >> ${IMAGE_ROOTFS}${sysconfdir}/luna-next/environment.conf
}

ROOTFS_POSTPROCESS_COMMAND += "webos_enable_devmode;"
