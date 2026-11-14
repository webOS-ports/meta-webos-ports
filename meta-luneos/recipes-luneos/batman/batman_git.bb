SUMMARY = "Screen-on/off power tuning for Halium devices (libbatman with the LuneOS adapter)"
DESCRIPTION = "FuriLabs batman's tuning engine, split into libbatman, driven by \
luna://com.palm.display/control/status and the battery status in sysfs. While \
the display is off it switches CPU, GPU and devfreq governors to powersave and \
sends the Android power HAL the hints the framework sends at screen off \
(setInteractive(false) / low-power mode). It works while the phone is awake \
and does not suspend it; sleepd still does that."
HOMEPAGE = "https://github.com/FuriLabs/batman"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 libgbinder libnl luna-service2"

# libbatman branch of the fork, proposed upstream as FuriLabs/batman#2
SRC_URI = " \
    git://github.com/Herrie82/batman.git;branch=libbatman;protocol=https \
    file://batman-luna.service \
    file://config \
    file://com.palm.batman.role.json \
    file://com.palm.batman.perm.json \
    file://batman-luna.manifest.json \
"
SRCREV = "56f7f5bec539495bdef385c97f763f7117dc1ef5"
PV = "1.0+git"
PR = "r0"

inherit meson pkgconfig systemd

EXTRA_OEMESON = "-Ddaemon=false -Dluna_adapter=true -Dnfcd_plugin=disabled"

COMPATIBLE_MACHINE = "(halium)"

do_install:append() {
    # /etc/batman/config takes precedence over /var/lib/batman/config; ship
    # only the LuneOS one so there is a single place to look.
    rm -f ${D}/var/lib/batman/config
    install -d ${D}${sysconfdir}/batman
    install -m 0644 ${UNPACKDIR}/config ${D}${sysconfdir}/batman/config

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/batman-luna.service ${D}${systemd_system_unitdir}/

    install -d ${D}${datadir}/luna-service2/roles.d \
               ${D}${datadir}/luna-service2/client-permissions.d \
               ${D}${datadir}/luna-service2/manifests.d
    install -m 0644 ${UNPACKDIR}/com.palm.batman.role.json ${D}${datadir}/luna-service2/roles.d/
    install -m 0644 ${UNPACKDIR}/com.palm.batman.perm.json ${D}${datadir}/luna-service2/client-permissions.d/
    install -m 0644 ${UNPACKDIR}/batman-luna.manifest.json ${D}${datadir}/luna-service2/manifests.d/
}

PACKAGES =+ "libbatman"
FILES:libbatman = "${libdir}/libbatman.so.*"

FILES:${PN} += " \
    ${systemd_system_unitdir}/batman-luna.service \
    ${datadir}/luna-service2 \
    /var/lib/batman \
"
CONFFILES:${PN} = "${sysconfdir}/batman/config"

SYSTEMD_SERVICE:${PN} = "batman-luna.service"
SYSTEMD_AUTO_ENABLE = "enable"
