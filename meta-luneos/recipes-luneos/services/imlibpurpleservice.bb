SUMMARY = "Instant Messaging service"
SECTION = "webos/services"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=263f341c01743dbd6b06ae75369dbeed \
    file://COPYRIGHT;md5=2ce083d13f0f21e5207b4115c8926450 \
"

# libopus/libogg are for src/OpusEncoder.cpp, the WAV -> Ogg/Opus voice-note encoder. Note that
# CMakeLists.txt neither looks for them nor links them -- only the device build compensates, with a
# hand-written "-lopus -logg" in build.sh -- so the flags have to come from EXTRA_OECMAKE below.
# Worth fixing upstream with a pkg_check_modules(OPUS REQUIRED opus ogg) instead.
DEPENDS = "glib-2.0 db8 pidgin luna-service2 tidy-html5 libopus libogg"

PV = "3.0.5+git"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

# TEMPORARY: built from the fork rather than webOS-ports/imlibpurpleservice, to test the QR
# remote-auth transport. Revert both overrides once herrie/synergy-revival is merged into the
# webOS-ports repo, leaving only a SRCREV bump.
#
# The account validators call com.palm.imlibpurple/startQRLogin, getAuthChallenge and
# submitAuthInput. None of that exists at 134cf24 (107 commits back): the service does not
# implement the methods, com.palm.imlibpurple.api.json does not declare them, and the role file's
# inbound list has no entry for the accounts app -- which is the identity these calls actually
# carry, because the validator HTML runs inside com.palm.app.accounts' web context rather than its
# own. So WhatsApp sign-in fails with
#     "Not permitted to send to com.palm.imlibpurple."
# and ls-hubd logs LSHUB_NO_INB_PERMS for com.palm.app.accounts-<pid>. This revision fixes all
# three: the methods exist, the ACG declares them, and the role file admits com.palm.app.accounts
# plus the per-protocol validator apps.
WEBOS_PORTS_GIT_REPO = "git://github.com/Herrie82"
WEBOS_GIT_PARAM_BRANCH = "herrie/synergy-revival"
SRCREV = "2ee1c411e00ce880ad454cc8192924a2918494a4"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# See the DEPENDS note: OpusEncoder.cpp includes <opus/opus.h> and <ogg/ogg.h>, but CMakeLists.txt
# adds neither to target_link_libraries, so the link fails on the encoder symbols without this.
#
# CMAKE_CXX_STANDARD_LIBRARIES rather than CMAKE_EXE_LINKER_FLAGS: the latter is emitted ahead of
# the object files, where --as-needed drops both libraries again because nothing has referenced
# them yet. Standard libraries go last, after the objects that need them.
EXTRA_OECMAKE += "-DCMAKE_CXX_STANDARD_LIBRARIES='-lopus -logg'"
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

do_install:append() {
    cp -R --no-dereference --preserve=mode,links -v ${S}/files/etc ${D}
}

CXXFLAGS += "-fpermissive"
