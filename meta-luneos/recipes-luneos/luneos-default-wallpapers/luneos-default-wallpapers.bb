SUMMARY = "LuneOS default wallpapers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.0-1+git${SRCPV}"
SRCREV = "c063f57b20a2cfc11ad0e097547610f9bcf242fc"

inherit webos_ports_repo
inherit webos_filesystem_paths

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${datadir}/wallpapers
    cp -R --no-dereference --preserve=mode,links -v ${S}/*.jpg ${D}${datadir}/wallpapers
}

FILES:${PN} = "${datadir}/wallpapers"
