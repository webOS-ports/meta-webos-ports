SUMMARY = "Google Chat protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01f1269ee11920646864ee3a2f4c5ed3"

require purple-synergy.inc

# protobuf-c comes from meta-openembedded. The -native side supplies protoc-c, which the Makefile
# runs to regenerate the .pb-c.c sources.
DEPENDS = "pidgin json-glib glib-2.0 zlib protobuf-c protobuf-c-native"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/googlechat/plugin/purple-googlechat"

# INCLUDES is hardcoded to -I/usr/include/protobuf-c in the Makefile's Linux
# branch, which is a host path and trips poison-system-directories:
#   cc1: error: include location "/usr/include/protobuf-c" is unsafe for
#   cross-compilation [-Werror=poison-system-directories]
# It is redundant anyway - the real protobuf-c flags come from pkg-config via
# PROTOBUF_OPTS - so blank it out. A command line assignment beats the
# Makefile's own.
EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    PROTOC_C='${STAGING_BINDIR_NATIVE}/protoc-c' \
    INCLUDES='' \
"

do_compile() {
    oe_runmake
}

# Installed by hand rather than with `make install`, so no Pidgin pixmaps are shipped. That
# target also runs install-icons, which drops PNGs into ${datadir}/pixmaps/pidgin/protocols --
# a directory only Pidgin's GTK UI ever reads. There is no Pidgin here: libpurple is loaded
# headless by imlibpurpletransport, and webOS takes its account icons from the account template.
# Same reasoning as purple-discord and purple-teams, which install this way too.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${S}/libgooglechat.so ${D}${libdir}/purple-2/libgooglechat.so

    # Account template, so Google Chat can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/googlechat/account/com.palm.googlechat ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/googlechat/apps/com.palm.app.googlechat ${D}${webos_applicationsdir}/
}
