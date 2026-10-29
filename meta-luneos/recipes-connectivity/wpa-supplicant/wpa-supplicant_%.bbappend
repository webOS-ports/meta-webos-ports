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
    # P2P/WPS settings, added to the /etc/wpa_supplicant.conf oe-core installs
    # from wpa_supplicant.conf-sane. Done here rather than by appending to the
    # unpacked file in do_configure, which added another copy of every line
    # each time do_configure ran again. The WPS identity says LuneOS rather
    # than pretending to be an LGE webOS device.
    #
    # Also drop oe-core's catch-all "network={ key_mgmt=NONE }" block: with no
    # ssid it matches every open network, so wpa_supplicant would join any
    # rogue open access point if this file were ever loaded. connman runs
    # wpa_supplicant without -c and creates its interfaces over D-Bus without
    # a ConfigFile, so the file is dormant today, but it must not be a trap.
    conf=${D}${sysconfdir}/wpa_supplicant.conf
    sed -i '/^network={/,/^}/d' $conf
    if grep -q '^network=' $conf; then
        bbfatal "wpa_supplicant.conf: could not remove the open network block"
    fi
    printf '%s\n' bss_max_count=400 max_num_sta=2 manufacturer=LuneOS \
        model_name=LuneOS model_number=LuneOS device_name=LuneOS \
        serial_number=LuneOS >> $conf

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
