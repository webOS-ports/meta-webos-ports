SUMMARY = "Android header files"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://github.com/webOS-ports/phablet-headers.git;branch=cm-11.0;protocol=git"
SRCREV = "8c9983e80b1419acf8f9ea153f3a6b47e5e4de6d"
S = "${WORKDIR}/git"

PV = "4.4.3+gitr${SRCPV}"

PROVIDES += "virtual/android-headers"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${includedir}/android
    cp -rv ${S}/* ${D}${includedir}/android/
    rm ${D}${includedir}/android/android-headers.pc

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${S}/android-headers.pc ${D}${libdir}/pkgconfig
}

FILES_${PN}-dev += "${libdir}/pkg-config"
