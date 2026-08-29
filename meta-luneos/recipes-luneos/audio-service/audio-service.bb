SUMMARY = "Service interface to control pulseaudio through a ls2 based API."
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 json-c glib-2.0 pulseaudio"

PV = "0.1.0-10+git"
SRCREV = "12d188e45faffd3b74ccdd5cc8ca188b12d71fad"

SERVICE_NAME = "org.webosports.service.audio"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
