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
"

# Put sysvinit script into own package to move away from our image
UPDATERCPN = "${PN}-sysvinit"
PACKAGES =+ "${PN}-sysvinit"
FILES_${PN}-sysvinit = "${sysconfdir}/"
