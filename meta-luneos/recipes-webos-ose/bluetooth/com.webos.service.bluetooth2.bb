# Copyright (c) 2014-2025 LG Electronics, Inc.

SUMMARY = "webOS Bluetooth management service"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=2763f3ed850f8412903ea776e0526bea \
    file://oss-pkg-info.yaml;md5=1ff1242ee7d960d745e8abafd26f2471 \
"

DEPENDS = " \
    bluetooth-sil-api \
    glib-2.0 \
    libpbnjson \
    luna-service2 \
"

# Set the default Bluetooth SIL that the service will dynamically load to the
# specified by BLUETOOTH_SIL_NAME
BLUETOOTH_SIL_NAME ?= "bluez5"
BLUETOOTH_SIL_COMPONENT = "${@ ' bluetooth-sil-'.join([''] + '${BLUETOOTH_SIL_NAME}'.split(' '))}"
RDEPENDS:${PN} = " \
    ${BLUETOOTH_SIL_COMPONENT} \
"

# Set WEBOS_BLUETOOTH_ENABLED_SERVICE_CLASSES to a space-separated list of
# service classes to be supported by the webOS Bluetooth management service at
# runtime. The assignments are intended to be done in DISTRO.conf and/or
# MACHINE.conf; if none have been done, don't enable support for any service
# classes. Note that including a particular service class might introduce
# additional dependencies into the build image (which is why the setting must be
# global).
WEBOS_BLUETOOTH_ENABLED_SERVICE_CLASSES = "GATT FTP OPP A2DP SPP HFP AVRCP PAN ANCS PBAP MAP MESH"

# Set WEBOS_BLUETOOTH_PAIRING_IO_CAPABILITY in MACHINE.conf to configure the
# Bluetooth pairing IO capability of the adapter. The capability is expected to
# be one of the capabilities mentioned in the Bluetooth specification,
# Bluetooth 4.1 vol 3 Part H "Security Manager Specification" Chapter 2.3.2
# "IO Capabilities". Following are the possible values:
#
#   "DisplayYesNo"
#   "DisplayOnly"
#   "NoInputNoOutput"
#   "KeyboardDisplay"
#   "KeyboardOnly"
# LuneOS uses "DisplayYesNo": Settings' Bluetooth page (LuneOS.Bluetooth's
# LuneOSBluetoothAgent) shows the passkey to compare and asks before a device
# pairs. bluetoothd only takes it with bluez5's patch 0014 gone, which made
# every agent NoInputNoOutput.
WEBOS_BLUETOOTH_PAIRING_IO_CAPABILITY ??= "DisplayYesNo"

# Built from the webOS-ports fork: webosose submissions/79 with the LuneOS
# changes as commits, the two patches this recipe used to apply among them.
# Plain SRCREV: submission tags are a webosose convention.
PV = "1.0.0-79+git"
# herrie/bluetooth2-migration until its pull request is merged:
# /device/connect, /device/disconnect and Just Works prompts.
SRCREV = "872d928bf92d7a5908f0f5119aae59d69211792e"
WEBOS_GIT_PARAM_BRANCH = "herrie/bluetooth2-migration"
PR = "r13"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
# VIRTUAL-RUNTIME_bluetooth_stack, WEBOS_BLUETOOTH_PAIRING_IO_CAPABILITY, and
# possibly WEBOS_BLUETOOTH_ENABLED_SERVICE_CLASSES are MACHINE-dependent.

# The convention is for the component "bluetooth-sil-<name>" to build a <name>.so,
# so just pass in <name>.
EXTRA_OECMAKE += "-DWEBOS_BLUETOOTH_SIL:STRING='${BLUETOOTH_SIL_NAME}'"

EXTRA_OECMAKE += "-DWEBOS_BLUETOOTH_ENABLED_SERVICE_CLASSES:STRING='${WEBOS_BLUETOOTH_ENABLED_SERVICE_CLASSES}'"

EXTRA_OECMAKE += "-DWEBOS_BLUETOOTH_PAIRING_IO_CAPABILITY:STRING='${WEBOS_BLUETOOTH_PAIRING_IO_CAPABILITY}'"

PACKAGECONFIG[support-response-bt-prepare-suspend-done] = "-DSUPPORT_RESPONSE_BT_PREPARE_SUSPEND_DONE:BOOL=true,-DSUPPORT_RESPONSE_BT_PREPARE_SUSPEND_DONE:BOOL=false,"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "webos-bluetooth-service.service"
WEBOS_SYSTEMD_SCRIPT = "webos-bluetooth-service.sh"

inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = " \
    -u 1002 -d /var -s /usr/sbin/nologin -G netdev -U bluetooth ;\
"

GROUPMEMS_PARAM:${PN} = " \
    -a bluetooth -g netdev; \
"

GROUPADD_PARAM:${PN} = " \
    -g 2024 -f blemesh; \
"

#Below group only is required for webOS Auto
GROUPADD_PARAM:${PN} += "${@oe.utils.conditional('DISTRO', 'webos-auto', '-g -f 2025 vcardshare', '', d)}"
