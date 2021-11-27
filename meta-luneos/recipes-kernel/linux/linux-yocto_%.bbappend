FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
    file://virtio.cfg \
    file://waydroid-anbox.cfg \
"

# we have our own version of virtio.cfg
KERNEL_FEATURES:remove:qemuall = "cfg/virtio.scc"
