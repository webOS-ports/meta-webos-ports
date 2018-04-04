# Copyright (c) 2015-2018 LG Electronics, Inc.

SUMMARY = "webOS LS2 security configuration"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRCREV = "761191b1ff6ce4465e62109f8c9a0d018108d435"

FILES_${PN} += "${webos_sysbus_datadir}"

# The security configuration data isn't needed to build other components => don't stage it.
sysroot_stage_dirs_append() {
    # ${to} is the 2nd parameter passed to sysroot_stage_dir(),
    # e.g. ${SYSROOT_DESTDIR} passed from sysroot_stage_all()
    rm -vrf ${to}${webos_sysbus_datadir}
}
