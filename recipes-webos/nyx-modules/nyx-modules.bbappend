FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

# For the tuna device we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"

inherit webos-ports-submissions

SRCREV = "9cbf89ba1418d803766f8b45209197ab961d77af"
