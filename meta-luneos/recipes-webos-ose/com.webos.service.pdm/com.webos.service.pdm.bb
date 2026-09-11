# Copyright (c) 2019-2025 LG Electronics, Inc.

SUMMARY = "Physical Device Manager handles physical devices using netlink events"
DESCRIPTION = "Service for detecting and managing physical devices using netlink events. A physical device is a USB device, available internal storage device and so on."
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

VIRTUAL-RUNTIME_pdm-plugin ?= "pdm-plugin"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib udev libwebosi18n libusb gtest"
RDEPENDS:${PN} = "fuse-utils hdparm gphoto2 gphotofs sdparm gptfdisk-sgdisk e2fsprogs-e2fsck e2fsprogs-tune2fs ntfs-3g ntfs-3g-ntfsprogs dosfstools simple-mtpfs lsof smartmontools"

RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_pdm-plugin}"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0005 patch series plus the CMake 4 @VAR@ fix in this directory now lives
# as commits there, so nothing is applied here any more. Pinned with a plain
# SRCREV - submission tags are a webosose convention and this branch carries
# none. The branch itself comes from webos_ports_ose_repo below.
SRCREV = "a2fb14023ca31d727be34a1e33113911cb50e851"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 1.0.1-87, so anything lower
# would look like a downgrade to opkg on an update.
PV = "1.0.1-88"

PR = "r14"

inherit webos_component
inherit webos_cmake
inherit webos_system_bus
inherit webos_daemon
# The audit and test-harness work lives on herrie/fixes, not on the branch
# webos_ports_ose_repo defaults to, so the SRCREV below is not reachable from
# webOS-ports/webOS-OSE. Drop this line once herrie/fixes is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo
inherit webos_localizable
inherit useradd

USERADD_PARAM:${PN} = "-g pdmgroup -d /home/pdmuser -m -s /bin/sh pdmuser"
GROUPADD_PARAM:${PN} = "-g 2023 pdmgroup"
USERADD_PACKAGES = "${PN}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "physical-device-manager.service"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm -vf ${D}${sysconfdir}/systemd/system/physical-device-manager.service
}

FILES:${PN} += "${datadir}"

# webos doesn't have localization data for this recipe
WEBOS_LOCALIZATION_INSTALL_RESOURCES = "false"

# Build and package the gtest suite under tests/. INSTALL_TESTS implies
# BUILD_TESTS; without either, tests/CMakeLists.txt returns immediately and
# gtest is only a build-time dependency. Same arrangement as sam.
EXTRA_OECMAKE += "-DWEBOS_CONFIG_INSTALL_TESTS:BOOL=TRUE"

PACKAGES =+ "${PN}-tests"
ALLOW_EMPTY:${PN}-tests = "1"
FILES:${PN}-tests = "${webos_testsdir}/* ${libexecdir}/tests/*"
