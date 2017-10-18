FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "b5ed6d16db88f1f6386549c32948633cb42b5bff"
PV = "1.19+gitr${SRCPV}"

RDEPENDS_${PN} += "mobile-broadband-provider-info ofono-conf"

SRC_URI  = " \
  git://git.merproject.org/mer-core/ofono.git;protocol=git;branch=upgrade-2.1.3 \
  file://0001-Enable-the-various-modem-plugins-we-support-again.patch;striplevel=2 \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://ofono \
  file://ofono.service \
"

S = "${WORKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree
B = "${S}"

EXTRA_OECONF_append = " --disable-sailfish-pushforwarder"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE_forcevariable = "enable"

RDEPENDS_${PN}-tests += "python"