SUMMARY = "Discord protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

require purple-synergy.inc

DEPENDS = "pidgin json-glib glib-2.0 zlib qrencode openssl"

S = "${WORKDIR}/git/messaging/discord/plugin/purple-discord"

# QR remote-auth is NOT optional in this fork: discord_rsa.c includes <qrencode.h>
# unconditionally, so the plug-in does not compile without it. qrencode comes from meta-oe.
#
# USE_QRCODE_AUTH=0 looks backwards but is deliberate, and matches build-discord.sh. It only
# switches off the upstream Makefile's QR auto-detect block, which pulls the RSA backend from
# NSS via pkg-config. This fork uses the OpenSSL backend instead (USE_OPENSSL_CRYPTO in
# discord_rsa.c), so the feature is enabled by hand through CPPFLAGS -- -DUSE_QRCODE_AUTH
# without the NSS dependency the Makefile would otherwise impose.

# CPPFLAGS keeps OE's own value and appends -- setting it outright on the make command line would
# drop the sysroot flags bitbake puts there, since command-line variables beat the environment.
EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    USE_QRCODE_AUTH=0 \
    CPPFLAGS='${CPPFLAGS} -DUSE_QRCODE_AUTH -DUSE_OPENSSL_CRYPTO' \
"

do_compile() {
    oe_runmake LDFLAGS="${LDFLAGS} $(${STAGING_BINDIR_NATIVE}/pkg-config --libs libqrencode openssl)"
}

do_install() {
    oe_runmake DESTDIR="${D}" install
}
