SUMMARY = "LuneOS default wallpapers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.0-1+git${SRCPV}"
SRCREV = "7c15135ca9af6d36a85491e481fb069f9bb12ddd"

inherit webos_ports_repo
inherit webos_filesystem_paths

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_mountablestoragedir}/wallpapers
    cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}${webos_mountablestoragedir}/wallpapers
    rm ${D}${webos_mountablestoragedir}/wallpapers/README.md
}

FILES_${PN} = "${webos_mountablestoragedir}/wallpapers"
