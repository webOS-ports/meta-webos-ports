SUMMARY = "Microsoft Teams protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=30441c91ba0022ea994b5dae832bd063"

require purple-connectors.inc

# 0001-0002 are upstream PR #85 (personal/consumer accounts), 0003-0008 generic fixes meant for
# upstream; the webOS: ones from 0009 adapt the plug-in to imlibpurpletransport and the webOS
# Messaging app.
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
    file://0008-Report-a-rejected-sign-in-code-as-an-authentication-.patch \
    file://0009-webOS-Send-and-receive-message-reactions-through-tra.patch \
    file://0010-webOS-Show-replies-as-quote-cards-and-send-replies-w.patch \
    file://0011-webOS-Show-received-media-inline-and-send-voice-note.patch \
    file://0012-webOS-Add-Teams-voice-calling-for-the-webOS-Phone-ap.patch \
    file://0013-webOS-Always-report-the-account-as-active-and-Availa.patch \
"
SRCREV_upstream = "f7eb519b9968ed7844ff59319bff5c1859c46851"

DEPENDS = "pidgin json-glib glib-2.0 zlib luna-service2"

S = "${UNPACKDIR}/purple-teams"

# TEAMS_WEBOS turns on 0002's dedup of history that Teams re-delivers across logins; the webOS
# Messaging app would otherwise show it twice.
CFLAGS:append = " -DTEAMS_WEBOS"

# libpurple only looks up purple_init_plugin (G_MODULE_EXPORT); keep the rest, including the
# bundled purple2compat http/socket code other plug-ins also carry, out of the dynamic symbol table.
CFLAGS:append = " -fvisibility=hidden"

# The default target builds both variants: libteams.so (commercial) and libteams-personal.so
# (consumer). webOS uses the consumer one; both are installed, as upstream does.
#
# TEAMS_WEBOS_CALL builds 0012's calling bridge (teams_calling.c, teams_call_luna.c) into
# libteams-personal.so only, so one plug-in registers com.palm.teams.call; it links luna-service2.
# Each call's audio runs in teams_media (teams-media_git.bb), which the plug-in spawns from
# TEAMS_MEDIA_BIN.
EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    TEAMS_WEBOS_CALL=1 \
    TEAMS_MEDIA_BIN=${libexecdir}/teams_media \
"

RDEPENDS:${PN} += "teams-media"

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

    # The teams call service (0012's teams_call_luna.c): its bus name is listed, never activated --
    # imlibpurpleservice, which loads this plug-in, registers com.palm.teams.call itself, so the
    # .service file is Type=static -- and the Phone app is granted imlibpurple-call.operation to
    # call it. The role letting imlibpurpleservice own the name, and the api-permissions/groups
    # behind that group, ship with imlibpurpleservice. webos_configure_manifest lists both files
    # in this package's manifest.
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_permissionsdir}
    install -m 0644 ${CONNECTORS_DIR}/im/teams/files/sysbus/com.palm.teams.call.service \
        ${D}${webos_sysbus_servicedir}/
    install -m 0644 ${CONNECTORS_DIR}/im/teams/files/sysbus/com.palm.teams.call.perm.json \
        ${D}${webos_sysbus_permissionsdir}/
}
