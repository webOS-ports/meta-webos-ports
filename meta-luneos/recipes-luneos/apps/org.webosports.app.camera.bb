SUMMARY = "Camera application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d \
"

PV = "0.0.2-1+git"
PR = "r1"
SRCREV = "1eb1f978d3ecf444e5f4e75c4889dc8db7d86466"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native qtmultimedia libexif"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths
inherit webos_tweaks

inherit qt6-cmake
inherit webos_cmake

INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.camera \
    ${datadir}/luna-service2/roles.d/org.webosports.app.camera.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.camera.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.camera.perm.json \
"

# The droid camera now comes from LuneOS.Camera in luneos-components rather
# than a copy inside the app's own CameraApp plugin, so the QML module has to
# be on the device for the app to find a camera at all.
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    luneos-components \
"


