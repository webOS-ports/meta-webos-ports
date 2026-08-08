SUMMARY = "Instant Messaging service"
SECTION = "webos/services"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=263f341c01743dbd6b06ae75369dbeed \
    file://COPYRIGHT;md5=2ce083d13f0f21e5207b4115c8926450 \
"

DEPENDS = "glib-2.0 db8 pidgin luna-service2 tidy-html5"

PV = "3.0.5+git"
SRCREV = "134cf24abf82d019cc645599c39f360c3875a6b7"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

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

do_install:append() {
    cp -R --no-dereference --preserve=mode,links -v ${S}/files/etc ${D}
}

CXXFLAGS += "-fpermissive"
