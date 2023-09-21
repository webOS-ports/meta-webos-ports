# Copyright (c) 2015-2023 LG Electronics, Inc.

SUMMARY = "webOS LS2 security configuration"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

WEBOS_VERSION = "1.0.2-24_408d29b850749c79ecd6356ed8917df53116b4b3"
PR = "r6"

PV = "1.0.2-24+git${SRCPV}"
SRCREV = "408d29b850749c79ecd6356ed8917df53116b4b3"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += "${webos_sysbus_datadir}"

# The security configuration data isn't needed to build other components => don't stage it.
sysroot_stage_dirs:append() {
    # ${to} is the 2nd parameter passed to sysroot_stage_dir(),
    # e.g. ${SYSROOT_DESTDIR} passed from sysroot_stage_all()
    rm -vrf ${to}${webos_sysbus_datadir}
}
