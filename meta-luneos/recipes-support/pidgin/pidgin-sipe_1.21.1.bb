SUMMARY = "Protocol plugin for Office 365/Lync/OCS for Adium, Pidgin, Miranda and Telepathy IM Framework"
SECTION = "webos/services"
LICENSE = "GPLv2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "pidgin intltool-native"

inherit pkgconfig
inherit autotools

SRC_URI = "${SOURCEFORGE_MIRROR}/sipe/pidgin-sipe-${PV}.tar.xz"
SRC_URI[md5sum] = "96ea396adb5c8a1bb1749d778892605c"
SRC_URI[sha256sum] = "651af55d65cbdf5bdbee0366bd5151bec4152c910743aa432204d015893fe444"

PACKAGECONFIG ??= "nss krb5"
PACKAGECONFIG[nss] = "--enable-nss=yes,--enable-nss=no,nss"
PACKAGECONFIG[openssl] = "--enable-openssl=yes,--enable-openssl=no,openssl"
PACKAGECONFIG[krb5] = "--with-krb5=yes,--with-krb5=no,krb5"
#PACKAGECONFIG[voice_and_video] = "--with-vv=yes,--with-vv=no,libnice gstreamer"
PACKAGECONFIG[telepathy] = "--enable-telepathy=yes,--enable-telepathy=no,telepathy-glib gmime"
#PACKAGECONFIG[gssapi_only] = "--enable-gssapi-only=yes,--enable-gssapi-only=no,krb5"
PACKAGECONFIG[debug] = "--enable-debug=yes,--enable-debug=no,valgrind"

FILES_${PN}-dev += " \
    ${libdir}/purple-2/*.la \
"

FILES_${PN} += " \
    ${libdir}/purple-2/libsipe.so \
    ${datadir}/appdata \
"
