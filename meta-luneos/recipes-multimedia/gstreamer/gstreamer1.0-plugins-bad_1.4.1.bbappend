FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-camerabin-update-zoom-param-spec-if-video-source-cha.patch \
    file://0001-Set-video-branch-to-NULL-after-finishing-video-recor.patch \
    file://0002-Keep-video-branch-in-NULL-state.patch \
    file://0003-photography-add-missing-vmethods.patch \
    file://0004-camerabin-install-GST_PHOTOGRAPHY_PROP_EXPOSURE_MODE.patch \
    file://0005-Downgrade-mpeg4videoparse-to-prevent-it-from-being-p.patch \
"
