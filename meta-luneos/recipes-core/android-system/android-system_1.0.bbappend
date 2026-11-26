FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# start-android-hals.sh, 75-ramoops-keys and the pstore saver are ours, on top
# of the recipe's PR.
PR:append = ".2"

SRC_URI += " \
    file://change-before-dependencies.conf \
    file://75-ramoops-keys \
    file://save-pstore.sh \
    file://pstore-save.service \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/android-system.service.d
    install -m 0644 ${UNPACKDIR}/change-before-dependencies.conf \
        ${D}${systemd_unitdir}/system/android-system.service.d

    # Key directory for the vendor's encrypted ramoops; see the hook.
    install -m 0755 ${UNPACKDIR}/75-ramoops-keys ${D}${localstatedir}/lib/lxc/android/pre-start.d/

    # Copy the last boot's crash records out of pstore.
    install -m 0755 ${UNPACKDIR}/save-pstore.sh ${D}${bindir}
    install -m 0644 ${UNPACKDIR}/pstore-save.service ${D}${systemd_system_unitdir}
}

FILES:${PN} += "${systemd_unitdir}/system/android-system.service.d"
FILES:${PN} += "${systemd_system_unitdir}/pstore-save.service"

# Enabled by hand, like the recipe's own units: in SYSTEMD_SERVICE the class
# would also add it to the package's restart-on-upgrade postinst.
pkg_postinst:${PN}:append() {
    if type systemctl >/dev/null 2>/dev/null; then
        OPTS=""
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS enable pstore-save.service
    fi
}
