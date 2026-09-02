SUMMARY = "LuneOS NFC service, bridges nfcd onto the luna-service2 bus"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# glib-2.0-native provides gdbus-codegen, which generates the nfcd D-Bus glue.
# openssl is BAC/PACE's crypto (3DES/AES/SHA/EC). openjpeg decodes DG2's
# JPEG2000 facial photo - this platform's Qt has no JPEG2000 image plugin.
DEPENDS = "luna-service2 libpbnjson glib-2.0 glib-2.0-native openssl openjpeg"

# The service is useful without nfcd running (it reports NFC as unavailable),
# but there is no point shipping it on its own.
RRECOMMENDS:${PN} += "nfcd"

PV = "0.1.0-1+git"
SRCREV = "5b1dfbbaf5bf7e1cd9c1ca304f39c9e8b64ee044"

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

# gdbus-codegen writes an absolute #include for the header it generates, so the
# generated source shipped in -src carries a build path. webos-telephonyd has
# the same construct and the same QA hit:
# ERROR: webos-nfc-adapter-0.1.0-1+git-r0 do_package_qa: QA Issue: File /usr/src/debug/webos-nfc-adapter/0.1.0-1+git/Configured/src/nfcd-interface.c in package webos-nfc-adapter-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
