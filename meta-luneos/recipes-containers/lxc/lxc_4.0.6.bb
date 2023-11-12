DESCRIPTION = "lxc aims to use these new functionnalities to provide an userspace container object"
SECTION = "console/utils"
LICENSE = "LGPL-2.1-only & GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe \
"

DEPENDS = "libxml2 libcap"
RDEPENDS:${PN} = " \
		rsync \
		curl \
		gzip \
		xz \
		tar \
		libcap-bin \
		bridge-utils \
		dnsmasq \
		perl-module-strict \
		perl-module-getopt-long \
		perl-module-vars \
		perl-module-exporter \
		perl-module-constant \
		perl-module-overload \
		perl-module-exporter-heavy \
		gmp \
		libidn \
		gnutls \
		nettle \
		util-linux-mountpoint \
		util-linux-getopt \
"

RDEPENDS:${PN}:append:libc-glibc = " glibc-utils"

RDEPENDS:${PN}-ptest += "file make gmp nettle gnutls bash libgcc"

RDEPENDS:${PN}-networking += "iptables"

SRC_URI = "http://linuxcontainers.org/downloads/${BPN}/${BPN}-${PV}.tar.gz \
	file://lxc-1.0.0-disable-udhcp-from-busybox-template.patch \
	file://run-ptest \
	file://lxc-fix-B-S.patch \
	file://lxc-doc-upgrade-to-use-docbook-3.1-DTD.patch \
	file://logs-optionally-use-base-filenames-to-report-src-fil.patch \
	file://templates-actually-create-DOWNLOAD_TEMP-directory.patch \
	file://template-make-busybox-template-compatible-with-core-.patch \
	file://templates-use-curl-instead-of-wget.patch \
	file://tests-our-init-is-not-busybox.patch \
	file://tests-add-no-validate-when-using-download-template.patch \
	file://dnsmasq.conf \
	file://lxc-net \
	"

SRC_URI[md5sum] = "732571c7cb4ab845068afb227bf35256"
SRC_URI[sha256sum] = "9165dabc0bb6ef7f2fda2009aee90b20fbefe77ed8008347e9f06048eba1e463"



S = "${WORKDIR}/${BPN}-${PV}"

# Let's not configure for the host distro.
#
PTEST_CONF = "${@bb.utils.contains('DISTRO_FEATURES', 'ptest', '--enable-tests', '', d)}"
EXTRA_OECONF += "--with-distro=${DISTRO} ${PTEST_CONF}"

EXTRA_OECONF += "--with-init-script=\
${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'sysvinit,', '', d)}\
${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

EXTRA_OECONF += "--enable-log-src-basename --disable-werror"

PACKAGECONFIG ??= "templates \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'seccomp', 'seccomp', '', d)} \
"
PACKAGECONFIG[doc] = "--enable-doc --enable-api-docs,--disable-doc --disable-api-docs,,"
PACKAGECONFIG[rpath] = "--enable-rpath,--disable-rpath,,"
PACKAGECONFIG[apparmor] = "--enable-apparmor,--disable-apparmor,apparmor,apparmor"
PACKAGECONFIG[templates] = ",,, ${PN}-templates"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux,libselinux"
PACKAGECONFIG[seccomp] ="--enable-seccomp,--disable-seccomp,libseccomp,libseccomp"
PACKAGECONFIG[systemd] = "--with-systemdsystemunitdir=${systemd_unitdir}/system/,--without-systemdsystemunitdir,systemd,"

# required by python3 to run setup.py
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

inherit autotools pkgconfig ptest update-rc.d systemd python3native

SYSTEMD_PACKAGES = "${PN} ${PN}-networking"
SYSTEMD_SERVICE:${PN} = "lxc.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
SYSTEMD_SERVICE:${PN}-networking = "lxc-net.service"
SYSTEMD_AUTO_ENABLE:${PN}-networking = "enable"

INITSCRIPT_PACKAGES = "${PN} ${PN}-networking"
INITSCRIPT_NAME:${PN} = "lxc-containers"
INITSCRIPT_PARAMS:${PN} = "defaults"
INITSCRIPT_NAME:${PN}-networking = "lxc-net"
INITSCRIPT_PARAMS:${PN}-networking = "defaults"

