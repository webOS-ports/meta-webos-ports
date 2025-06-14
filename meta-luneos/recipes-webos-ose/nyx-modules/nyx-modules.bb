# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "webOS portability layer - ${MACHINE}-specific modules"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "nyx-lib glib-2.0 luna-service2 openssl udev nmeaparser"
#LuneOS added for mtdev modules such as touchscreen
DEPENDS += "mtdev"

RDEPENDS:${PN} = "lsb-release gzip"
#LuneOS uses config per device provided by nyx-conf
RDEPENDS:${PN} += "nyx-conf"

WEBOS_VERSION = "7.1.0-25_802df9c1da7fb70c9d7506d4b863cd858153a1b1"

PR = "r22"

EXTRA_OECMAKE += "\
    -DDISTRO_VERSION:STRING='${DISTRO_VERSION}' \
    -DDISTRO_NAME:STRING='${DISTRO_NAME}${WEBOS_DISTRO_NAME_SUFFIX}' \
    -DWEBOS_DISTRO_RELEASE_PLATFORMCODE:STRING='${WEBOS_DISTRO_RELEASE_PLATFORMCODE}' \
    -DWEBOS_DISTRO_RELEASE_CODENAME:STRING='${WEBOS_DISTRO_RELEASE_CODENAME}' \
    -DWEBOS_DISTRO_BUILD_ID:STRING='${WEBOS_DISTRO_BUILD_ID}' \
"

# Only pass in a value for the Manufacturing version if one is actually
# defined. Otherwise, let the CMake script provide the default value.
#
# Defining it to be the empty string will override the default used in
# the CMake script.
WEBOS_DISTRO_MANUFACTURING_VERSION ??= ""
EXTRA_OECMAKE += "${@ '-DWEBOS_DISTRO_MANUFACTURING_VERSION:STRING="${WEBOS_DISTRO_MANUFACTURING_VERSION}"' \
                  if d.getVar('WEBOS_DISTRO_MANUFACTURING_VERSION') != '' else ''}"

# NB. CMakeLists.txt arranges for the return value of the NYX_OS_INFO_WEBOS_PRERELEASE
# query to be "" when WEBOS_DISTRO_PRERELEASE is not defined on the command line.
EXTRA_OECMAKE += "${@ '-DWEBOS_DISTRO_PRERELEASE:STRING="${WEBOS_DISTRO_PRERELEASE}"' \
                  if d.getVar('WEBOS_DISTRO_PRERELEASE') != '' else ''}"

# Currently always using the modules for the rockhopper core OS.
WEBOS_TARGET_CORE_OS = "rockhopper"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library
inherit webos_machine_impl_dep
#inherit webos_prerelease_dep
inherit webos_core_os_dep
inherit webos_nyx_module_provider
#inherit webos_distro_variant_dep

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-nyx-modules-Add-ALS.patch \
    file://0002-nyx-modules-Add-haptics-module.patch \
    file://0003-nyx-modules-Add-keys-module.patch \
    file://0004-nyx-modules-Add-LED-module.patch \
    file://0005-nyx-modules-Add-MSM-MTP-module.patch \
    file://0006-nyx-modules-Add-touchpanel-mtdev-modules.patch \
    file://0007-msgid-Add-messages-for-LuneOS-modules.patch \
    file://0008-Add-LuneOS-modules-and-machine-specific-cmake-file-t.patch \
    file://0009-Add-wait-touchscreen-conf.patch \
    file://0010-nyx-modules-Use-etc-machine-id-for-serial-number.patch \
"
S = "${WORKDIR}/git"

SRC_URI:append = " \
    file://${MACHINE}.cmake \
"

do_configure:prepend() {
    # Install additional machine specific nyx configuration before CMake is started
    if [ -f ${WORKDIR}/${MACHINE}.cmake ]
    then
        cp ${WORKDIR}/${MACHINE}.cmake ${S}/src/machine.cmake
    fi
}

do_install:append:tenderloin-halium() {
    install -d ${D}${systemd_system_unitdir}/nyx.target.d/
    install -m 0644 ${S}/files/systemd/nyx.target.d/wait-touchscreen.conf ${D}${systemd_system_unitdir}/nyx.target.d/
}

PACKAGES += "${PN}-tests"
FILES:${PN} += "${libdir}/nyx/modules/*"
FILES:${PN} += "${systemd_system_unitdir}/*"
FILES:${PN}-tests += "${bindir}/nyx-test-ledcontroller"
