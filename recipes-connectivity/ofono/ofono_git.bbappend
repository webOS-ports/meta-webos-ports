SRCREV = "11817cf623a2be4efcbbdfdff18ce8746ab4c4f3"
PV = "1.12+git${SRCPV}"
PRINC := "${@int(PRINC) + 1}"

DEPENDS += "libsamsung-ipc"
# NOTE: Needed for GPRS provisioning support
RDEPENDS_${PN} += "mobile-broadband-provider-info"

# NOTE: When samsung-ipc support is part of a ofono release this should go into
# meta-samsung layer as bbappend to oe-core.
RDEPENDS_${PN}_tuna = "samsung-rfs-mgr"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=webOS-ports/master \
  file://ofono"
