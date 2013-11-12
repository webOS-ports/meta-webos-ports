FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "351893629f08356e3565ecd5dbc62ce15adc276a"
PV = "1.12+git${SRCPV}"

# NOTE: Needed for GPRS provisioning support
RDEPENDS_${PN}_append = " android-apn-database"

SRC_URI  = " \
  git://github.com/rilmodem/ofono.git;protocol=git;branch=master \
  file://ofono \
  file://missing-ssize_t.patch \
  file://0001-Disable-backtrace-cause-linking-to-libdl-fails.patch \
  file://ofono.service \
  file://wait-for-rild.sh \
"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service

    install -d ${D}${bindir}
    install -m 0744 ${WORKDIR}/wait-for-rild.sh ${D}${bindir}
}
