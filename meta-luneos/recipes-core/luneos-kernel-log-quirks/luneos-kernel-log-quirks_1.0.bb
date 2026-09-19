DESCRIPTION = "systemd-tmpfiles rules that quieten chatty vendor kernel drivers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "base"

# Machine-independent on purpose. Every rule here uses tmpfiles' "w", which only
# writes a file that already exists, so a rule for a module this device does not
# have is a no-op rather than an error. That keeps the package the same on every
# device and avoids a MACHINE-specific variant per PMIC.
PACKAGE_ARCH = "${TUNE_PKGARCH}"

SRC_URI = "file://luneos-charger-debug.conf"

S = "${UNPACKDIR}"

PR = "r1"

# Installed under ${libdir}/tmpfiles.d rather than ${sysconfdir}/tmpfiles.d so
# that it is shipped configuration a user can still override by dropping a file
# of the same name in /etc/tmpfiles.d.
#
# It deliberately does NOT go through luneos-device-config's per-device sparse
# overlay: that unit is only ordered after systemd-udev-trigger and before
# nyx.target, with no ordering against systemd-tmpfiles-setup.service, so a
# tmpfiles rule arriving via the overlay can easily land after tmpfiles has
# already run.
do_install() {
    install -d ${D}${libdir}/tmpfiles.d
    install -m 0644 ${UNPACKDIR}/luneos-charger-debug.conf ${D}${libdir}/tmpfiles.d/
}

FILES:${PN} = "${libdir}/tmpfiles.d"

RDEPENDS:${PN} = "systemd"
