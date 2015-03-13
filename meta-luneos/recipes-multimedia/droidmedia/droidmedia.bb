SUMMARY = "Android Media components wrapper library based on libhybris"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "0.0+gitr${SRCPV}"
SRCREV = "76de8e04d29a59936d34bc6dd6953998ea205253"

SRC_URI = "git://github.com/sailfishos/droidmedia.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${includedir}/droidmedia
    install -m 0644 ${S}/*.h ${D}${includedir}/droidmedia

    install -d ${D}${datadir}/droidmedia
    install -m 0644 ${S}/hybris.c ${D}${datadir}/droidmedia/
}
