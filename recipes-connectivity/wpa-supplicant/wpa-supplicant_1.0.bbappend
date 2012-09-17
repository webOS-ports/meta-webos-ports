PRINC := "${@int(PRINC) + 1}"

# NOTE: same as do_install of oe-core/meta/recipes-connectivity/wpa-supplicant/wpa-supplicant-1.0.inc
# but one line changed. See below.
do_install () {
    install -d ${D}${sbindir}
    install -m 755 wpa_supplicant ${D}${sbindir}
    install -m 755 wpa_cli        ${D}${sbindir}

    install -d ${D}${bindir}
    install -m 755 wpa_passphrase ${D}${bindir}

    install -d ${D}${docdir}/wpa_supplicant
    install -m 644 README ${WORKDIR}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

    install -d ${D}${sysconfdir}
    install -m 600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf

    install -d ${D}${sysconfdir}/network/if-pre-up.d/
    install -d ${D}${sysconfdir}/network/if-post-down.d/
    install -d ${D}${sysconfdir}/network/if-down.d/
    install -m 755 ${WORKDIR}/wpa-supplicant.sh ${D}${sysconfdir}/network/if-pre-up.d/wpa-supplicant
    cd ${D}${sysconfdir}/network/ && \
    ln -sf ../if-pre-up.d/wpa-supplicant if-post-down.d/wpa-supplicant

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${S}/dbus/dbus-wpa_supplicant.conf ${D}/${sysconfdir}/dbus-1/system.d
    install -d ${D}/${datadir}/dbus-1/system-services
    install -m 644 ${S}/dbus/*.service ${D}/${datadir}/dbus-1/system-services

    # NOTE: This has to be removed as it breaks dbus activation. A patch pending for oe-core
    # upstream fixes this already
    # sed -i -e s:${base_sbindir}:${sbindir}:g ${D}/${datadir}/dbus-1/system-services/*.service

    install -d ${D}/etc/default/volatiles
    install -m 0644 ${WORKDIR}/99_wpa_supplicant ${D}/etc/default/volatiles
}

