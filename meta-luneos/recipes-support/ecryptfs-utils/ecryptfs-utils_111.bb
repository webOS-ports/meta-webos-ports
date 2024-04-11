# Imported from meta-security as in:
# http://git.yoctoproject.org/cgit/cgit.cgi/meta-security/commit/?id=4f2a08e3faf46c3179e334af5882045fd9cb5162
# Replaced bb.utils.filter with @bb.utils.contains because our Yocto 2.2 doesn't support filter yet

SUMMARY = "The eCryptfs mount helper and support libraries"
DESCRIPTION = "eCryptfs is a stacked cryptographic filesystem \
    that ships in Linux kernel versions 2.6.19 and above. This \
    package provides the mount helper and supporting libraries \
    to perform key management and mount functions."
HOMEPAGE = "https://launchpad.net/ecryptfs"
SECTION = "base"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

DEPENDS = "keyutils libgcrypt intltool-native glib-2.0-native"

PR = "r1"

SRC_URI = "\
    https://launchpad.net/ecryptfs/trunk/${PV}/+download/${BPN}_${PV}.orig.tar.gz \
    file://ecryptfs-utils-CVE-2016-6224.patch \
    file://ecryptfs.service \
"

SRC_URI[md5sum] = "83513228984f671930752c3518cac6fd"
SRC_URI[sha256sum] = "112cb3e37e81a1ecd8e39516725dec0ce55c5f3df6284e0f4cc0f118750a987f"

inherit autotools pkgconfig systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "ecryptfs.service"

EXTRA_OECONF = "\
    --libdir=${base_libdir} \
    --disable-pywrap \
    --disable-nls \
"

PACKAGECONFIG ??= "nss \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)} \
"
PACKAGECONFIG[nss] = "--enable-nss,--disable-nss,nss,"
PACKAGECONFIG[openssl] = "--enable-openssl,--disable-openssl,openssl,"
PACKAGECONFIG[pam] = "--enable-pam --with-pamdir=${base_libdir}/security,--disable-pam,libpam,"

do_configure:prepend() {
    export NSS_CFLAGS="-I${STAGING_INCDIR}/nspr -I${STAGING_INCDIR}/nss3"
    export NSS_LIBS="-L${STAGING_BASELIBDIR} -lssl3 -lsmime3 -lnss3 -lsoftokn3 -lnssutil3"
    export KEYUTILS_CFLAGS="-I${STAGING_INCDIR}"
    export KEYUTILS_LIBS="-L${STAGING_LIBDIR} -lkeyutils"
}

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'usrmerge', 'true', 'false', d)}; then
        install -d ${D}${base_sbindir}
        mv ${D}/sbin/* ${D}${base_sbindir}
        rmdir ${D}/sbin
    else
        mkdir -p ${D}/${libdir}
        mv ${D}/${base_libdir}/pkgconfig ${D}/${libdir}
    fi
    chmod 4755 ${D}${base_sbindir}/mount.ecryptfs_private
    sed -i -e 's:-I${STAGING_INCDIR}::' \
           -e 's:-L${STAGING_LIBDIR}::' ${D}/${libdir}/pkgconfig/libecryptfs.pc
    sed -i -e "s: ${base_sbindir}/cryptsetup: ${sbindir}/cryptsetup:" ${D}${bindir}/ecryptfs-setup-swap
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -m 0644 ${WORKDIR}/ecryptfs.service ${D}${systemd_system_unitdir}/ecryptfs.service
    fi
}

FILES:${PN} += "${base_libdir}/security/* ${base_libdir}/ecryptfs/*"

RDEPENDS:${PN} += "cryptsetup"
RRECOMMENDS:${PN} = "gettext-runtime"
