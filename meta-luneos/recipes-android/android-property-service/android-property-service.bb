SUMMARY = "Simple service to retrieve, set and get notified about android properties."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;beginline=86;endline=105;md5=649a1e756b7d4ea0e24d38c2d5a572ee"

DEPENDS += "luna-service2 libhybris libpbnjson luna-prefs virtual/android-headers"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

PV = "0.1.0-2+git${SRCPV}"
SRCREV = "5c0025d2ff1772175e31803763a2f4c5b46ea283"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
