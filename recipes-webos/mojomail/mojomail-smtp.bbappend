FILESEXTRAPATHS_prepend := "${THISDIR}/mojomail:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI_append_arm = " file://smtp-arm-fix.patch;striplevel=2"
