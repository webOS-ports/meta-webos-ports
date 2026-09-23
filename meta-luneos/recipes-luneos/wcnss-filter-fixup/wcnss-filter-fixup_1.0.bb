DESCRIPTION = "Stop the redundant, busy-looping wcnss_filter"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "base"

COMPATIBLE_MACHINE = "^(tissot|tissot-halium)$"

# The unit and script name the machine, and the guard is only correct on a
# device whose BT HAL owns the SMD channels itself.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit systemd

PR = "r1"

SRC_URI = " \
    file://wcnss-filter-fixup.sh \
    file://wcnss-filter-fixup.service \
"

S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/wcnss-filter-fixup.sh \
        ${D}${sbindir}/tissot-wcnss-filter-fixup.sh

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/wcnss-filter-fixup.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE:${PN} = "wcnss-filter-fixup.service"

# setprop comes from the Android container's property tooling; procps/psmisc
# supply pidof and pkill.
RDEPENDS:${PN} = "procps psmisc"
