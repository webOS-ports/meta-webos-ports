FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Use-dbus-system-bus-instead-of-session-bus-for-dbus-.patch \
    file://0002-Set-XDG_RUNTIME_DIR-inside-maliit-server-statically.patch \
    file://0003-Add-webos-platform-implementation-to-set-correct-win.patch \
    file://maliit-server.conf \
    file://maliit-server.service \
    file://maliit-env.conf \
"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "maliit-server.service"

do_install_append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/maliit-server.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/maliit-server.service ${D}${systemd_unitdir}/system/

    install -d ${D}${sysconfdir}/luna-next
    install -m 0644 ${WORKDIR}/maliit-env.conf ${D}${sysconfdir}/luna-next/
}

pkg_postinst_${PN} () {
}

pkg_postrm_${PN} () {
}

# Enable webos specific support within maliit
EXTRA_QMAKEVARS_PRE += "CONFIG+=webos"

# We want maliit to provide it's input context on it's own rather than qtbase doing this
# so we build it here and package it. However the inputcontext within qtbase must be moved
# into a separate package otherwise two packages will install the same file into the rootfs
EXTRA_QMAKEVARS_PRE += "CONFIG+=qt5-inputcontext"
FILES_${PN} += "${libdir}/qt5/plugins/platforminputcontexts"
FILES_${PN}-dbg += "${libdir}/qt5/plugins/platforminputcontexts/.debug"
