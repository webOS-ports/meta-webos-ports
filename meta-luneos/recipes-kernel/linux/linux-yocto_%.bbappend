FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
    file://virtio.cfg \
    file://crypto.cfg \
    file://waydroid-anbox.cfg \
"

# For the qemu images: testing the Bluetooth stack without a radio.
SRC_URI:append:qemuall = " file://bluetooth-vhci.cfg"

# we have our own version of virtio.cfg
KERNEL_FEATURES:remove:qemuall = "cfg/virtio.scc"
