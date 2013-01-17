SRCREV = "89498c785c84cc45ec153bf2d4adf3d1c0500697"
PV = "1.12+git${SRCPV}"
PRINC := "${@int(PRINC) + 0}"

DEPENDS += "libsamsung-ipc"

# NOTE: When samsung-ipc support is part of a ofono release this should go into
# meta-samsung layer as bbappend to oe-core.
RDEPENDS_${PN}_tuna = "samsung-rfs-mgr"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=webOS-ports/master \
  file://ofono"
