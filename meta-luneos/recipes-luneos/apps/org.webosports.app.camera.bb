SUMMARY = "Camera application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d \
"

PV = "0.0.2-1+git"
SRCREV = "0b4475028fbc7d3278fd10abcb5b7dc7e1f3038f"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native qtmultimedia"

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

FILES:${PN} += "${webos_applicationsdir}/org.webosports.app.camera"

# The droid camera now comes from LuneOS.Camera in luneos-components rather
# than a copy inside the app's own CameraApp plugin, so the QML module has to
# be on the device for the app to find a camera at all.
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    luneos-components \
"

