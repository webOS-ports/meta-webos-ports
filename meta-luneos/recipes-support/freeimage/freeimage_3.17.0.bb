SRC_URI = "${SOURCEFORGE_MIRROR}/freeimage/FreeImage3170.zip \
file://0001-dsp-mips-add-whitespace-around-stringizing-operator.patch"

SRC_URI[md5sum] = "459e15f0ec75d6efa3c7bd63277ead86"
SRC_URI[sha256sum] = "fbfc65e39b3d4e2cb108c4ffa8c41fd02c07d4d436c594fff8dab1a6d5297f89"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
    file://license-fi.txt;md5=8e1438cab62c8f655288588dc43daaf6 \
    file://license-gplv2.txt;md5=1fbed70be9d970d3da399f33dae9cc51 \
    file://license-gplv3.txt;md5=b5c176c43d7fb06bf6dd56e79c490f5b \
"

S = "${WORKDIR}/FreeImage/"

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
