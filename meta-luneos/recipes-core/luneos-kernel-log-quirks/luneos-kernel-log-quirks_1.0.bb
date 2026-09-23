DESCRIPTION = "systemd-tmpfiles rules that quieten chatty vendor kernel drivers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "base"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

SRC_URI = "file://luneos-charger-debug.conf"

S = "${UNPACKDIR}"

PR = "r1"

do_install() {
    install -d ${D}${libdir}/tmpfiles.d
    install -m 0644 ${UNPACKDIR}/luneos-charger-debug.conf ${D}${libdir}/tmpfiles.d/
}

FILES:${PN} = "${libdir}/tmpfiles.d"

RDEPENDS:${PN} = "systemd"
