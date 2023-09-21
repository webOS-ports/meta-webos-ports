# Copyright (c) 2012-2023 LG Electronics, Inc.

# WARNING: This recipe appears in the SIGGEN_EXCLUDERECIPES_ABISAFE list in
# conf/layer.conf which means that changes to it will not be used in the generation
# of sstate signatures. This is unlikely to cause problems, unless luna-prefs is
# changed to no longer use the files under /etc/prefs/properties to determine the
# properties its exposes and their values.

SUMMARY = "Preferences Manager data for ${MACHINE}"
AUTHOR = "Oleksandr Ivanov <oleksandr.ivanov@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.0.0"
PR = "r3"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PRODUCT_DEVICE_NAME ?= "LuneOS Device"
PRODUCT_DEVICE_NAME_BRANDED ?= "LuneOS Device"
PRODUCT_DEVICE_NAME_SHORT ?= "LuneOS Device"
PRODUCT_DEVICE_NAME_SHORT_BRANDED ?= "LuneOS Device"
PRODUCT_DEVICE_NAME_PRODUCT_LINE_NAME ?= "LuneOS Device"
PRODUCT_DEVICE_NAME_PRODUCT_CLASS ?= "Tablet"
PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_NAME ?= "LuneOS"

# Versions can not be overridden
PRODUCT_DEVICE_NAME_PRODUCT_LINE_VERSION = "${WEBOS_DISTRO_API_VERSION}"
PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_VERSION = "${WEBOS_DISTRO_API_VERSION}"

# Public properties that should be accessible for apps
PUBLIC_PROPERTIES ?= "com.palm.properties.nduid"

do_install() {
    install -d ${D}${sysconfdir}/prefs/properties

    echo -n "${PRODUCT_DEVICE_NAME}"                         > ${D}${sysconfdir}/prefs/properties/deviceName
    echo -n "${PRODUCT_DEVICE_NAME_BRANDED}"                 > ${D}${sysconfdir}/prefs/properties/deviceNameBranded
    echo -n "${PRODUCT_DEVICE_NAME_SHORT}"                   > ${D}${sysconfdir}/prefs/properties/deviceNameShort
    echo -n "${PRODUCT_DEVICE_NAME_SHORT_BRANDED}"           > ${D}${sysconfdir}/prefs/properties/deviceNameShortBranded
    echo -n "${MACHINE}"                                     > ${D}${sysconfdir}/prefs/properties/machineName
    echo -n "${PRODUCT_DEVICE_NAME_PRODUCT_LINE_NAME}"       > ${D}${sysconfdir}/prefs/properties/productLineName
    echo -n "${PRODUCT_DEVICE_NAME_PRODUCT_LINE_VERSION}"    > ${D}${sysconfdir}/prefs/properties/productLineVersion
    echo -n "${PRODUCT_DEVICE_NAME_PRODUCT_CLASS}"           > ${D}${sysconfdir}/prefs/properties/productClass
    echo -n "${PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_NAME}" > ${D}${sysconfdir}/prefs/properties/browserOsName
    if [ ${PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_NAME} == "webOS" ]
    then
        echo -n ${PRODUCT_DEVICE_NAME_PRODUCT_BROWSER_OS_VERSION} > ${D}${sysconfdir}/prefs/properties/browserOsVersion
    fi
	
    echo "${PUBLIC_PROPERTIES}"               > ${D}${sysconfdir}/prefs/public_properties
}
