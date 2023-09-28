# Copyright (c) 2018-2023 LG Electronics, Inc.

SUMMARY = "webOS text to speech service"
SECTION = "webos/base"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=eb0fefa4904ac8820261e985096d5ad4 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib json-c pulseaudio googleapis grpc"

WEBOS_VERSION = "1.0.0-28_06ceb4c6e4b9899e2c1760061a7130146ab5ae98"
PR = "r9"

PV = "1.0.0-28+git${SRCPV}"
SRCREV = "06ceb4c6e4b9899e2c1760061a7130146ab5ae98"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.tts.service"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/com.webos.service.tts.service
}

EXTRA_OECMAKE += "-DGOOGLEAPIS_PATH=${STAGING_INCDIR}/google"

FILES:${PN} += "${webos_sysbus_datadir}"
