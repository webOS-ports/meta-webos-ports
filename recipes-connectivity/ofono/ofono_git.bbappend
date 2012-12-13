SRCREV = "058de0025d8a3e4fff9a77a24c109e688f12b3f2"
PV = "1.11+git${SRCPV}"
PRINC := "${@int(PRINC) + 2}"

DEPENDS += "libsamsung-ipc"

# NOTE: When samsung-ipc support is part of a ofono release this should go into
# meta-samsung layer as bbappend to oe-core.
RDEPENDS_${PN}_tuna = "samsung-modem-mgr"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=webOS-ports/master \
  file://ofono"
