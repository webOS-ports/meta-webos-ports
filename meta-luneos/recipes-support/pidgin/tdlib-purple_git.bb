SUMMARY = "Telegram protocol plug-in for libpurple (TDLib)"
DESCRIPTION = "libpurple prpl for Telegram built on TDLib, with webOS voice and video calling \
via tgcalls-lite."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=60e1c5c15dfde4c879ef9a5f0f20feab"

require purple-connectors.inc

# UnitedMarsupials/tdlib-purple is the maintained continuation of ars3niy/tdlib-purple, which has
# not moved since 2021 and is stuck on TDLib 1.7.9. The fork follows TDLib's releases (see
# tdlib_1.8.67.bb; it needs at least 1.8.44). The patches, one per feature, adapt the plug-in to
# imlibpurpletransport, the webOS Messaging app and the Phone app.
SRC_URI += " \
    git://github.com/UnitedMarsupials/tdlib-purple.git;branch=master;protocol=https;name=upstream;destsuffix=tdlib-purple \
    file://0001-webOS-identity-store-and-what-the-transport-needs-pr.patch \
    file://0002-webOS-reactions-sent-message-ids-delivery-and-read-t.patch \
    file://0003-webOS-Telegram-calls-through-the-Phone-app-with-tgca.patch \
    file://0004-webOS-carry-Telegram-calls-with-tgcalls-lite-alone-w.patch \
    file://0005-webOS-do-what-the-Messaging-app-asks-edit-delete-gro.patch \
    file://0006-webOS-edit-and-delete-a-received-message-where-it-is.patch \
    file://0007-webOS-rename-a-group-when-the-Messaging-app-asks.patch \
    file://0008-webOS-give-the-app-the-id-of-what-we-sent-to-a-group.patch \
    file://0009-webOS-show-a-poll-s-votes-in-the-Messaging-app-and-a.patch \
    file://0010-webOS-hand-the-Messaging-app-received-files-file-URI.patch \
    file://0011-webOS-name-who-a-forward-is-from-whatever-it-came-fr.patch \
"
SRCREV_upstream = "e460589266fa4132c85f270efb4f2c8d332dbcb5"

PV = "0.9.5+git"
# The recipe used to be versioned 1.0+git while it built the monorepo copy; upstream's real version
# is lower, so bump the epoch to keep package feeds moving forward.
PE = "5"

# tdlib is the expensive part of a Telegram build: TDLib compiles a very large set of generated C++
# sources.
#
# Calling is a choice, not a necessity: -DNoVoip=ON would give a working messaging-only plug-in.
# It is kept because it is a real feature on this platform.
#
# fmt: the fork takes it from the system (pkg-config fmt) rather than bundling it.
#
# tgcalls-lite (0003) carries the calls, in the tgcalls V2 protocol current Telegram clients speak:
# a static library, whose libnice, libsrtp2, json-glib, Opus and PulseAudio references are resolved
# at this link. libtgvoip is not used (0004, NoTgvoip below).
DEPENDS = "pidgin glib-2.0 zlib openssl fmt tdlib libopus luna-service2 \
           tgcalls-lite libnice libsrtp json-glib pulseaudio"

S = "${UNPACKDIR}/tdlib-purple"

# Map the whole unpack directory, not only ${S}, so nothing compiled from outside the plug-in
# checkout leaves a TMPDIR path in the debug info (buildpaths QA).
DEBUG_PREFIX_MAP:append = " -ffile-prefix-map=${UNPACKDIR}=/usr/src/debug/${PN}/${PV}"

inherit cmake pkgconfig

# NoVoip=OFF keeps calling; NoTgvoip=ON (0004) carries every call with tgcalls-lite and leaves
# libtgvoip out. libtgvoip only carried a call when the peer agreed on no tgcalls version, which no
# current official client does (they all register tgcalls V2, 7.0.0); such a call is now refused.
#
# NoLottie=ON drops animated-sticker conversion, which would need a system rlottie; the webOS UI
# does not animate stickers anyway. Static stickers are decoded through the front end's
# GdkPixbuf, looked up at runtime.
#
# NoPidgin=ON: there is no Pidgin GTK UI here, and what only Pidgin's conversation window can do
# (message menus, pasting, filling a tab from the log) needs its headers, which are not built.
#
# NoTranslations=ON, purely to avoid a gettext dependency: find_package(Gettext REQUIRED) fires at
# configure time otherwise. translate.h falls back to passing strings through, so this costs the
# plug-in's own localised strings and nothing else.
#
# lunaservice_* both need overriding. The defaults are the legacy webOS ones (liblunaservice.so);
# LuneOS ships libluna-service2.so.
#
# tgcalls-lite's video (0003) has luneos-rtc-engine
# (packagegroup-luneos-extended) decode and, where there is a camera and an encoder, encode the
# H.264; without the engine on the device a video call stays voice-only.
EXTRA_OECMAKE = " \
    -DNoVoip=OFF \
    -DNoTgvoip=ON \
    -DNoLottie=ON \
    -DNoTranslations=ON \
    -DNoPidgin=ON \
    -Dlunaservice_INCLUDE_DIRS=${STAGING_INCDIR}/luna-service2 \
    -Dlunaservice_LIBRARIES=luna-service2 \
    -Dtgcalls_lite_INCLUDE_DIRS=${STAGING_INCDIR}/tgcalls-lite \
    -Dtgcalls_lite_LIBRARIES=${STAGING_LIBDIR}/libtgcalls-lite.a \
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
