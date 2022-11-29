# Copyright (c) 2012-2019 LG Electronics, Inc.

SUMMARY = "webOS logging daemon"
AUTHOR = "Gayathri Srinivasan <gayathri.srinivasan@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib zlib glib-2.0 libpbnjson pmloglib-private luna-service2"
# show_disk_usage.sh script uses mktemp, find, xargs, and du, all of which are
# provided by busybox.
RDEPENDS:${PN} = "busybox"

PV = "3.1.0-13+git${SRCPV}"
SRCREV = "0092c55c97e428fa187b6fa8a760da85f3d40410"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_pmlog_config
inherit pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[whitelist] = "-DENABLE_WHITELIST:BOOL=TRUE, -DENABLE_WHITELIST:BOOL=FALSE"


SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Make-PmLogDaemon-compatible-with-systemd.patch \
    ${@bb.utils.contains('PACKAGECONFIG', 'whitelist', 'file://whitelist.txt', '', d)} \
"
S = "${WORKDIR}/git"

do_install:append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'whitelist', 'true', 'false', d)} ; then
        install -m 644 ${WORKDIR}/whitelist.txt ${D}${sysconfdir}/PmLogDaemon
    fi
}
FILES:${PN} += "${datadir}/PmLogDaemon"
