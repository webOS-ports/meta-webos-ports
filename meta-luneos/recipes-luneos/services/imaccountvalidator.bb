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
    purple-combined \
    tdlib-purple \
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

# All six webos-synergy-revival plug-ins are enabled. purple-combined provides TWO prpls from one
# shared object -- prpl-hehoe-whatsmeow (WhatsApp) and prpl-gometa (Facebook Messenger) -- because
# they share a Go runtime.
#
# Three of them are heavier or more fragile than the rest, so if an image build breaks these are
# the ones to drop first:
#
#   tdlib-purple     pulls in tdlib, a very large C++ build and the most likely thing to OOM a
#                    constrained builder (its recipe caps PARALLEL_MAKE at 2 for that reason).
#   purple-combined  needs network access in do_compile to fetch Go modules through GOPROXY,
#                    the same way influxdb and etcd do in this layer set.
#
# purple-presage (Signal) is NOT listed: it cannot build on scarthgap at all. Its Cargo.lock is
# lockfile version 4 (cargo 1.78+) and its graph needs rust 1.86 via icu_* 2.2.0, against the
# 1.75 this layer set provides. The recipe is complete and waiting for a newer toolchain -- see
# purple-presage_git.bb. Signal also does not work on webOS at runtime regardless.

CXXFLAGS += "-fpermissive"
