SUMMARY = "Microsoft Teams protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=30441c91ba0022ea994b5dae832bd063"

require purple-synergy.inc

DEPENDS = "pidgin json-glib glib-2.0 zlib"

S = "${WORKDIR}/git/messaging/teams/plugin/purple-teams"

# The default target builds both variants: libteams.so (commercial, Skype for Business) and
# libteams-personal.so (consumer). webOS uses the consumer one; both are installed, as upstream
# does, since the extra object costs little and keeps the recipe close to the Makefile.
#
# NOT built here: the webOS calling bridge. teams_call_luna.c (com.palm.teams.call),
# teams_calling.c and h264_rtp.c are absent from the upstream Makefile's file list --
# build-teams.sh compiles them separately -- so this is the messaging plug-in without calling.
# Adding it needs those files taught to the Makefile plus messaging/common/webos-ls2-compat.h
# (which maps the legacy split-bus LS2 API onto the single-bus one 3.21.2 provides) and
# DEPENDS += "luna-service2". Video needs more still: it binds libpalmgstskype.so, a
# gstreamer-0.10 plug-in present only in legacy webOS firmware. voipkit_none.c is the null
# backend that lets it link without one.

EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
"

do_compile() {
    oe_runmake
}

# Installed by hand rather than with `make install`, so no Pidgin pixmaps are shipped. That
# target also runs install-icons, which drops PNGs into ${datadir}/pixmaps/pidgin/protocols --
# a directory only Pidgin's GTK UI ever reads. There is no Pidgin here: libpurple is loaded
# headless by imlibpurpletransport, and webOS takes its account icons from the account template.
# Same reasoning as purple-discord and purple-googlechat, which install this way too.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${S}/libteams.so          ${D}${libdir}/purple-2/libteams.so
    install -m 0755 ${S}/libteams-personal.so ${D}${libdir}/purple-2/libteams-personal.so

    # Account template, so Teams can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${WORKDIR}/git/messaging/teams/account/com.palm.teams ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${WORKDIR}/git/messaging/teams/apps/com.palm.app.teams ${D}${webos_applicationsdir}/
}
