FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

# For the tuna device we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"

inherit webos-ports-submissions

SRCREV = "bc2a48f5246ff7c5e6e9bf217c052742859daf7d"
