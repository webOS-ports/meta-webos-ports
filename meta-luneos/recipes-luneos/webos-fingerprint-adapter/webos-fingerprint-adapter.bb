SUMMARY = "LuneOS fingerprint service, bridges droidian-fpd onto the luna-service2 bus"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# glib-2.0-native provides gdbus-codegen, which generates the fpd D-Bus glue
DEPENDS = "luna-service2 libpbnjson glib-2.0 glib-2.0-native"

# The service is useful without droidian-fpd running (it reports the sensor as
# unavailable), but there is no point shipping it on its own.
RRECOMMENDS:${PN} += "droidian-fpd"

PV = "0.1.0-1+git"
SRCREV = "878d42a485c74aab46fe49121b9464b530809fbf"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "webos-fingerprint-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# gdbus-codegen writes an absolute #include for the header it generates, same
# QA hit as webos-nfc-adapter/webos-telephonyd.
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
