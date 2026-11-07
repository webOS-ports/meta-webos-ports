# adbd starts only while USB debugging and developer mode are both on; see the
# drop-in. Each host also has to be allowed by the user (key authorisation:
# android-tools_35%.bbappend, or android-tools_5.1%.bbappend for 5.1.1);
# adbd runs as root.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://adbd-devmode.conf"

do_install:append:class-target() {
    install -D -m 0644 ${UNPACKDIR}/adbd-devmode.conf \
        ${D}${systemd_system_unitdir}/android-tools-adbd.service.d/05-devmode.conf
    # The keys' directory: root's alone. devmode writes adb_keys in it (0600).
    install -d -m 0700 ${D}${localstatedir}/lib/adbd
}

FILES:${PN}-adbd += " \
    ${systemd_system_unitdir}/android-tools-adbd.service.d/05-devmode.conf \
    ${localstatedir}/lib/adbd \
"
