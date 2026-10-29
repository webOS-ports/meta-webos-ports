DESCRIPTION = "enyo-dev command-line tools used by webOS"
LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${UNPACKDIR}/graceful-fs/package/LICENSE;md5=163972d49c2f7a3d3b687aeb48e9e3c9 \
"

PR = "r2"

inherit native

# graceful-fs 4.2.11: see do_install. Verified against the npm registry's
# dist.integrity for 4.2.11.
SRC_URI = "git://github.com/enyojs/enyo-dev-dist.git;branch=master;protocol=https \
           https://registry.npmjs.org/graceful-fs/-/graceful-fs-4.2.11.tgz;name=gracefulfs;subdir=graceful-fs \
"
SRCREV = "ba42e8cb3fe061380f77fd36baa206205a4ac06b"
SRC_URI[gracefulfs.sha256sum] = "3961374aa161e6fed80d6f4b6aaf2fb7eafd2c9e5be34e865b375f0114dd099c"
PV = "0.5.2"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${base_prefix}/opt/enyo-dev
    cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}${base_prefix}/opt/enyo-dev

    # enyo-dev-dist ships Node v0.12.2 for i686 and x86_64 only. On any other
    # build host (aarch64) webos_enyodev_application falls back to nodejs-native,
    # and on a current Node the 2015-era graceful-fs 2.x/3.x copies bundled in
    # node_modules die at load time ("ReferenceError: primordials is not
    # defined", fs-extra -> graceful-fs 3.0.8). graceful-fs 4.2 is the drop-in
    # replacement for that API. Hosts that run the bundled Node keep the tree
    # exactly as shipped.
    if [ ! -x ${S}/node_binaries/${BUILD_ARCH}/node ]; then
        find ${D}${base_prefix}/opt/enyo-dev/node_modules -type d -name graceful-fs | while read d; do
            rm -rf "$d"
            cp -R ${UNPACKDIR}/graceful-fs/package "$d"
        done
    fi
}

# The bundled node_binaries are prebuilt for x86; a non-x86 host's strip cannot
# read them ("Unable to recognise the format of the input file") and
# do_populate_sysroot fails. They are used as shipped, so do not strip them.
INHIBIT_SYSROOT_STRIP = "1"

SYSROOT_DIRS += "${base_prefix}/opt"
