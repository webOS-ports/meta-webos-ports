DESCRIPTION = "Stop the redundant, busy-looping wcnss_filter"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "base"

# On tissot the A9 vendor's BT HAL (android.hardware.bluetooth@1.0-service-qti)
# opens /dev/smd2 and /dev/smd3 directly, and LuneOS reaches it through
# bluebinder -> /dev/vhci -> hci0. libbt-vendor nevertheless ctl.start's
# /system/vendor/bin/wcnss_filter during BT power-on, but the SMD channels are
# already taken, so its SoC open fails and handle_soc_events() spins on the dead
# fd instead of exiting, logging
#
#   E WCNSS_FILTER: handle_soc_events: Unexpected data format!!:55 - Ignore the Packet
#
# at roughly 62000 lines/s. Measured on tissot: ~96% of one CPU core in
# wcnss_filter plus ~36% of a core in logd absorbing the spam, both CPU clusters
# pinned at their maximum frequency, and the device unable to idle.
#
# Bluetooth is unaffected by stopping it - verified through a full cold re-init
# (BT HAL restart + bluebinder restart + hci0 up) with the filter stopped, after
# which inquiry scans work normally.
#
# This is a host-side workaround. The better fix belongs in the vendor image:
# drop the ctl.start, or neutralise the vendor.start_hci_filter service in
# init.qcom.rc. Once that lands, this package can go - the guard already no-ops
# when the filter never appears.
COMPATIBLE_MACHINE = "^(tissot|tissot-halium)$"

# The unit and script name the machine, and the guard is only correct on a
# device whose BT HAL owns the SMD channels itself.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit systemd

SRC_URI = " \
    file://wcnss-filter-fixup.sh \
    file://wcnss-filter-fixup.service \
"

S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/wcnss-filter-fixup.sh ${D}${sbindir}/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/wcnss-filter-fixup.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE:${PN} = "wcnss-filter-fixup.service"

# setprop comes from the Android container's property tooling; procps/psmisc
# supply pidof and pkill.
RDEPENDS:${PN} = "procps psmisc"
