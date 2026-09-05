SUMMARY = "Fingerprint daemon bridging the Android biometrics HAL to D-Bus"
DESCRIPTION = "Fingerprint enrollment/identification daemon for Halium devices. \
Talks to the vendor android.hardware.biometrics.fingerprint@2.1 HIDL HAL \
through libhybris (android_dlopen of libbiometry_fp_api.so, which has to be \
built inside the device's Halium tree) and exposes the SailfishOS-style \
fingerprint D-Bus API as org.droidian.fingerprint on the system bus. \
Fork lineage: UBports biometryd -> sailfish-fpd-community -> droidian-fpd."
HOMEPAGE = "https://github.com/FuriLabs/droidian-fpd"
SECTION = "webos/support"
# Daemon is GPL-3.0; the biometryd-derived bridge/HAL glue it embeds
# (src/bridge/, src/hardware/) is LGPL-3.0 Canonical/UBports code.
LICENSE = "GPL-3.0-only AND LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "qtbase libhybris"

# Needs libhybris and the Android container providing the fingerprint HAL.
COMPATIBLE_MACHINE = "^halium$"
# libhybris is MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig
inherit qt6-cmake
inherit systemd

SRC_URI = " \
    git://github.com/FuriLabs/droidian-fpd.git;protocol=https;branch=trixie \
    file://0001-Use-uid-0-for-the-single-LuneOS-user.patch \
    file://0002-Treat-fingerId-0-as-a-failed-match.patch \
    file://0003-Add-a-Rename-method.patch \
    file://droidian-fpd.service \
    file://fpd-hal-setup.sh \
    file://90-uinput-fpc-ignore.rules \
    file://50-no-reboot-key.conf \
"

PV = "2.0+git"
SRCREV = "5296a7d9537b1cdf616ca2f601c54148440acf6e"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "droidian-fpd.service"

# The daemon shells out to /usr/bin/getprop (util/property_store.cpp) to read
# ro.build.version.sdk for the template-store path selection; android-tools is
# already part of the halium stack via packagegroup-luneos-extended.
RDEPENDS:${PN} += "android-tools"

# Upstream's CMakeLists.txt has no install rules (Debian packaging installs by
# hand), and its unit is written for Droidian's lxc@android + binder-wait
# setup, so we ship our own unit from ${UNPACKDIR}.
do_install() {
    install -D -m 0755 ${B}/droidian-fpd ${D}${sbindir}/droidian-fpd
    install -D -m 0644 ${S}/org.droidian.fingerprint.conf ${D}${sysconfdir}/dbus-1/system.d/org.droidian.fingerprint.conf
    install -D -m 0644 ${UNPACKDIR}/droidian-fpd.service ${D}${systemd_system_unitdir}/droidian-fpd.service
    install -D -m 0755 ${UNPACKDIR}/fpd-hal-setup.sh ${D}${bindir}/fpd-hal-setup.sh
    install -D -m 0644 ${UNPACKDIR}/90-uinput-fpc-ignore.rules ${D}${sysconfdir}/udev/rules.d/90-uinput-fpc-ignore.rules
    install -D -m 0644 ${UNPACKDIR}/50-no-reboot-key.conf ${D}${sysconfdir}/systemd/logind.conf.d/50-no-reboot-key.conf
}
