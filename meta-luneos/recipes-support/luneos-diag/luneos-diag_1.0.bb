SUMMARY = "Boot-time hardware state dump"
DESCRIPTION = "Writes /var/log/luneos-diag.txt once per boot with the sysfs \
state that the kernel log does not show: dwc3/UDC/gadget binding, Type-C \
roles, extcon cables, power supplies, DRM connector status, which process has \
/dev/dri open, whether libEGL advertises wayland-display, and the wireless \
regulatory database."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# No COMPATIBLE_MACHINE.
#
# This lived in meta-pine64-luneos pinned to the three Pine boards, which made
# every other device that wanted it carry a bbappend widening the list -
# meta-mecha-luneos had one for the Comet. Worse, that bbappend made the Comet
# layer silently require the Pine layer: without it the append has no recipe and
# the parse dies with "No recipes available for .../luneos-diag_1.0.bbappend",
# which names only the append and reads like the recipe was deleted.
#
# Nothing in it is board-specific. luneos-diag.sh dumps dwc3/UDC gadget binding,
# Type-C roles, extcon cables, power supplies, DRM nodes and their holders, the
# libEGL wl_drm check and the regulatory domain - there is not one pine, rk3 or
# sun50i string in it. So it belongs in the generic layer with no machine list,
# and a board opts in the ordinary way, with MACHINE_EXTRA_RRECOMMENDS.

SRC_URI = " \
    file://luneos-diag.sh \
    file://luneos-diag.service \
    file://luneos-diag-event.service \
    file://90-luneos-diag.rules \
"

S = "${UNPACKDIR}"

# allarch, now that it can be.
#
# The previous comment here read "Not allarch: allarch recipes are shared across
# machines, which contradicts COMPATIBLE_MACHINE and confuses sstate reuse" -
# but it never set PACKAGE_ARCH, so the recipe was built at TUNE_PKGARCH and
# shared across every aarch64 machine anyway (tmp/work/aarch64-webos-linux/).
# The stated intent was not implemented, and with COMPATIBLE_MACHINE gone the
# objection no longer exists.
#
# The package is three systemd units, a udev rule and a POSIX shell script:
# nothing compiled, nothing machine-dependent. One build now serves every
# device, including the x86 emulators.
inherit allarch systemd

# luneos-diag-event.service is started by udev (SYSTEMD_WANTS), never enabled.
SYSTEMD_SERVICE:${PN} = "luneos-diag.service"
SYSTEMD_AUTO_ENABLE = "enable"

# iw is only used if present - the script degrades gracefully without it, but
# "iw reg get" is the quickest way to see which regulatory domain won.
RRECOMMENDS:${PN} = "iw"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/luneos-diag.sh ${D}${sbindir}/luneos-diag.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/luneos-diag.service ${D}${systemd_system_unitdir}/luneos-diag.service
    install -m 0644 ${UNPACKDIR}/luneos-diag-event.service ${D}${systemd_system_unitdir}/luneos-diag-event.service

    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/90-luneos-diag.rules ${D}${nonarch_base_libdir}/udev/rules.d/90-luneos-diag.rules
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/luneos-diag.service \
    ${systemd_system_unitdir}/luneos-diag-event.service \
    ${nonarch_base_libdir}/udev/rules.d/90-luneos-diag.rules \
"
