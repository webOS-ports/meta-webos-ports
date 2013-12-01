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

# switch from upstart to systemd
WEBOS_DISTRO_FEATURES_INIT = "systemd"
WEBOS_DISTRO_FEATURES_INIT_CONSIDERED = "sysvinit"
VIRTUAL-RUNTIME_initscripts = ""
VIRTUAL-RUNTIME_init_manager = "systemd systemd-compat-units systemd-machine-units"
BAD_RECOMMENDATIONS = "update-rc.d"

# Enable adb support
DISTRO_FEATURES += "adb"

# For proper support of wayland through the whole stack we need to set it as distro
# feature (e.g. mesa requiers it to be set to compile the egl platform module)
DISTRO_FEATURES += "wayland"

VIRTUAL-RUNTIME_webappmanager = "webapp-launcher"
VIRTUAL-RUNTIME_webos-compositor = "luna-next"
VIRTUAL-RUNTIME_webos-ime = "webos-keyboard"

inherit android_image_types

OPKGLIBDIR_forcevariable := "${localstatedir}/lib"
