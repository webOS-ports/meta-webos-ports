# Copyright (c) 2019-2023 LG Electronics, Inc.

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

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib udev libwebosi18n libusb"
RDEPENDS:${PN} = "fuse-utils hdparm gphoto2 gphotofs sdparm gptfdisk-sgdisk e2fsprogs-e2fsck e2fsprogs-tune2fs ntfs-3g ntfs-3g-ntfsprogs dosfstools simple-mtpfs lsof smartmontools"

RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_pdm-plugin}"

WEBOS_VERSION = "1.0.1-82_76bdc781196e4a5d0cf01ccf8d59ea04d5ef9fe8"

PV = "1.0.1-82+git${SRCPV}"
SRCREV = "76bdc781196e4a5d0cf01ccf8d59ea04d5ef9fe8"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-NfcDeviceHandler-Fix-incorrect-name.patch \
    file://0002-com.webos.service.pdm-Remove-the-Android-Auto-bits.patch \
"

S = "${WORKDIR}/git"

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
