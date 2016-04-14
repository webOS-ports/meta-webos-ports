FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "403f29320c68ead8b93ca422b1c3c509f8bff482"
PV = "1.17+gitr${SRCPV}"

RDEPENDS_${PN} += "mobile-broadband-provider-info ofono-conf"
RDEPENDS_${PN}-tests += "python3"

SRC_URI  = " \
  git://git.merproject.org/mer-core/ofono.git;protocol=git;branch=master \
  file://0001-Enable-the-various-modem-plugins-we-support-again.patch;striplevel=2 \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
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
