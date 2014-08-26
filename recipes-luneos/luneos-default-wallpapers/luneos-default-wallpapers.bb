SUMMARY = "LuneOS default wallpapers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "1.0.0-1_7c15135ca9af6d36a85491e481fb069f9bb12ddd"

inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "git://github.com/webOS-ports/${PN}.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${datadir}/wallpapers
    cp -av ${S}/* ${D}${datadir}/wallpapers
    rm ${D}${datadir}/wallpapers/README.md
}

FILES_${PN} = "${datadir}/wallpapers"
