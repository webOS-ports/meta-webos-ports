SUMMARY = "libwebos-application implements lifecycle support for native applications."
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

DEPENDS += "luna-service2"

PV = "0.1.0-3+git"
SRCREV = "e628103a9f032d263f0097f0c86f19b8f6867bbe"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
