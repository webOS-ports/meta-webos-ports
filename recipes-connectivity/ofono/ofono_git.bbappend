SRCREV = "8a80684f22b379d8b9f637a84999c034c3923c91"
PV = "1.11+git${SRCPV}"
PRINC := "${@int(PRINC) + 2}"

DEPENDS += "libsamsung-ipc"

# NOTE: When samsung-ipc support is part of a ofono release this should go into
# meta-samsung layer as bbappend to oe-core.
RDEPENDS_${PN}_tuna = "samsung-rfs-mgr"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=webOS-ports/master \
  file://ofono"
