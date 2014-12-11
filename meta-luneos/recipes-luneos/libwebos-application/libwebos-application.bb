SUMMARY = "libwebos-application implements lifecycle support for native applications."
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

DEPENDS += "luna-service2"

PV = "0.1.0-3+git${SRCPV}"
SRCREV = "da907470c061be078684508bc0a845ceba59a00d"

inherit webos_ports_repo
inherit webos_cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
