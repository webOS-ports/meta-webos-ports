DESCRIPTION = "enyo-dev command-line tools used by webOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

inherit native

SRC_URI = "git://github.com/enyojs/enyo-dev-dist.git;branch=master"
SRCREV = "3d0546ecd1846c80150fcd6928e953cff7006a47"
PV = "0.5.2"
S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${base_prefix}/opt/enyo-dev
    cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}${base_prefix}/opt/enyo-dev
}

sysroot_stage_all_append() {
    sysroot_stage_dir ${D}${base_prefix}/opt ${SYSROOT_DESTDIR}${base_prefix}/opt
}
