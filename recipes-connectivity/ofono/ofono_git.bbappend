SRCREV = "a892bfc9bb3afd10974dced5783f879d44cbb55d"
PV = "1.10+git${SRCPV}"
PRINC := "${@int(PRINC) + 1}"

DEPENDS += "libsamsung-ipc"

SRC_URI  = " \
  git://github.com/webOS-ports/ofono.git;protocol=git;branch=master \
  file://ofono"
