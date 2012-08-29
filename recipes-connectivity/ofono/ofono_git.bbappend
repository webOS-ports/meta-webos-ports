SRCREV = "5df67b2ec663ddbf6b4e782a28399b87ec6d2880"
PV = "1.10+git${SRCPV}"
PRINC := "${@int(PRINC) + 1}"

DEPENDS += "libsamsung-ipc"

SRC_URI  = " \
  git://github.com/morphis/ofono.git;branch=morphis/samsung-ipc \
  file://ofono"
