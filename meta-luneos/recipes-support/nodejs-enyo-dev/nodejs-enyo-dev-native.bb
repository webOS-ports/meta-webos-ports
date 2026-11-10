DESCRIPTION = "enyo-dev command-line tools used by webOS"
LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${UNPACKDIR}/graceful-fs/package/LICENSE;md5=163972d49c2f7a3d3b687aeb48e9e3c9"

PR = "r2"

inherit native

SRC_URI = "git://github.com/enyojs/enyo-dev-dist.git;branch=master;protocol=https \
           https://registry.npmjs.org/graceful-fs/-/graceful-fs-4.2.11.tgz;name=graceful-fs;downloadfilename=graceful-fs-4.2.11-npm.tgz;subdir=graceful-fs \
"
SRCREV = "ba42e8cb3fe061380f77fd36baa206205a4ac06b"
SRC_URI[graceful-fs.sha256sum] = "3961374aa161e6fed80d6f4b6aaf2fb7eafd2c9e5be34e865b375f0114dd099c"
PV = "0.5.2"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# enyo-dev-dist ships its own Node 0.12.2 for i686 and x86_64 in node_binaries.
# It cannot run on any other build host, and on an aarch64 host strip cannot
# even read it, so do_populate_sysroot fails. The tools run on nodejs-native
# instead (webos_enyodev_application). For that they need a graceful-fs newer
# than the 3.0.8 and 2.0.3 copies bundled here, which use process.binding()
# and do not load on Node 22; with 4.2.11, enyo pack produces the same
# JavaScript as under Node 0.12.
ENYO_DEV_OLD_GRACEFUL_FS = "\
    node_modules/fs-extra/node_modules/graceful-fs \
    node_modules/less/node_modules/graceful-fs \
    node_modules/mocha/node_modules/glob/node_modules/graceful-fs \
"

do_install() {
    install -d ${D}${base_prefix}/opt/enyo-dev
    cp -R --no-dereference --preserve=mode,links ${S}/* ${D}${base_prefix}/opt/enyo-dev
    rm -rf ${D}${base_prefix}/opt/enyo-dev/node_binaries
    for dir in ${ENYO_DEV_OLD_GRACEFUL_FS}; do
        test -d ${D}${base_prefix}/opt/enyo-dev/$dir || bbfatal "$dir is no longer in enyo-dev"
        rm -rf ${D}${base_prefix}/opt/enyo-dev/$dir
        cp -R --no-dereference --preserve=mode,links ${UNPACKDIR}/graceful-fs/package ${D}${base_prefix}/opt/enyo-dev/$dir
    done
}

SYSROOT_DIRS += "${base_prefix}/opt"
