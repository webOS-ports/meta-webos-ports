SUMMARY = "Telegram protocol plug-in for libpurple (TDLib)"
DESCRIPTION = "libpurple prpl for Telegram built on TDLib, with webOS voice calling via \
libtgvoip."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=60e1c5c15dfde4c879ef9a5f0f20feab"

require purple-connectors.inc

# UnitedMarsupials/tdlib-purple is the maintained continuation of ars3niy/tdlib-purple, which has
# not moved since 2021 and is stuck on TDLib 1.7.9. The fork follows TDLib's releases (see
# tdlib_1.8.67.bb). 0001-0007 are generic fixes meant for it; 0008 onwards adapt the plug-in to
# imlibpurpletransport, the webOS Messaging app and the Phone app.
SRC_URI += " \
    git://github.com/UnitedMarsupials/tdlib-purple.git;branch=master;protocol=https;name=upstream;destsuffix=tdlib-purple \
    file://0001-Request-missing-private-chats-in-batches-at-login.patch \
    file://0002-Find-users-by-phone-number-and-by-username.patch \
    file://0003-Send-voice-recordings-and-videos-as-such-not-as-docu.patch \
    file://0004-Ask-for-login-input-through-a-chat-when-the-UI-has-n.patch \
    file://0005-Do-not-truncate-the-user-id-of-an-outgoing-call.patch \
    file://0006-Honour-Keep-inline-downloads-again-through-use_stora.patch \
    file://0007-Let-libtgvoip-be-given-without-pkg-config.patch \
    file://0008-webOS-register-as-prpl-telegram.patch \
    file://0009-webOS-cap-TDLib-s-log-verbosity-at-1.patch \
    file://0010-webOS-keep-the-TDLib-store-on-media-cryptofs-and-rem.patch \
    file://0011-webOS-hand-the-transport-what-it-needs-for-contacts-.patch \
    file://0012-webOS-expose-mute-and-archive-state-and-leave-archiv.patch \
    file://0013-webOS-reactions-sent-message-ids-and-delivery-read-t.patch \
    file://0014-webOS-replies-as-quote-cards-and-real-replies-when-s.patch \
    file://0015-webOS-place-and-take-calls-through-the-Phone-app-ove.patch \
"
SRCREV_upstream = "c83e3778bea79ec87b8ce447da3214c91cd58f4e"

PV = "0.9.3+git"
# The recipe used to be versioned 1.0+git while it built the monorepo copy; upstream's real version
# is lower, so bump the epoch to keep package feeds moving forward.
PE = "1"

# tdlib is the expensive part of a Telegram build: TDLib compiles a very large set of generated C++
# sources.
#
# libtgvoip is a choice, not a necessity: -DNoVoip=ON would give a working messaging-only plug-in.
# Calling is kept because it is a real feature on this platform. libopus/alsa-lib are here because
# libtgvoip is a STATIC archive whose opus, ALSA and libcrypto references are resolved at this link.
#
# fmt: the fork takes it from the system (pkg-config fmt) rather than bundling it.
DEPENDS = "pidgin glib-2.0 zlib openssl fmt tdlib libtgvoip libopus alsa-lib luna-service2"

S = "${UNPACKDIR}/tdlib-purple"

# S is only the plug-in checkout, so OE's automatic ${S} prefix-map misses the
# luneos-synergy-connectors header call-luna.cpp includes, whose path would survive into the
# debug info and trip buildpaths QA. Map the whole unpack directory.
DEBUG_PREFIX_MAP:append = " -ffile-prefix-map=${UNPACKDIR}=/usr/src/debug/${PN}/${PV}"

inherit cmake pkgconfig

# NoVoip=OFF keeps libtgvoip, which is what carries webOS calling.
#
# NoLottie=ON drops animated-sticker conversion, which would need a system rlottie; the webOS UI
# does not animate stickers anyway. Static stickers are decoded through the front end's
# GdkPixbuf, looked up at runtime.
#
# NoTranslations=ON, purely to avoid a gettext dependency: find_package(Gettext REQUIRED) fires at
# configure time otherwise. translate.h falls back to passing strings through, so this costs the
# plug-in's own localised strings and nothing else.
#
# libtgvoip installs no pkg-config file, so it is named here (0007 makes the fork accept that).
# tgvoip_LIBRARIES is a CMake list: the static libtgvoip.a leaves opus, ALSA and libcrypto
# undefined for this link to resolve. tgvoip_NO_DSP is deliberately NOT set -- see the note in
# libtgvoip_git.bb about keeping both sides of TGVOIP_NO_DSP in agreement.
#
# lunaservice_* both need overriding. The defaults are the legacy webOS ones (liblunaservice.so);
# LuneOS ships libluna-service2.so. The include list carries luneos-synergy-connectors'
# im/_core/include for webos-ls2-compat.h, which call-luna.cpp uses to map the legacy split-bus
# API onto luna-service2's single bus. The luna-service2 subdirectory is explicit because the
# header is included bare as <lunaservice.h> while the .pc offers only -I${includedir}.
#
# Video calling stays off: its only backend is legacy webOS's SkypeKit library, which LuneOS does
# not have, so SkypeKitFwRootfs is left unset and the bridge builds against the null backend.
#
# The semicolons must stay quoted: cmake_do_configure expands EXTRA_OECMAKE unquoted into a shell
# command, which would otherwise split the line at each one.
EXTRA_OECMAKE = " \
    -DNoVoip=OFF \
    -DNoLottie=ON \
    -DNoTranslations=ON \
    -Dtgvoip_INCLUDE_DIRS=${STAGING_INCDIR}/tgvoip \
    -Dtgvoip_LIBRARIES='tgvoip;opus;asound;crypto' \
    -Dlunaservice_INCLUDE_DIRS='${STAGING_INCDIR}/luna-service2;${CONNECTORS_DIR}/im/_core/include' \
    -Dlunaservice_LIBRARIES=luna-service2 \
"

# CMakeLists installs two things webOS has no use for, so they are removed after `make install`:
#
#   pixmaps  Pidgin protocol icons; there is no Pidgin GTK UI here (see purple-connectors.inc).
#   metainfo AppStream metadata for software centres. ${datadir}/metainfo is not in the default
#            FILES:${PN}, so it would otherwise be an installed-but-not-shipped QA error.
do_install:append() {
    rm -rf ${D}${datadir}/pixmaps
    rm -rf ${D}${datadir}/metainfo

    # Account template, so Telegram can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/telegram/account/com.palm.telegram ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/telegram/apps/com.palm.app.telegram ${D}${webos_applicationsdir}/

    # The telegram call service (call-luna.cpp, with NoVoip=OFF builds it): its bus name is listed, never activated --
    # imlibpurpleservice, which loads this plug-in, registers com.palm.telegram.call itself, so the
    # .service file is Type=static -- and the Phone app is granted imlibpurple-call.operation to
    # call it. The role letting imlibpurpleservice own the name, and the api-permissions/groups
    # behind that group, ship with imlibpurpleservice. webos_configure_manifest lists both files
    # in this package's manifest.
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_permissionsdir}
    install -m 0644 ${CONNECTORS_DIR}/im/telegram/files/sysbus/com.palm.telegram.call.service \
        ${D}${webos_sysbus_servicedir}/
    install -m 0644 ${CONNECTORS_DIR}/im/telegram/files/sysbus/com.palm.telegram.call.perm.json \
        ${D}${webos_sysbus_permissionsdir}/
}
