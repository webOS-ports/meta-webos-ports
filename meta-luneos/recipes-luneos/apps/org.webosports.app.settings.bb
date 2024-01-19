SUMMARY = "Settings app written from scratch for LuneOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_app

PV = "0.3.0-1+git${SRCPV}"
SRCREV = "79905851f05d0915578b31149c5c44103a8742cd"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# We don't provide android-property-service on non-Android devices, so remove it in these cases to avoid LS2 warnings/errors

REMOVE_ANDROID_PROPERTY_SERVICE_CMD:halium = ""
REMOVE_ANDROID_PROPERTY_SERVICE_CMD = "sed -i 's/\"android-property-service.operation\", //g' ${D}/${webos_applicationsdir}/org.webosports.app.settings/appinfo.json"

do_install:append() {
    ${REMOVE_ANDROID_PROPERTY_SERVICE_CMD}
}
