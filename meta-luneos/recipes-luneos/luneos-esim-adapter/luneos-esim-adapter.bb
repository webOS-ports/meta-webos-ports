SUMMARY = "LuneOS eSIM service, bridges ofono's EuiccManager and lpac onto the luna-service2 bus"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# gio-2.0 is how the service talks to ofono: org.ofono.SimManager for the card
# and its slots, and the presence of org.ofono.EuiccManager as the test for
# whether this device can do eSIM at all.
DEPENDS = "luna-service2 libpbnjson glib-2.0"

# SGP.22 itself is lpac's job - the service shells out to it. Without lpac the
# card can still be inspected, but no profile can be listed or downloaded.
RDEPENDS:${PN} += "lpac"

# ofono needs the EuiccManager interface, i.e. the patches carried in
# recipes-connectivity/ofono. It is a runtime relationship, not a build one.
RRECOMMENDS:${PN} += "ofono"

PV = "0.1.0-1+git"
SRCREV = "f0fc6bde04aaf3a28153cba8b2743b54d0df765e"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "luneos-esim-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
