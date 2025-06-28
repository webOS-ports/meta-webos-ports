DESCRIPTION = "enyo-dev command-line tools used by webOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

inherit native

SRC_URI = "git://github.com/enyojs/enyo-dev-dist.git;branch=master;protocol=https"
SRCREV = "ba42e8cb3fe061380f77fd36baa206205a4ac06b"
PV = "0.5.2"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${base_prefix}/opt/enyo-dev
    cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}${base_prefix}/opt/enyo-dev
}

SYSROOT_DIRS += "${base_prefix}/opt"
