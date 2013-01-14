SRCREV = "93038e632d296d63266ff59f0ea2823704950f8b"
PV = "1.12+git${SRCPV}"
PRINC := "${@int(PRINC) + 0}"

DEPENDS += "libsamsung-ipc"

# NOTE: When samsung-ipc support is part of a ofono release this should go into
# meta-samsung layer as bbappend to oe-core.
RDEPENDS_${PN}_tuna = "samsung-rfs-mgr"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=webOS-ports/master \
  file://ofono"
