SUMMARY = "Discord protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

require purple-synergy.inc

DEPENDS = "pidgin json-glib glib-2.0 zlib qrencode openssl"

S = "${WORKDIR}/git/messaging/discord/plugin/purple-discord"

# QR remote-auth is NOT optional in this fork: discord_rsa.c includes <qrencode.h>
# unconditionally, so the plug-in does not compile without it. qrencode comes from meta-oe and
# ships libqrencode.pc, the module name the Makefile probes for.
#
# USE_QRCODE_AUTH=0 reads backwards but is deliberate, and matches build-discord.sh. It only
# switches off the upstream Makefile's QR auto-detect block, which would pull the RSA backend in
# from NSS via pkg-config. This fork uses the OpenSSL backend instead (USE_OPENSSL_CRYPTO in
# discord_rsa.c), so the feature is enabled through CFLAGS below and NSS is never involved.
#
# The defines go through CFLAGS rather than a CPPFLAGS= assignment in EXTRA_OEMAKE: quoting a
# multi-word value there does not survive into make reliably -- the first attempt reached make as
# an empty CPPFLAGS= plus two stray arguments. The Makefile uses both $(CFLAGS) and $(CPPFLAGS)
# on its link line, and declares CFLAGS with ?= so bitbake's exported value wins anyway.
CFLAGS:append = " -DUSE_QRCODE_AUTH -DUSE_OPENSSL_CRYPTO"

EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    USE_QRCODE_AUTH=0 \
"

do_compile() {
    oe_runmake LDFLAGS="${LDFLAGS} $(${STAGING_BINDIR_NATIVE}/pkg-config --libs libqrencode openssl)"
}

# Installed by hand rather than with `make install`, so no Pidgin pixmaps are shipped -- same as
# purple-teams and purple-googlechat. See purple-synergy.inc for why none of them ship icons.
#
# Here there is a second reason: `make install` cannot run at all. It depends on discord16.png
# and friends, which are in upstream's own .gitignore -- the Makefile renders them from
# discord-alt-logo.svg with ImageMagick at install time, so the target fails with
# "magick: No such file or directory" and would otherwise need imagemagick-native purely to
# generate artwork that nothing on this platform reads.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${S}/libdiscord.so ${D}${libdir}/purple-2/libdiscord.so
}
