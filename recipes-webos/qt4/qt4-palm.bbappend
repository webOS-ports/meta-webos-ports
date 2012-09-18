FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS_append_tuna = " virtual/egl"
