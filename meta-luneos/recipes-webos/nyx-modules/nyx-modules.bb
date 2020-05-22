# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "webOS portability layer - ${MACHINE}-specific modules"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0 luna-service2 openssl udev"
DEPENDS += "mtdev"

RDEPENDS_${PN} = "lsb-release gzip nyx-conf"

EXTRA_OECMAKE += "-DDISTRO_VERSION:STRING='${DISTRO_VERSION}' -DDISTRO_NAME:STRING='${DISTRO_NAME}${WEBOS_DISTRO_NAME_SUFFIX}' \
                  -DWEBOS_DISTRO_API_VERSION:STRING='${WEBOS_DISTRO_API_VERSION}' \
                  -DWEBOS_DISTRO_RELEASE_CODENAME:STRING='${WEBOS_DISTRO_RELEASE_CODENAME}' \
                  -DWEBOS_DISTRO_BUILD_ID:STRING='${WEBOS_DISTRO_BUILD_ID}'"

# Only pass in a value for the Manufacturing version if one is actually
# defined. Otherwise, let the CMake script provide the default value.
#
# Defining it to be the empty string will override the default used in
# the CMake script.
WEBOS_DISTRO_MANUFACTURING_VERSION ??= ""
EXTRA_OECMAKE += "${@ '-DWEBOS_DISTRO_MANUFACTURING_VERSION:STRING="${WEBOS_DISTRO_MANUFACTURING_VERSION}"' \
                  if d.getVar('WEBOS_DISTRO_MANUFACTURING_VERSION',True) != '' else ''}"

# NB. CMakeLists.txt arranges for the return value of the NYX_OS_INFO_WEBOS_PRERELEASE
# query to be "" when WEBOS_DISTRO_PRERELEASE is not defined on the command line.
WEBOS_DISTRO_PRERELEASE ??= ""
EXTRA_OECMAKE += "${@ '-DWEBOS_DISTRO_PRERELEASE:STRING="${WEBOS_DISTRO_PRERELEASE}"' \
                  if d.getVar('WEBOS_DISTRO_PRERELEASE',True) != '' else ''}"

# Currently always using the modules for the rockhopper core OS.
WEBOS_TARGET_CORE_OS = "rockhopper"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit webos_public_repo
inherit webos_ports_repo
inherit webos_cmake
inherit webos_machine_impl_dep
inherit webos_core_os_dep
inherit webos_nyx_module_provider

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRC_URI_append = " \
    file://${MACHINE}.cmake \
"

PV = "7.1.0-1+git${SRCPV}"
SRCREV = "9824fc77c362f7deaa3af3b140340714fb214991"

do_configure_prepend() {
    # Install additional machine specific nyx configuration before CMake is started
    if [ -f ${WORKDIR}/${MACHINE}.cmake ]
    then
        cp ${WORKDIR}/${MACHINE}.cmake ${S}/src/machine.cmake
    fi
}


PACKAGES += "${PN}-tests"
FILES_${PN} += "${libdir}/nyx/modules/*"
FILES_${PN} += "${systemd_system_unitdir}/*"
FILES_${PN}-tests += "${bindir}/nyx-test-ledcontroller"
