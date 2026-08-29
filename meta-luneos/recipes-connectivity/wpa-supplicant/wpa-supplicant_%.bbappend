# Copyright (c) 2017-2024 LG Electronics, Inc.

EXTENDPRAUTO:append = "webos8"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Replace the wpa_supplicant.service from wpa-supplicant source with our own version (for some unknown reason)
SYSTEMD_SERVICE:${PN}:remove = "wpa_supplicant.service"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "wpa-supplicant.service"
WEBOS_SYSTEMD_SCRIPT = "wpa-supplicant.sh"

do_configure:append() {
    # Enable DBus Introspection for easier debugging
    echo "CONFIG_CTRL_IFACE_DBUS_INTRO=y" >> ${B}/wpa_supplicant/.config

    # Enable debugging output to a file
    echo "CONFIG_DEBUG_FILE=y" >> ${B}/wpa_supplicant/.config

    # Redirect log to syslog instead of stdout
    echo "CONFIG_DEBUG_SYSLOG=y" >> ${B}/wpa_supplicant/.config
    echo "CONFIG_DEBUG_SYSLOG_FACILITY=LOG_DAEMON" >> ${B}/wpa_supplicant/.config

    # P2P config.
    #
    # UNPACKDIR, not WORKDIR: oe-core installs ${UNPACKDIR}/wpa_supplicant.conf-sane
    # as /etc/wpa_supplicant.conf, and SRC_URI files stopped landing in WORKDIR. So
    # these appends were creating a second file that nothing installs, and every one
    # of these settings has been silently missing from the image - the device gets
    # the stock oe-core config with no bss_max_count, no manufacturer, no P2P
    # identity at all.
    echo "bss_max_count=400" >> ${UNPACKDIR}/wpa_supplicant.conf-sane
    echo "max_num_sta=2" >> ${UNPACKDIR}/wpa_supplicant.conf-sane
    echo "manufacturer=LGE" >> ${UNPACKDIR}/wpa_supplicant.conf-sane
    echo "model_name=webOS" >> ${UNPACKDIR}/wpa_supplicant.conf-sane
    echo "model_number=webOS" >> ${UNPACKDIR}/wpa_supplicant.conf-sane
    echo "device_name=webOS" >> ${UNPACKDIR}/wpa_supplicant.conf-sane
    echo "serial_number=webOS" >> ${UNPACKDIR}/wpa_supplicant.conf-sane

    # Enable P2P (aka WiFi direct) support
    echo "CONFIG_P2P=y" >> ${B}/wpa_supplicant/.config
    echo "CONFIG_AP=y" >> ${B}/wpa_supplicant/.config
    echo "CONFIG_WPS=y" >> ${B}/wpa_supplicant/.config
    echo "CONFIG_WPS2=y" >> ${B}/wpa_supplicant/.config
    echo "CONFIG_WIFI_DISPLAY=y" >> ${B}/wpa_supplicant/.config
    echo "CONFIG_IEEE80211N=y" >> ${B}/wpa_supplicant/.config

    #Enable WEP Security
    echo "CONFIG_WEP=y" >> ${B}/wpa_supplicant/.config

}

do_install:append() {
    # Remove the wpa_supplicant.service from upstream, but be aware that we're still
    # keeping upstream wpa_supplicant-nl80211@.service wpa_supplicant@.service  wpa_supplicant-wired@.service
    rm -vf ${D}${systemd_unitdir}/system/wpa_supplicant.service

    # Replace the removed wpa_supplicant.service from upstream with our =wpa-supplicant.service
    sed -i 's/SystemdService=wpa_supplicant.service/SystemdService=wpa-supplicant.service/g' ${D}/${datadir}/dbus-1/system-services/*service
}

FILES:${PN} += "${systemd_unitdir}"

inherit useradd
USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = " \
    -u 1010 -d /var -s /usr/sbin/nologin -G netdev -U wifi ;\
    -u 1025 -d /var -s /usr/sbin/nologin -G netdev -U network ;\
"