FILES:${PN}-doc = "${mandir} ${infodir}"
# For LXC the docdir only contains example configuration files and should be included in the lxc package
FILES:${PN} += "${docdir}"
FILES:${PN} += "${libdir}/python3*"
FILES:${PN} += "${datadir}/bash-completion"
FILES:${PN}-dbg += "${libexecdir}/lxc/.debug"
FILES:${PN}-dbg += "${libexecdir}/lxc/hooks/.debug"
PACKAGES =+ "${PN}-templates ${PN}-networking ${PN}-lua"
FILES:lua-${PN} = "${datadir}/lua ${libdir}/lua"
FILES:lua-${PN}-dbg += "${libdir}/lua/lxc/.debug"
FILES:${PN}-templates += "${datadir}/lxc/templates"
RDEPENDS:${PN}-templates += "bash"

FILES:${PN}-networking += " \
    ${sysconfdir}/init.d/lxc-net \
    ${sysconfdir}/default/lxc-net \
"

CACHED_CONFIGUREVARS += " \
    ac_cv_path_PYTHON='${STAGING_BINDIR_NATIVE}/python3-native/python3' \
    am_cv_python_pyexecdir='${PYTHON_SITEPACKAGES_DIR}' \
    am_cv_python_pythondir='${PYTHON_SITEPACKAGES_DIR}' \
"

do_install:append() {
	# The /var/cache/lxc directory created by the Makefile
	# is wiped out in volatile, we need to create this at boot.
	rm -rf ${D}${localstatedir}/cache
	install -d ${D}${sysconfdir}/default/volatiles
	echo "d root root 0755 ${localstatedir}/cache/lxc none" \
	     > ${D}${sysconfdir}/default/volatiles/99_lxc

	for i in `grep -l "#! */bin/bash" ${D}${datadir}/lxc/hooks/*`; do \
	    sed -e 's|#! */bin/bash|#!/bin/sh|' -i $i; done

	install -d ${D}${sysconfdir}/init.d
	install -m 755 config/init/sysvinit/lxc* ${D}${sysconfdir}/init.d

	# since python3-native is used for install location this will not be
	# suitable for the target and we will have to correct the package install
	if ${@bb.utils.contains('PACKAGECONFIG', 'python', 'true', 'false', d)}; then
	    if [ -d ${D}${exec_prefix}/lib/python* ]; then mv ${D}${exec_prefix}/lib/python* ${D}${libdir}/; fi
	    rmdir --ignore-fail-on-non-empty ${D}${exec_prefix}/lib
	fi

	# /etc/default/lxc sources lxc-net, this allows lxc bridge when lxc-networking
	# is not installed this results in no lxcbr0, but when lxc-networking is installed
	# lxcbr0 will be fully configured.
	install -m 644 ${WORKDIR}/lxc-net ${D}${sysconfdir}/default/

	# Force the main dnsmasq instance to bind only to specified interfaces and
	# to not bind to virbr0. Libvirt will run its own instance on this interface.
	install -d ${D}/${sysconfdir}/dnsmasq.d
	install -m 644 ${WORKDIR}/dnsmasq.conf ${D}/${sysconfdir}/dnsmasq.d/lxc
}

EXTRA_OEMAKE += "TEST_DIR=${D}${PTEST_PATH}/src/tests"

do_install_ptest() {
	# Move tests to the "ptest directory"
	install -d ${D}/${PTEST_PATH}/tests
	mv ${D}/usr/bin/lxc-test-* ${D}/${PTEST_PATH}/tests/.
}

pkg_postinst:${PN}() {
	if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
		/etc/init.d/populate-volatile.sh update
	fi
}

pkg_postinst:${PN}-networking() {
if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
cat >> $D/etc/network/interfaces << EOF

auto lxcbr0
iface lxcbr0 inet dhcp
	bridge_ports eth0
	bridge_fd 0
	bridge_maxwait 0
EOF

cat<<EOF>$D/etc/network/if-pre-up.d/lxcbr0
#! /bin/sh

if test "x\$IFACE" = xlxcbr0 ; then
        brctl show |grep lxcbr0 > /dev/null 2>/dev/null
        if [ \$? != 0 ] ; then
                brctl addbr lxcbr0
                brctl addif lxcbr0 eth0
                ip addr flush eth0
                ifconfig eth0 up
        fi
fi
EOF
chmod 755 $D/etc/network/if-pre-up.d/lxcbr0
fi
}
