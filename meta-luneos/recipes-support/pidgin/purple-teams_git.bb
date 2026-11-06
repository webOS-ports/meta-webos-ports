SUMMARY = "Microsoft Teams protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=30441c91ba0022ea994b5dae832bd063"

require purple-connectors.inc

# The source is the fork branch behind upstream PR #99 (https://github.com/EionRobb/purple-teams/pull/99):
# master plus #85 (personal/consumer accounts), the fix PRs #92-#98 and the opt-in calling
# framework (TEAMS_CALLING). Go back to EionRobb's master once those are merged.
# 0001 is the history dedup meant for upstream; 0002-0006 adapt the plug-in to
# imlibpurpletransport, the webOS Messaging app and, through #99's teams_calling_set_*_cb hooks,
# the webOS Phone app.
# Upstream's .c files and Makefile have CRLF line endings and so do the patches against them: keep
# the .patch files byte-exact.
SRC_URI += " \
    git://github.com/Herrie82/purple-teams.git;branch=herrie/teams-calling;protocol=https;name=upstream;destsuffix=purple-teams \
    file://0001-Drop-history-re-delivered-across-logins-behind-TEAMS.patch \
    file://0002-webOS-Send-and-receive-message-reactions-through-tra.patch \
    file://0003-webOS-Show-replies-as-quote-cards-and-send-replies-w.patch \
    file://0004-webOS-Show-received-media-inline-and-send-voice-note.patch \
    file://0005-webOS-Always-report-the-account-as-active-and-Availa.patch \
    file://0006-webOS-Ring-and-drive-Teams-calls-from-the-webOS-Phon.patch \
"
SRCREV_upstream = "91a5dc6f960ee623ef722a6e2c00b4e1e010f2ce"

DEPENDS = "pidgin json-glib glib-2.0 zlib luna-service2"

S = "${UNPACKDIR}/purple-teams"

# TEAMS_WEBOS turns on 0001's dedup of history that Teams re-delivers across logins; the webOS
# Messaging app would otherwise show it twice.
CFLAGS:append = " -DTEAMS_WEBOS"

# libpurple only looks up purple_init_plugin (G_MODULE_EXPORT); keep the rest, including the
# bundled purple2compat http/socket code other plug-ins also carry, out of the dynamic symbol table.
CFLAGS:append = " -fvisibility=hidden"

# The default target builds both variants: libteams.so (commercial) and libteams-personal.so
# (consumer). webOS uses the consumer one; both are installed, as upstream does.
#
# TEAMS_CALLING builds #99's calling framework (teams_calling.c) and, with 0006, the webOS front
# end on its hooks (teams_call_luna.c, teams_call_video.c), into libteams-personal.so only, so one
# plug-in registers com.palm.teams.call; it links luna-service2. Each call's audio and video run in
# teams_media (teams-media_git.bb), which the plug-in spawns from TEAMS_MEDIA_BIN.
EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    TEAMS_CALLING=1 \
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

    # The teams call service (0006's teams_call_luna.c): its bus name is listed, never activated --
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
