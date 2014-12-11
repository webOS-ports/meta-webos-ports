# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS product preference data"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Should be kept in sync with ${PV} of luna-prefs
PV = "2.0.0-2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PRODUCT_DEVICE_NAME ?= "Lune OS Device"
PRODUCT_DEVICE_NAME_BRANDED ?= "Lune OS Device"
PRODUCT_DEVICE_NAME_SHORT ?= "Lune OS Device"
PRODUCT_DEVICE_NAME_SHORT_BRANDED ?= "Lune OS Device"
PRODUCT_DEVICE_NAME_PRODUCT_LINE_NAME ?= "Lune OS Device"
PRODUCT_DEVICE_NAME_PRODUCT_CLASS ?= "Tablet"
PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_NAME ?= "Lune OS"

# Versions can not be overridden
PRODUCT_DEVICE_NAME_PRODUCT_LINE_VERSION = "${WEBOS_DISTRO_API_VERSION}"
PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_VERSION = "${WEBOS_DISTRO_API_VERSION}"

do_install() {
    install -d ${D}${sysconfdir}/prefs/properties

    echo -n "${PRODUCT_DEVICE_NAME}"               > ${D}${sysconfdir}/prefs/properties/deviceName
    echo -n "${PRODUCT_DEVICE_NAME_BRANDED}"       > ${D}${sysconfdir}/prefs/properties/deviceNameBranded
    echo -n "${PRODUCT_DEVICE_NAME_SHORT}"         > ${D}${sysconfdir}/prefs/properties/deviceNameShort
    echo -n "${PRODUCT_DEVICE_NAME_SHORT_BRANDED}" > ${D}${sysconfdir}/prefs/properties/deviceNameShortBranded
    echo -n ${MACHINE} > ${D}${sysconfdir}/prefs/properties/machineName
    echo -n ${PRODUCT_DEVICE_NAME_PRODUCT_LINE_NAME} > ${D}${sysconfdir}/prefs/properties/productLineName
    echo -n ${PRODUCT_DEVICE_NAME_PRODUCT_LINE_VERSION} > ${D}${sysconfdir}/prefs/properties/productLineVersion
    echo -n ${PRODUCT_DEVICE_NAME_PRODUCT_CLASS} > ${D}${sysconfdir}/prefs/properties/productClass
    echo -n ${PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_NAME} > ${D}${sysconfdir}/prefs/properties/browserOsName
    if [ ${PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_NAME} == "webOS" ]
    then
        echo -n ${PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_VERSION} > ${D}${sysconfdir}/prefs/properties/browserOsVersion
    fi
}
