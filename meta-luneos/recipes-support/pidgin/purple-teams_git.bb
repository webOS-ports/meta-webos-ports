SUMMARY = "Microsoft Teams protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=30441c91ba0022ea994b5dae832bd063"

require purple-synergy.inc

DEPENDS = "pidgin json-glib glib-2.0 zlib"

S = "${WORKDIR}/git/messaging/teams/plugin/purple-teams"

# The Makefile's default target builds both variants -- libteams.so (commercial, Skype for
# Business) and libteams-personal.so (consumer) -- and its install target ships both plus the
# icon sets under icons/{16,22,48}/. webOS only uses the consumer one, but installing both is
# what upstream does and costs little.
#
# NOT built here: the webOS calling bridge. teams_call_luna.c (com.palm.teams.call),
# teams_calling.c and h264_rtp.c are absent from the upstream Makefile's file list -- build-teams.sh
# in the monorepo compiles them separately -- so this recipe produces the messaging plugin without
# calling. Adding it needs those files taught to the Makefile along with
# messaging/common/webos-ls2-compat.h (which maps the legacy split-bus LS2 API onto the single-bus
# one 3.21.2 provides), and DEPENDS += "luna-service2". The VIDEO half needs more than that: it
# binds libpalmgstskype.so, a gstreamer-0.10 plugin that exists only in legacy webOS firmware.
# voipkit_none.c is the null backend that lets it link without one; a real LuneOS backend has
# still to be written.

do_compile() {
    oe_runmake CC="${CC}" PKG_CONFIG="${STAGING_BINDIR_NATIVE}/pkg-config"
}

do_install() {
    oe_runmake CC="${CC}" PKG_CONFIG="${STAGING_BINDIR_NATIVE}/pkg-config" \
               DESTDIR="${D}" install
}
