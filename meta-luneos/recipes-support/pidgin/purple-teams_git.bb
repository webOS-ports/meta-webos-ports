SUMMARY = "Microsoft Teams protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=30441c91ba0022ea994b5dae832bd063"

require purple-connectors.inc

# 0001-0002 are upstream PR #85 (personal/consumer accounts), 0003-0007 generic fixes meant for
# upstream; 0008 onwards adapt the plug-in to imlibpurpletransport and the webOS Messaging app.
# Upstream's teams_messages.c has CRLF line endings and so do the patches against it: keep the
# .patch files byte-exact.
SRC_URI += " \
    git://github.com/EionRobb/purple-teams.git;branch=master;protocol=https;name=upstream;destsuffix=purple-teams \
    file://0001-Add-personal-consumer-Teams-for-Life-account-support.patch \
    file://0002-Drop-history-re-delivered-across-logins-behind-TEAMS.patch \
    file://0003-Reconnect-Trouter-with-a-backoff-and-survive-empty-r.patch \
    file://0004-Fix-two-crashes-when-sending-or-echoing-an-image.patch \
    file://0005-Ignore-async-callbacks-that-arrive-after-the-account.patch \
    file://0006-Send-the-skype-token-to-regional-.asm.skype.com-medi.patch \
    file://0007-Send-image-attachments-as-inline-AMSImage-pictures.patch \
    file://0008-webOS-Send-and-receive-message-reactions-through-tra.patch \
    file://0009-webOS-Show-replies-as-quote-cards-and-send-replies-w.patch \
    file://0010-webOS-Show-received-media-inline-and-send-voice-note.patch \
    file://0011-webOS-Add-Teams-voice-and-video-calling-for-the-webO.patch \
    file://0012-webOS-Always-report-the-account-as-active-and-Availa.patch \
"
SRCREV_upstream = "f7eb519b9968ed7844ff59319bff5c1859c46851"

DEPENDS = "pidgin json-glib glib-2.0 zlib"

S = "${UNPACKDIR}/purple-teams"

# TEAMS_WEBOS turns on 0002's dedup of history that Teams re-delivers across logins; the webOS
# Messaging app would otherwise show it twice.
CFLAGS:append = " -DTEAMS_WEBOS"

# The default target builds both variants: libteams.so (commercial) and libteams-personal.so
# (consumer). webOS uses the consumer one; both are installed, as upstream does.
#
# NOT built here: the calling bridge from 0011 (teams_calling.c, teams_call_luna.c, h264_rtp.c,
# voipkit_none.c, teams_video_relay.cpp). The upstream Makefile does not list them; building them
# needs -DTEAMS_WEBOS_CALL, ${CONNECTORS_DIR}/im/_core/include for webos-ls2-compat.h,
# luna-service2, and the separate teams_media engine at runtime. Without TEAMS_WEBOS_CALL the
# calling hooks compile out and the plug-in shows upstream's "Incoming call" message instead.
EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
"

do_compile() {
    oe_runmake
}

# Installed by hand rather than with `make install`, so no Pidgin pixmaps are shipped -- see
# purple-connectors.inc.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${S}/libteams.so          ${D}${libdir}/purple-2/libteams.so
    install -m 0755 ${S}/libteams-personal.so ${D}${libdir}/purple-2/libteams-personal.so

    # Account template, so Teams can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/teams/account/com.palm.teams ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/teams/apps/com.palm.app.teams ${D}${webos_applicationsdir}/
}
