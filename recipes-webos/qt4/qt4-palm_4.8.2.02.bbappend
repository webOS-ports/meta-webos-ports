FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://remove-dependency-on-libhid-and-fix-errors.patch"
RECIPE_PR = ".1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS_append_tuna = " virtual/egl"
