FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 3}"

# NOTE: tuna-fix-screen-size.patch is only needed until upstream is using
# FBIOGET_VSCREENINFO for screen size detection and no statically coded values
SRC_URI_append_tuna = " \
    file://add-support-for-tuna-machine.patch \
    file://tuna-fix-screen-size.patch"
