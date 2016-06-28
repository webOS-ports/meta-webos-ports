
SRC_URI = "git://github.com/webos-ports/FreeImage;branch=webOS-ports/master"

SRCREV = "08068610929df4602a0edb7b0d0f1779c9296e5c"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
    file://license-fi.txt;md5=7d2690b4d6d7dd53d69a773664bc4850 \
    file://license-gplv2.txt;md5=0440c487be0c0200c36caf975ab31174 \
    file://license-gplv3.txt;md5=e9661e0bea741d71a430b23475da519e \
"

S = "${WORKDIR}/git/"

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

INSANE_SKIP_${PN} = "already-stripped dev-so"

FILES_${PN} += "${libdir}"
FILES_SOLIBSDEV = "{libdir}/lib${BP}${SOLIBSDEV}"
