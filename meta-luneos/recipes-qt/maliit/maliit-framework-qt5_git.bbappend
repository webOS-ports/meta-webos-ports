FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Use-dbus-system-bus-instead-of-session-bus-for-dbus-.patch \
    file://0003-Add-webos-platform-implementation-to-set-correct-win.patch \
    file://maliit-server.conf \
    file://maliit-server.service \
    file://maliit-env.conf \
"

SRCREV = "62bd54bcde3c0d79337a0eb7361ff6d7375bc73c"

# Do not recomment maliit-plugins-qt5 as keyboard provider
RRECOMMENDS:${PN} = ""

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "maliit-server.service"

do_install:append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/maliit-server.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/maliit-server.service ${D}${systemd_unitdir}/system/

    install -d ${D}${sysconfdir}/maliit
    install -m 0644 ${WORKDIR}/maliit-env.conf ${D}${sysconfdir}/maliit/

    install -d ${D}${localstatedir}/lib/maliit
}

# we don't have /etc/xdg/autostart/maliit-server.desktop
pkg_postinst_ontarget:${PN} () {
}

pkg_postrm:${PN} () {
}

# Enable LuneOS/webOS specific support within maliit
EXTRA_QMAKEVARS_PRE += "\
    CONFIG+=webos \
    MALIIT_DEFAULT_HW_PLUGIN=libluneos-keyboard-plugin.so \
    MALIIT_DEFAULT_PLUGIN=libluneos-keyboard-plugin.so \
"

# We want maliit to provide it's input context on it's own rather than qtbase doing this
# so we build it here and package it. However the inputcontext within qtbase must be moved
# into a separate package otherwise two packages will install the same file into the rootfs
EXTRA_QMAKEVARS_PRE += "CONFIG+=qt5-inputcontext"
FILES:${PN} += "${libdir}/qt5/plugins/platforminputcontexts"
