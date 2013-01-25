FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 6}"

# For the tuna device we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"
WEBOS_TARGET_MACHINE_IMPL_grouper = "device"

inherit webos-ports-submissions

DEPENDS += "mtdev"

SRCREV = "6308b1f0d99fa02340acbc637d4e30b29095eaff"
