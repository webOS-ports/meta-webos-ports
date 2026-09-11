# Copyright (c) 2017-2024 LG Electronics, Inc.

EXTENDPRAUTO:append = "webos8"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0001-Add-p2p-changes.patch \
"
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

    # P2P config
    echo "bss_max_count=400" >> ${WORKDIR}/wpa_supplicant.conf-sane
    echo "max_num_sta=2" >> ${WORKDIR}/wpa_supplicant.conf-sane
    echo "manufacturer=LGE" >> ${WORKDIR}/wpa_supplicant.conf-sane
    echo "model_name=webOS" >> ${WORKDIR}/wpa_supplicant.conf-sane
    echo "model_number=webOS" >> ${WORKDIR}/wpa_supplicant.conf-sane
    echo "device_name=webOS" >> ${WORKDIR}/wpa_supplicant.conf-sane
    echo "serial_number=webOS" >> ${WORKDIR}/wpa_supplicant.conf-sane

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

# bluejay (Pixel 6a): wpa_supplicant creating a p2p-dev-wlan0 P2P_DEVICE wdev
# panics bcmdhd4389.ko - a P2P_DEVICE wdev has no net_device, but
# wldev_iovar_setbuf() passes one to dhd_ioctl_entry_local() anyway, which
# dereferences it. See bluejay-wifi-p2p-kernel-panic in project memory for
# the full trace. CONFIG_P2P=y above is built LuneOS-wide for devices that
# actually want Wi-Fi Direct/Miracast, so this is scoped to sargo - bluejay's
# build stand-in, since it has no MACHINE of its own yet (Priority 1 in
# PATCH-INVENTORY-AND-UPSTREAM-PLAN.md) - rather than disabling P2P
# everywhere. Move this to a real bluejay override once that exists.
do_configure:append:sargo() {
    echo "p2p_disabled=1" >> ${WORKDIR}/wpa_supplicant.conf-sane

    # The upstream sample also ships a stray open network={} block
    # (key_mgmt=NONE => join any open AP on sight). Harmless while this
    # config file was never actually read at all (see the same memory
    # entry - WpaSupplicantConfigFile in connman-conf/main.conf is not a
    # key connmand understands), but the wpa-supplicant.env fix below makes
    # ConnMan adopt an interface pre-created from this exact file, so it
    # is live now.
    sed -i '/^network={$/a\	disabled=1' ${WORKDIR}/wpa_supplicant.conf-sane
}

do_install:append() {
    # Remove the wpa_supplicant.service from upstream, but be aware that we're still
    # keeping upstream wpa_supplicant-nl80211@.service wpa_supplicant@.service  wpa_supplicant-wired@.service
    rm -vf ${D}${systemd_unitdir}/system/wpa_supplicant.service

    # Replace the removed wpa_supplicant.service from upstream with our =wpa-supplicant.service
    sed -i 's/SystemdService=wpa_supplicant.service/SystemdService=wpa-supplicant.service/g' ${D}/${datadir}/dbus-1/system-services/*service
}

# bluejay: pre-create wlan0 from /etc/wpa_supplicant.conf (with p2p_disabled=1
# above) via wpa-supplicant.service's own $WPA_EXTRA_PARAM extension point,
# rather than /var/systemd/system/env/wpa-supplicant.env - that path is an
# ad-hoc local-override escape hatch (see pulseaudio.service's own comment on
# the equivalent file), not something a recipe should ship a default into. A
# real systemd drop-in is the correct build-time mechanism. ConnMan calls
# GetInterface before CreateInterface (gsupplicant/supplicant.c:
# interface_get_result's "create:" fallback) and adopts the interface this
# creates instead of making its own config-less one - see
# bluejay-wifi-p2p-kernel-panic for why a ConnMan-created interface can never
# carry a config file on its own.
do_install:append:sargo() {
    install -d ${D}${systemd_unitdir}/system/wpa-supplicant.service.d
    printf '[Service]\nEnvironment=WPA_EXTRA_PARAM=-i wlan0 -c /etc/wpa_supplicant.conf -D nl80211\n' \
        > ${D}${systemd_unitdir}/system/wpa-supplicant.service.d/bluejay-p2p-fix.conf
}

FILES:${PN} += "${systemd_unitdir}"

inherit useradd
USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = " \
    -u 1010 -d /var -s /usr/sbin/nologin -G netdev -U wifi ;\
    -u 1025 -d /var -s /usr/sbin/nologin -G netdev -U network ;\
"

# http://gecko.lge.com:8000/Errors/Details/819468
# caused by 0001-Add-p2p-changes.patch
# notify.c:769:25: error: implicit declaration of function 'wpas_dbus_signal_p2p_peer_joined_with_ip'; did you mean 'wpas_dbus_signal_p2p_peer_joined'? [-Wimplicit-function-declaration]
CFLAGS += "-Wno-error=implicit-function-declaration"
