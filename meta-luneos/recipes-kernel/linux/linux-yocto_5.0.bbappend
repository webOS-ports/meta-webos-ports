FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
    file://virtio.cfg \
"

# we have our own version of virtio.cfg
KERNEL_FEATURES_remove_qemuall = "cfg/virtio.scc"
