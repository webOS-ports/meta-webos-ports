SUMMARY = "Small library implementing Android suspend mechanism"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "1.0.0-2+git${SRCPV}"
SRCREV = "838ef4eafcc9bb5231584ca58ce9b636e1003712"

inherit webos_ports_repo
inherit pkgconfig
inherit cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
