FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "7331c88b41287886cf90cf8c2e3cb51116814e59"
PV = "1.16+gitr${SRCPV}"

RDEPENDS_${PN} += "mobile-broadband-provider-info ofono-conf"
RDEPENDS_${PN}-tests += "python3"

SRC_URI  = " \
  git://github.com/nemomobile-packages/ofono.git;protocol=git;branch=master \
  file://0001-Enable-the-various-modem-plugins-we-support-again.patch;striplevel=2 \
  file://0002-Disable-bluetooth-support-completely.patch;striplevel=2 \
  file://0003-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://0004-Disable-backtrace-cause-linking-to-libdl-fails.patch;striplevel=2 \
  file://ofono \
  file://ofono.service \
"
S = "${WORKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree
B = "${S}"

EXTRA_OECONF_append = " --disable-pushforwarder"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE_forcevariable = "enable"
