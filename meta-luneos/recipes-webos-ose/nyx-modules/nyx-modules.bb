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

# Built from the webOS-ports fork (webosose master + LuneOS modules and fixes
# merged as commits) rather than webosose plus a patch stack. Pinned with a
# plain SRCREV: submission tags are a webosose convention and this branch
# carries none.
#
# The charger, ALS and suspend work sits on herrie/charger-resync, not on the
# webOS-ports/webOS-OSE that webos_ports_ose_repo defaults to. Set through
# WEBOS_GIT_PARAM_BRANCH rather than appended to SRC_URI: the class already
# puts a ";branch=" in WEBOS_PORTS_GIT_REPO_COMPLETE, so appending a second one
# left the URL carrying two, and which one the fetcher honours is an accident
# of how it parses parameters.
WEBOS_GIT_PARAM_BRANCH = "herrie/charger-resync"
SRCREV = "5b553fe15da24c04adeeef0fb88f937188830602"

PR = "r41"

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
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_library
inherit webos_machine_impl_dep
#inherit webos_prerelease_dep
inherit webos_core_os_dep
inherit webos_nyx_module_provider
#inherit webos_distro_variant_dep

# Which modules this provider owns, per machine. Shared with nyx-modules-hybris,
# which has to agree with us: these flags pick a provider, not a build.
require recipes-webos-ose/nyx-modules/nyx-modules-machines.inc

# Set outright rather than derived from a submission tag. Kept monotonic: the
# patch-stack recipe shipped 7.1.0-25, so anything lower would look like a
# downgrade to opkg on an update.
PV = "7.1.0-26"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install:append:tenderloin-halium() {
    install -d ${D}${systemd_system_unitdir}/nyx.target.d/
    install -m 0644 ${S}/files/systemd/nyx.target.d/wait-touchscreen.conf ${D}${systemd_system_unitdir}/nyx.target.d/
}

PACKAGES += "${PN}-tests"
FILES:${PN} += "${libdir}/nyx/modules/*"
FILES:${PN} += "${systemd_system_unitdir}/*"
FILES:${PN}-tests += "${bindir}/nyx-test-ledcontroller"
FILES:${PN}-tests += "${bindir}/nyx-test-led"
