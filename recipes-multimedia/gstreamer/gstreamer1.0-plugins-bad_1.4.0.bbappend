FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-gralloc-allocator.patch \
    file://0002-Downgrade-mpeg4videoparse-to-prevent-it-from-being-p.patch \
    file://0004-Set-video-branch-to-NULL-after-finishing-video-recor.patch \
    file://0005-Keep-video-branch-in-NULL-state.patch \
    file://0006-photography-add-missing-vmethods.patch \
    file://0007-camerabin-install-GST_PHOTOGRAPHY_PROP_EXPOSURE_MODE.patch \
"
