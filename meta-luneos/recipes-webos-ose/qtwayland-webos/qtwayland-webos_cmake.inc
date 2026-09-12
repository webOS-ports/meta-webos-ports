# Copyright (c) 2023-2024 LG Electronics, Inc.

inherit webos_cmake_qt6

FILES:${PN}-dev += " \
    ${libdir}/cmake/* \
    ${libdir}/metatypes/* \
    ${datadir}/modules/* \
"

# http://gpro.lge.com/c/webosose/qtwayland-webos/+/349070 Use QtWebOSEglClientBuffer for cmake]
# This one stays a patch rather than moving to a commit on the webOS-ports
# branch: it is genuinely CMake-only. The qmake build's
# webos-wayland-egl-support.pro declares TARGET = WebOSEglClientBuffer and
# syncqt.pl looks that exact name up in sync.profile, so renaming the key
# unconditionally breaks it with "No such module: WebOSEglClientBuffer".
SRC_URI:append = "\
    file://0001-Use-QtWebOSEglClientBuffer-for-cmake.patch \
"

PACKAGECONFIG[criu] = "-DFEATURE_criu=ON,-DFEATURE_criu=OFF,criu-webos"
PACKAGECONFIG[lttng] = "-DFEATURE_lttng=ON,-DFEATURE_lttng=OFF,"
