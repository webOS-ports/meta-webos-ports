PRINC := "${@int(PRINC) + 6}"

# For the tuna device we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"
WEBOS_TARGET_MACHINE_IMPL_grouper = "device"

inherit webos-ports-submissions

DEPENDS += "mtdev"

SRCREV = "af0a4ebd65b98ee5749cff56a9cda43d4a06a20a"
