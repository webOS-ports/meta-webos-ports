SUMMARY = "Discord protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

require purple-synergy.inc

DEPENDS = "pidgin json-glib glib-2.0 zlib"

S = "${WORKDIR}/git/messaging/discord/plugin/purple-discord"

# QR-code login needs nss and libqrencode. The Makefile enables it only when both are present
# (USE_QRCODE_AUTH), so leaving them out simply falls back to token login rather than failing the
# build. libqrencode has no recipe in the current layer set; add one and put "nss libqrencode"
# into DEPENDS to turn it on.

do_compile() {
    oe_runmake CC="${CC}" PKG_CONFIG="${STAGING_BINDIR_NATIVE}/pkg-config"
}

do_install() {
    oe_runmake CC="${CC}" PKG_CONFIG="${STAGING_BINDIR_NATIVE}/pkg-config" \
               DESTDIR="${D}" install
}
