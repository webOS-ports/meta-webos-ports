SRCREV = "a423f38a84e52111013df8365533e55f255a4e84"
PV = "1.10+git${SRCPV}"
PRINC := "${@int(PRINC) + 1}"

DEPENDS += "libsamsung-ipc"
DEFAULT_PREFERENCE = "-1"

SRC_URI  = " \
  git://github.com/morphis/ofono.git;branch=morphis/samsung-ipc \
  file://ofono"
