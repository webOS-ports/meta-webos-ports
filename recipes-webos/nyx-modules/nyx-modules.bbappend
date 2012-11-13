FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

# For the tuna device we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"

inherit webos-ports-submissions

SRCREV = "14f9013ff981ca7736f2fbcc72791bfdb0cccfd2"
