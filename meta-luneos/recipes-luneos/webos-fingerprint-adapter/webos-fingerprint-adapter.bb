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
SRCREV = "7273fce557f219a444de694495d5a1bc1c2503be"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

# Points at the biomd port branch until it lands. The client, the systemd
# unit and the docs all moved from droidian-fpd to io.FuriOS.Biomd there;
# master still has the fpd client, which asks for a bus name nothing
# provides any more. Switch back to master once the PR is merged.
WEBOS_GIT_PARAM_BRANCH = "herrie/port-to-biomd"
WEBOS_REPO_NAME = "webos-fingerprint-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# gdbus-codegen writes an absolute #include for the header it generates, same
# QA hit as webos-nfc-adapter/webos-telephonyd.
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
