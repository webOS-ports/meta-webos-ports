SUMMARY = "FreeImage tool used by WhatsApp Purple"
SECTION = "webos/support"

#Due to issues with CRLF line endings in LibWebP now using our own fork, so we don't need to patch in OE which caused issues. In future we migth be able to go back to upstream 3.18.0 release directly for example.
SRC_URI = "git://github.com/webos-ports/FreeImage;branch=herrie/gcc6-arm;protocol=https"

SRCREV = "c01653d6b12939877e79d2787d3695865e8b1ee3"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://license-fi.txt;md5=7d2690b4d6d7dd53d69a773664bc4850 \
    file://license-gplv2.txt;md5=0440c487be0c0200c36caf975ab31174 \
    file://license-gplv3.txt;md5=e9661e0bea741d71a430b23475da519e \
"

TARGET_CC_ARCH += "${LDFLAGS}"

do_configure() {
    sed -i -e /^CC/d \
           -e /^CXX\ /d \
           -e /^AR/d \
           -e /^INCDIR\ /d \
           -e /^INSTALLDIR\ /d \
           -e s:'-o root -g root'::g \
           -e /ldconfig/d \
    ${S}/Makefile.gnu
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    oe_runmake INSTALLDIR="${D}${libdir}" INCDIR="${D}${includedir}" install
}

INSANE_SKIP:${PN} = "already-stripped dev-so"

FILES:${PN} += "${libdir}"
FILES_SOLIBSDEV = "{libdir}/lib${BP}${SOLIBSDEV}"
