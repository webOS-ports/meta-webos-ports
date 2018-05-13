SUMMARY = "Android in a Box"
DESCRIPTION = "Runtime for Android applications which runs a full Android system\
 in a container using Linux namespaces (user, ipc, net, mount) to\
 separate the Android system fully from the host."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=f27defe1e96c2e1ecd4e0c9be8967949"

require anbox.inc

DEPENDS += "dbus-cpp libsdl2 libsdl2-image lxc glm protobuf protobuf-native gtest virtual/egl"

RDEPENDS_${PN}_append_qemux86-64 = " ashmem binder"

SRC_URI += " \
           file://0001-Fix-dependencies-for-LuneOS.patch \
           file://0002-Fix-native-binaries-build.patch \
	   file://0003-Fix-emugl-build.patch \
	   file://0004-Fix-build-with-Wayland-SDL2.patch \
	   file://0005-Fix-invalid-method-overload.patch \
	   file://0006-Exclude-GLX-translator-from-build.patch \
	   file://0007-Fix-build-on-LuneOS-with-libhybris.patch \
	   file://anbox-container-manager.service \
	   file://anbox-session-manager.service \
	   file://anbox-bridge.network \
	   file://anbox-bridge.netdev \
	   "
S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=debug"
EXTRA_OECMAKE += "-DWAYLAND_SUPPORT=1"
EXTRA_OECMAKE += "-DHOST_CMAKE_C_COMPILER='${BUILD_CC}'"
EXTRA_OECMAKE += "-DHOST_CMAKE_CXX_COMPILER='${BUILD_CXX}'"
EXTRA_OECMAKE += "-DHOST_CMAKE_CXX_LINK_FLAGS='${BUILD_LDFLAGS}'"

inherit webos_cmake
inherit pkgconfig

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "anbox-container-manager.service anbox-session-manager.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/anbox-container-manager.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/anbox-session-manager.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/network
    install -Dm 644 ${WORKDIR}/anbox-bridge.network ${D}${systemd_unitdir}/network/80-anbox-bridge.network
    install -Dm 644 ${WORKDIR}/anbox-bridge.netdev ${D}${systemd_unitdir}/network/80-anbox-bridge.netdev

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${S}/kernel/99-anbox.rules ${D}${sysconfdir}/udev/rules.d/99-anbox.rules
}

FILES_${PN} += " ${systemd_unitdir} ${sysconfdir}/udev"
