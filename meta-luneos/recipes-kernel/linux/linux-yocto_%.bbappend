FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
    file://virtio.cfg \
    file://crypto.cfg \
    file://waydroid-anbox.cfg \
    file://yama.cfg \
"

# For the qemu images: testing the Bluetooth stack without a radio.
SRC_URI:append:qemuall = " file://bluetooth-vhci.cfg"

# For the qemu images: a fake battery, since they have none.
SRC_URI:append:qemuall = " file://test-power.cfg"

# we have our own version of virtio.cfg
KERNEL_FEATURES:remove:qemuall = "cfg/virtio.scc"
