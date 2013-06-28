# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "JavaScript loader for foundation frameworks and other loadable libraries"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag submissions/9
SRCREV = "c3d2053af70a9c8b2ee91a5fb3379fe717849c07"
PV = "1.0-9"
PR = "r5"

#inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
#inherit webos_cmake
inherit webos_arch_indep

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_frameworksdir}
    install -v -m 0644  ${S}/mojoloader.js ${D}${webos_frameworksdir}
}

FILES_${PN} += "${webos_frameworksdir}"
