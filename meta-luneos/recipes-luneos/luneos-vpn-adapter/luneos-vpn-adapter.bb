SUMMARY = "LuneOS VPN service, bridges connman-vpnd onto the luna-service2 bus"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# gio-2.0 is how the service talks to connman-vpnd (net.connman.vpn), and
# glib-2.0-native provides gdbus-codegen, which generates the glue for it.
DEPENDS = "luna-service2 libpbnjson glib-2.0 glib-2.0-native"

# connman-vpn is the whole backend - profiles, credentials and the tunnel all
# live there. The service starts and reports VPN as unavailable without it, but
# there is no point shipping one without the other.
RDEPENDS:${PN} += "connman-vpn"

# Which of these are installed is what getAgents reports as the available VPN
# technologies, so recommend the full set rather than letting the image decide
# silently. They are RRECOMMENDS on connman as well; listing them here means
# removing this service does not quietly take the plugins with it.
RRECOMMENDS:${PN} += " \
    connman-plugin-vpn-wireguard \
    connman-plugin-vpn-openvpn \
    connman-plugin-vpn-openconnect \
    connman-plugin-vpn-vpnc \
    connman-plugin-vpn-l2tp \
"

PV = "0.1.0-1+git"
# PLACEHOLDER - replace with the real commit once the repo is pushed. Until then
# this recipe only builds via externalsrc; see VPN/luneos-vpn-adapter/README.md.
SRCREV = "0000000000000000000000000000000000000000"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "luneos-vpn-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# The per-provider vpnFormFields descriptors are data, not compiled-in tables,
# so a wrong field can be fixed without a rebuild.
FILES:${PN} += "${datadir}/luneos-vpn-adapter"

# gdbus-codegen writes an absolute #include for the header it generates, so the
# generated source shipped in -src carries a build path. Same construct and the
# same QA hit as webos-nfc-adapter and webos-telephonyd.
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
