FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += " file://remove-dependency-on-libhid-and-fix-errors.patch"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS_append_tuna = " virtual/egl"
