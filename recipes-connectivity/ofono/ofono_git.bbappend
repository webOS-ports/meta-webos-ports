FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "a54f4eb6fab735503f1a40c3a9607f94ba637bd6"
PV = "1.12+git${SRCPV}"

DEPENDS += "android-audiosystem"

# NOTE: Needed for GPRS provisioning support
RDEPENDS_${PN}_append = " android-apn-database"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono-ubuntu.git;protocol=git;branch=master \
  file://ofono \
  file://ofono.upstart"

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
