FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "7fe16c7df411c5093a80eddee56a844ae4c7f920"
PV = "1.12+git${SRCPV}"

# NOTE: Needed for GPRS provisioning support
RDEPENDS_${PN}_append = " android-apn-database"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono-ubuntu.git;protocol=git;branch=master \
  file://ofono \
  file://ofono.upstart \
  file://0001-Disable-support-for-android-audiosystem.patch \
"

do_configure_prepend() {
    # Workaround missing bootstrap script which is required by our base recipe
    touch ${S}/bootstrap
    chmod +x ${S}/bootstrap
}

do_install_append() {
    install -d ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/ofono.upstart ${D}${webos_upstartconfdir}/ofono
}

# for update-rc.d.bbclass
UPDATERCPN = "${PN}-sysvinit"

PACKAGES =+ "${PN}-sysvinit ${PN}-upstart"
FILES_${PN}-sysvinit = "${sysconfdir}/init.d/ofono"
FILES_${PN}-upstart = "${webos_upstartconfdir}/ofono"
