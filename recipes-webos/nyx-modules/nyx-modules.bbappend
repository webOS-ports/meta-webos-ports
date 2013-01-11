FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 6}"

# For the tuna device we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"
WEBOS_TARGET_MACHINE_IMPL_grouper = "device"

inherit webos-ports-submissions

DEPENDS += "mtdev"

SRCREV = "3ec447ed76b891b1eb5d2c863ea3dd2ac7609cb7"
