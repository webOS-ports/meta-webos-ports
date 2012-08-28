FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://remove-dependency-on-libhid-and-fix-errors.patch"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS_append_tuna = " virtual/egl"
