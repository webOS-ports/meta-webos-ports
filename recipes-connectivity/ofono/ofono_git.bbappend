SRCREV = "231a767abffb6ba5419bdbe6b5f3b23c9de8707f"
PV = "1.10+git${SRCPV}"
PRINC := "${@int(PRINC) + 1}"

DEPENDS += "libsamsung-ipc"

SRC_URI  = " \
  git://github.com/morphis/ofono.git;branch=morphis/samsung-ipc \
  file://ofono"
