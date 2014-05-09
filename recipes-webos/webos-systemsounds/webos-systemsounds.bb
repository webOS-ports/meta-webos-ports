SUMMARY = "System sounds effects for webOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6c4db32a2fa8717faffa1d4f10136f47"

PV = "1.0.0+git${SRCPV}"

SRC_URI = "git://github.com/openwebos/image-assets;protocol=git;branch=master"
S = "${WORKDIR}/git"

SRCREV = "c4fc6d761deb548b492735683efed1718935ed71"

do_install() {
    install -d ${D}${datadir}/systemsounds
    cp -rv ${S}/share/systemsounds/* ${D}${datadir}/systemsounds
}

FILES_${PN} += "${datadir}/systemsounds"
