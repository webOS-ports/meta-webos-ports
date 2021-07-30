# Copyright (c) 2013 LG Electronics, Inc.

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://fix-lsb_release-to-work-if-there-are-parens-in-release-codename.patch \
"

WEBOS_TARGET_CORE_OS ?= "undefined"
BUILD_INFO_FILE = "${DISTRO}-release"
BUILD_DISTRIB_ID = "${@'${WEBOS_TARGET_CORE_OS}'.capitalize()}"
BUILD_TREE = "${WEBOS_DISTRO_TOPDIR_BRANCH}"

# this might be needed by lsb_release (parent recipe says so), but lsbinitscripts conflict with our initscripts
RDEPENDS:${PN}:remove = "lsbinitscripts"

do_install:append() {
    # Remove lsb-release file and directory created by parent recipe.
    rm -f ${D}${sysconfdir}/lsb-release
    rm -rf ${D}${sysconfdir}/lsb-release.d

    echo "${BUILD_DISTRIB_ID} release ${DISTRO_VERSION}-${BUILD_TREE}-${WEBOS_DISTRO_BUILD_ID} (${WEBOS_DISTRO_RELEASE_CODENAME})" > ${D}${sysconfdir}/${BUILD_INFO_FILE}
}
