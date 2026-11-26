SUMMARY = "LuneOS fingerprint service, bridges biomd onto the luna-service2 bus"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# glib-2.0-native provides gdbus-codegen, which generates the biomd D-Bus glue
DEPENDS = "luna-service2 libpbnjson glib-2.0 glib-2.0-native"

# The service is useful without biomd running (it reports the sensor as
# unavailable and re-attaches when biomd appears), but there is no point
# shipping it on its own.
RRECOMMENDS:${PN} += "biomd"

PV = "0.1.0-1+git"
PR = "r2"
SRCREV = "ac6829fbba1c2a9e49744447593a3ea0df4bb4bc"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_REPO_NAME = "webos-fingerprint-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://luneos-has-fingerprint.conf \
"

# Skip the service on a device with no fingerprint reader; see the drop-in.
do_install:append() {
    install -d ${D}${systemd_system_unitdir}/${PN}.service.d
    install -m 0644 ${UNPACKDIR}/luneos-has-fingerprint.conf \
        ${D}${systemd_system_unitdir}/${PN}.service.d/50-luneos-has-fingerprint.conf
}

FILES:${PN} += "${systemd_system_unitdir}/${PN}.service.d"
