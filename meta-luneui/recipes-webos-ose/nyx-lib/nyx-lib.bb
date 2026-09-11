# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "webOS portability layer - library"
AUTHOR = "Yogish S <yogish.s@lge.com>"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"
SECTION = "webos/libs"

# nyx-lib needs nyx-modules at runtime, but a runtime dependency is not defined
# here because nyx-modules is MACHINE_ARCH (e.g. qemux86), while nyx-lib is
# TUNE_PKGARCH  (e.g. i586). Instead, it is pulled into the image by adding it to
# the RDPENDS_${PN} of packagegroup-webos-extended. Putting
#   RDEPENDS:${PN} = "nyx-modules"
# here would cause bitbake to re-execute the do_package task for each MACHINE,
# even if these MACHINE-s were all i586 and should therefore share the same nyx-lib
# .ipk and sstate files. (The reason do_package is re-executed when a component
# in any of the R* variables is re-built is because its package name is stored in
# this component's .ipk and it may have changed because debian.bbclass is inherited.)

DEPENDS = "glib-2.0 pmloglib"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: the six patches that used
# to live in this directory - the asynchronous suspend/resume methods, the nyx
# systemd target, the led_controller RGB parameters, the nyx_gps debug and
# non-framework location notifications, and the multi-battery API - are commits
# on webOS-ports/webOS-OSE, along with the memory-safety work that branch adds
# on top of them (nyx_device NULL-iterator and truncation handling, nyx_file_io
# no longer reporting failed reads and writes as successes, the transposed
# calloc arguments in nyx_led_controller, and a thread-attribute leak).
#
# NOT yet on that branch: 0006-battery-carry-the-battery-s-condition-and-
# shipped-capacity, which is only on herrie/battery-health. Bump SRCREV once
# that PR is merged, otherwise the battery condition and shipped capacity are
# missing from this build.
#
# Pinned with a plain SRCREV - submission tags are a webosose convention and
# this branch carries none.
SRCREV = "c5d253eb0ac1800457b2df9b5d44cba2a5809b5b"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 7.3.0-13, so anything lower
# would look like a downgrade to opkg on an update.
PV = "7.3.0-14"

PR = "r14"

inherit webos_component
inherit webos_cmake
inherit webos_library
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "nyx.target"

inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=herrie/battery-health"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/${SYSTEMD_SERVICE:${PN}} ${D}${systemd_unitdir}/system/
}
