SUMMARY = "Instant Messaging Account Validator service"
SECTION = "webos/services"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYRIGHT;md5=b928fe818deef43f0b52d28bd42f56f2 \
"

DEPENDS = "glib-2.0 db8 pidgin luna-service2"

PV = "3.0.5+git"
SRCREV = "ac3240070bc1ef9a69e597c91640b41871741c33"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_filesystem_paths

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

RRECOMMENDS:${PN} += " \
    pidgin-sipe \
    purple-matrix \
    purple-discord \
    purple-googlechat \
    purple-teams \
    funyahoo-plusplus \
    icyque \
    libpurple-plugin-autoaccept \
    libpurple-plugin-buddynote \
    libpurple-plugin-idle \
    libpurple-plugin-joinpart \
    libpurple-plugin-log_reader \
    libpurple-plugin-newline \
    libpurple-plugin-offlinemsg \
    libpurple-plugin-psychic \
    libpurple-plugin-ssl \
    libpurple-plugin-ssl-gnutls \
    libpurple-plugin-statenotify \
    libpurple-protocol-bonjour \
    libpurple-protocol-gg \
    libpurple-protocol-irc \
    libpurple-protocol-novell \
    libpurple-protocol-simple \
    libpurple-protocol-xmpp \
    libpurple-protocol-zephyr \
"

# The remaining webos-synergy-revival plug-ins are deliberately NOT listed above. RRECOMMENDS
# still requires the recipe to build, so a broken one fails the whole image, and each of these
# has a known blocker:
#
#   purple-combined  needs network access in do_compile to fetch Go modules through GOPROXY.
#                    Vendoring instead would add 172M to the source repo.
#   purple-presage   its 24 non-crates.io git dependencies are wired up but have never been
#                    built, and boring-sys compiles BoringSSL, which is slow.
#   tdlib-purple     pulls in tdlib, a very large C++ build and the most likely thing to OOM a
#                    constrained builder.
#
# Add them to the list above one at a time, once each has been through a real build:
#     purple-combined purple-presage tdlib-purple

CXXFLAGS += "-fpermissive"
