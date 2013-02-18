FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "a059b470b47b2375aa94faed5c1a58e96a6567e2"
PV = "1.12+git${SRCPV}"
PRINC := "${@int(PRINC) + 2}"

DEPENDS += "libsamsung-ipc"
# NOTE: Needed for GPRS provisioning support
RDEPENDS_${PN}_append = " android-apn-database"

# NOTE: When samsung-ipc support is part of a ofono release this should go into
# meta-samsung layer as bbappend to oe-core.
RDEPENDS_${PN}_tuna = "samsung-rfs-mgr"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=webOS-ports/master \
  file://ofono \
  file://ofono.upstart"

do_install_append() {
    install -d ${D}${sysconfdir}/event.d
    install -m 0644 ${WORKDIR}/ofono.upstart ${D}${sysconfdir}/event.d/ofono
}

INITSCRIPT_PACKAGES = "${PN}-sysvinit"

PACKAGES =+ "${PN}-sysvinit ${PN}-upstart"
FILES_${PN}-sysvinit = "${sysconfdir}/init.d/ofono"
FILES_${PN}-upstart = "${sysconfdir}/event.d/ofono"
