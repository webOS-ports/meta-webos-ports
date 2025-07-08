SUMMARY = "Android in a Box"
DESCRIPTION = "Runtime for Android applications which runs a full Android system \
    in a container using Linux namespaces (user, ipc, net, mount) to \
    separate the Android system fully from the host."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=f27defe1e96c2e1ecd4e0c9be8967949"

SECTION = "webos/support"

SRCREV_FORMAT = "main"
SRCREV_main = "9de4e87cdd05135e1c71e6eadb68bf82719cebdf"
SRCREV_cpu-features = "b9593c8b395318bb2bc42683a94f962564cc4664"
SRCREV_sdbus-cpp = "3b735bf1aad65277f56e65c828a22455cbaf5245"
PV = "3.0+git"

DEPENDS += "dbus-cpp libsdl2 libsdl2-image lxc glm protobuf protobuf-native gtest elfutils sdbus-c++-tools-native"

RDEPENDS:${PN} += "anbox-data"

# these modules are directly included in android-flavored kernels
# Note: Anbox requires kernel >= 3.18 !
RRECOMMENDS:${PN} += " \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
    kernel-module-squashfs \
"

SRC_URI = "git://github.com/anbox/anbox;name=main;branch=master;protocol=https \
    git://github.com/google/cpu_features.git;name=cpu-features;destsuffix=git/external/cpu_features;branch=main;protocol=https \
    git://github.com/Kistler-Group/sdbus-cpp.git;name=sdbus-cpp;destsuffix=git/external/sdbus-cpp;branch=master;protocol=https \
    file://0001-Fix-dependencies-for-LuneOS.patch \
    file://0002-Fix-native-binaries-build.patch \
    file://0003-Fix-emugl-build.patch \
    file://0004-external-pass-CMAKE_TOOLCHAIN_FILE.patch \
    file://0005-CMakeLists.txt-use-sdbus-c-from-native-build.patch \
    file://0006-Fix-build-with-gcc-11.patch \
    file://0001-Fix-build-with-glibc-2.34.patch \
    file://anbox-container-manager.service \
    file://anbox-session-manager.service \
    file://anbox-bridge.network \
    file://anbox-bridge.netdev \
"
S = "${WORKDIR}/git"

# Needs quite new kernel (probably >= 3.18) and from LuneOS supported machines
# only qemux86, qemux86-64 and rpi (later hammerhead-mainline) MACHINEs have it
# Unlink ashmem, binder drop qemux86 here, because anbox-data is available only
# for following 4 archs (x86-64, armv7a, armv7ve, aarch64)
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:qemux86-64 = "(.*)"
COMPATIBLE_MACHINE:rpi = "(.*)"

PACKAGECONFIG ??= "wayland"
PACKAGECONFIG[x11] = "-DENABLE_X11=ON,-DENABLE_X11=OFF,virtual/libx11"
PACKAGECONFIG[wayland] = "-DENABLE_WAYLAND=ON,-DENABLE_WAYLAND=OFF,virtual/egl"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=debug"
EXTRA_OECMAKE += "-DHOST_CMAKE_C_COMPILER='${BUILD_CC}'"
EXTRA_OECMAKE += "-DHOST_CMAKE_CXX_COMPILER='${BUILD_CXX}'"
EXTRA_OECMAKE += "-DHOST_CMAKE_CXX_LINK_FLAGS='${BUILD_LDFLAGS}'"

inherit webos_cmake
inherit pkgconfig

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "anbox-container-manager.service anbox-session-manager.service"

do_configure:prepend() {
    sed -i 's@^#!/usr/bin/env python2$@#!/usr/bin/env python3@g' ${S}/scripts/gen-emugl-entries.py
}

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/anbox-container-manager.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/anbox-session-manager.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/network
    install -Dm 644 ${WORKDIR}/anbox-bridge.network ${D}${systemd_unitdir}/network/80-anbox-bridge.network
    install -Dm 644 ${WORKDIR}/anbox-bridge.netdev ${D}${systemd_unitdir}/network/80-anbox-bridge.netdev
}

FILES:${PN} += "${systemd_unitdir}"
FILES:${PN}-dev += "${libdir}/backward/BackwardConfig.cmake"
