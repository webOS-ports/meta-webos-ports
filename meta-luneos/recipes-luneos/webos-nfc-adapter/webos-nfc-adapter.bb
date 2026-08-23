SUMMARY = "LuneOS NFC service, bridges nfcd onto the luna-service2 bus"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# glib-2.0-native provides gdbus-codegen, which generates the nfcd D-Bus glue
DEPENDS = "luna-service2 pbnjson glib-2.0 glib-2.0-native"

# The service is useful without nfcd running (it reports NFC as unavailable),
# but there is no point shipping it on its own.
RRECOMMENDS:${PN} += "nfcd"

PV = "0.1.0-1+git"
# Pin this once webos-nfc-adapter has been pushed to github.com/webOS-ports
SRCREV = "${AUTOREV}"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "webos-nfc-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
