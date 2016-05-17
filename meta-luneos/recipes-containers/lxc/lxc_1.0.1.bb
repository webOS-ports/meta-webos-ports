# imported from meta-virtualization
# http://git.yoctoproject.org/cgit/cgit.cgi/meta-virtualization/commit/recipes-containers/lxc/lxc_1.0.1.bb?id=5b57bf462b41142deae0479c06f4da8e0b66bb7e
# dropped PR and PRIORITY

DESCRIPTION = "lxc aims to use these new functionnalities to provide an userspace container object"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
DEPENDS = "libxml2 libcap"
RDEPENDS_${PN} = " \
    rsync \
    gzip \
    libcap-bin \
    bridge-utils \
    dnsmasq \
    perl-module-strict \
    perl-module-getopt-long \
    perl-module-vars \
    perl-module-warnings-register \
    perl-module-exporter \
    perl-module-constant \
    perl-module-overload \
    perl-module-exporter-heavy \
    bash \
"
RDEPENDS_${PN}-ptest += "file make"

SRC_URI = "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz \
    file://disable-udhcp-from-busybox-template.patch \
    file://config_network_type-set-macvlan-default-mode-to-priv.patch \
    file://lxc-busybox-follow-symlinks-when-inspecting-busybox-.patch \
    file://network.c-Add-missing-LXC_NET_NONE-option-refactor.patch \
    file://runtest.patch \
    file://run-ptest \
    file://automake-ensure-VPATH-builds-work-correctly.patch \
    file://0001-tests-remove-target-for-unneeded-tests-which-let-the.patch \
"
SRC_URI[md5sum] = "3c7379891e45713817ec873a167070b0"
SRC_URI[sha256sum] = "17d8e5b575207b4fb57da0b8ba2d13f3e5ee20ce8ccd1259d6eae4bd5ca575b1"

S = "${WORKDIR}/${BPN}-${PV}"

# Let's not configure for the host distro.
#
PTEST_CONF = "${@bb.utils.contains('DISTRO_FEATURES', 'ptest', '--enable-tests', '', d)}"
EXTRA_OECONF += "--with-distro=${DISTRO} ${PTEST_CONF}"

PACKAGECONFIG ??= ""
PACKAGECONFIG[doc] = "--enable-doc,--disable-doc,,"
PACKAGECONFIG[rpath] = "--enable-rpath,--disable-rpath,,"
PACKAGECONFIG[apparmour] = "--enable-apparmor,--disable-apparmor,apparmor,apparmor"
PACKAGECONFIG[python] = "--enable-python,--disable-python,python3,python3"

inherit autotools pkgconfig ptest

FILES_${PN}-doc = "${mandir} ${infodir}"
# For LXC the docdir only contains example configuration files and should be included in the lxc package
FILES_${PN} += "${docdir}"
FILES_${PN}-dbg += "${libexecdir}/lxc/.debug"

PRIVATE_LIBS_${PN}-ptest = "liblxc.so.1"

do_install_append() {
    # The /var/cache/lxc directory created by the Makefile
    # is wiped out in volatile, we need to create this at boot.
    rm -rf ${D}${localstatedir}/cache
    install -d ${D}${sysconfdir}/default/volatiles
    echo "d root root 0755 ${localstatedir}/cache/lxc none" \
         > ${D}${sysconfdir}/default/volatiles/99_lxc
}

EXTRA_OEMAKE += "TEST_DIR=${D}${PTEST_PATH}/src/tests"

do_install_ptest() {
    oe_runmake -C src/tests install-ptest
}

pkg_postinst_${PN}() {
    if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
        /etc/init.d/populate-volatile.sh update
    fi
}

