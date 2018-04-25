# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "webOS portability layer - library"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "webos/libs"

# nyx-lib needs nyx-modules at runtime, but a runtime dependency is not defined
# here because nyx-modules is MACHINE_ARCH (e.g. qemux86), while nyx-lib is
# TUNE_PKGARCH  (e.g. i586). Instead, it is pulled into the image by adding it to
# the RDPENDS_${PN} of packagegroup-webos-extended. Putting
#   RDEPENDS_${PN} = "nyx-modules"
# here would cause bitbake to re-execute the do_package task for each MACHINE,
# even if these MACHINE-s were all i586 and should therefore share the same nyx-lib
# .ipk and sstate files. (The reason do_package is re-executed when a component
# in any of the R* variables is re-built is because its package name is stored in
# this component's .ipk and it may have changed because debian.bbclass is inherited.)

PV = "7.3.0-1+git${SRCPV}"

DEPENDS = "glib-2.0 pmloglib"

inherit webos_public_repo
inherit webos_cmake
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "nyx.target"

SRC_URI = "git://github.com/herrie82/nyx-lib.git;branch=herrie/webosose"
S = "${WORKDIR}/git"

SRCREV = "097ecf2cfefd2c6e73c516accd3b99b90154c467"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/${SYSTEMD_SERVICE_${PN}} ${D}${systemd_unitdir}/system/
}
