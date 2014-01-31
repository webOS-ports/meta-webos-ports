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
"

SRC_URI = "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz \
	file://lxc-0.9.0-disable-udhcp-from-busybox-template.patch \
	file://lxc-0.9.0-enable-chroot-chpasswd-functionality-for-busybox-hosts.patch \
	file://lxc-0.9.0-check-when-bind-mounting-libdirs.patch \
	file://lxc-0.9.0-don-t-let-LXC_PATH-end-in-failure.patch \
	file://lxc-0.9.0-setup_netdev-re-read-ifindex-in-LXC_NET_PHYS-case.patch \
	file://lxc-0.9.0-use-susv3-head-arguments.patch \
	"
SRC_URI[md5sum] = "8552a4479090616f4bc04d8473765fc9"
SRC_URI[sha256sum] = "1e1767eae6cc5fbf892c0e193d25da420ba19f2db203716c38f7cdea3b654120"

S = "${WORKDIR}/${BPN}-${PV}"

# Let's not configure for the host distro.
#
EXTRA_OECONF += "--with-distro=${DISTRO}"

PACKAGECONFIG ??= ""
PACKAGECONFIG[doc] = "--enable-doc,--disable-doc,,"
PACKAGECONFIG[rpath] = "--enable-rpath,--disable-rpath,,"
PACKAGECONFIG[apparmour] = "--enable-apparmor,--disable-apparmor,apparmor,apparmor"

inherit autotools pkgconfig

FILES_${PN}-doc = "${mandir} ${infodir}"
# For LXC the docdir only contains example configuration files and should be included in the lxc package
FILES_${PN} += "${docdir}"
FILES_${PN}-dbg += "${libexecdir}/lxc/.debug"

do_configure_prepend() {
	# drop -Werror until there is newer lxc version with subdir-objects enabled to fix build with automake-1.14
	# | automake: warning: possible forward-incompatibility.
	# | automake: At least a source file is in a subdirectory, but the 'subdir-objects'
	# | automake: automake option hasn't been enabled.  For now, the corresponding output
	# | automake: object file(s) will be placed in the top-level directory.  However,
	# | automake: this behaviour will change in future Automake versions: they will
	# | automake: unconditionally cause object files to be placed in the same subdirectory
	# | automake: of the corresponding sources.
	# | automake: You are advised to start using 'subdir-objects' option throughout your
	# | automake: project, to avoid future incompatibilities.
	# | src/lxc/Makefile.am:79: warning: source file '../include/lxcmntent.c' is in a subdirectory,
	# | src/lxc/Makefile.am:79: but option 'subdir-objects' is disabled
	sed 's/AM_INIT_AUTOMAKE(\[-Wall -Werror -Wno-portability\])/AM_INIT_AUTOMAKE([-Wall -Wno-portability])/g' -i ${S}/configure.ac
}

do_install_append() {
	# The /var/cache/lxc directory created by the Makefile
	# is wiped out in volatile, we need to create this at boot.
	rm -rf ${D}${localstatedir}/cache
	install -d ${D}${sysconfdir}/default/volatiles
	echo "d root root 0755 ${localstatedir}/cache/lxc none" \
	     > ${D}${sysconfdir}/default/volatiles/99_lxc

}

pkg_postinst_${PN}() {
	if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
		/etc/init.d/populate-volatile.sh update
	fi
}
