# Settings specific for webos-ports

DEPLOY_DIR_IMAGE = "${DEPLOY_DIR}/images/${MACHINE}"

LICENSE_CREATE_PACKAGE = "1"

require conf/distro/include/defaulttunes.inc

#############################################################################
# Ensure MACHINE_CLASS is in OVERRIDES and is not empty as :: in OVERRIDES
# breaks order of OVERRIDES ie in FILESPATH
#############################################################################
MACHINE_CLASS ?= "${MACHINE}"
MACHINEOVERRIDES .= ":${MACHINE_CLASS}"
MACHINEOVERRIDES[vardepsexclude] = "MACHINE MACHINE_CLASS"
OVERRIDES[vardepsexclude] = "MACHINE MACHINE_CLASS"
