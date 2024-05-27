# Copyright (c) 2014-2024 LG Electronics, Inc.

EXTENDPRAUTO:append = "webos6"

PACKAGES =+ "${PN}-gpl"
LICENSE += "& GPL-2.0-only"
LICENSE:${PN}-gpl = "GPL-2.0-only"

RDEPENDS:${PN} += "${PN}-gpl"

FILES:${PN}-gpl = " \
    ${bindir}/dbus-cleanup-sockets \
    ${bindir}/dbus-daemon \
    ${bindir}/dbus-monitor \
    ${bindir}/dbus-send \
    ${bindir}/dbus-uuidgen \
"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS:${PN}-ptest:append:class-target = " ${VIRTUAL-RUNTIME_bash}"
RDEPENDS:${PN}-ptest:remove:class-target = "${@oe.utils.conditional('WEBOS_PREFERRED_PROVIDER_FOR_BASH', 'busybox', 'bash', '', d)}"

# This part is introduced for LuneOS, to create a DBus session (needed by voicecall, for instance)
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://dbus-session.service \
    file://startup-dbus-session.sh \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/dbus-session.service ${D}${systemd_unitdir}/system/

    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/startup-dbus-session.sh ${D}${bindir}/
}

FILES:${PN} += "${bindir} ${systemd_unitdir}"
