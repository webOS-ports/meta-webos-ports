SUMMARY = "Waydroid uses a container-based approach to boot a full Android system"
DESCRIPTION = "Runtime for Android applications which runs a full Android system \
    in a container using Linux namespaces (user, ipc, net, mount) to \
    separate the Android system fully from the host."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SECTION = "webos/support"

SRCREV = "957f8b12f46fcf054124ba8baa9f36ec6ce10d23"
SPV = "1.4.2"
PV = "${SPV}+git${SRCPV}"


RDEPENDS:${PN} += "waydroid-data lxc python3-gbinder python3-pygobject libgbinder python3-pyclip"

# these modules are directly included in android-flavored kernels
# Note: Waydroid requires kernel >= 3.18 !
RRECOMMENDS:${PN} += " \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
"

SRC_URI = "git://github.com/herrie82/waydroid.git;branch=herrie/luneos;protocol=https \
    file://0001-Makefile-use-regular-SYSD_DIR-for-LuneOS-as-well.patch \
    file://gbinder.conf \
"
S = "${WORKDIR}/git"

# Needs quite new kernel (probably >= 3.18) and from LuneOS supported machines
# only qemux86, qemux86-64, rpi, Pine64 and other mainline) MACHINEs have it
# Unlink ashmem, binder drop qemux86 here, because waydroid-data is available only
# for following 4 archs (x86-64, armv7a, armv7ve, aarch64)
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:qemux86-64 = "(.*)"
COMPATIBLE_MACHINE:rpi = "(.*)"
COMPATIBLE_MACHINE:pinephone = "(.*)"
COMPATIBLE_MACHINE:pinephonepro = "(.*)"
COMPATIBLE_MACHINE:pinetab2 = "(.*)"
COMPATIBLE_MACHINE:mido-halium = "(.*)"
COMPATIBLE_MACHINE:tissot = "(.*)"

inherit pkgconfig
inherit webos_app
inherit webos_filesystem_paths

CLEANBROKEN = "1"

EXTRA_OEMAKE = "SYSD_DIR=${systemd_system_unitdir} USE_NFTABLES="1" WAYDROID_VERSION=${SPV}"

do_install() {
    make install_luneos DESTDIR=${D}
}

# Provided by libgbinder already for Halium devices, but necessary to add for non-Halium devices.

do_install:append:pinephone() {
    install -Dm644 -t "${D}${sysconfdir}" "${WORKDIR}/gbinder.conf" 
}

do_install:append:pinephonepro() {
    install -Dm644 -t "${D}${sysconfdir}" "${WORKDIR}/gbinder.conf" 
}

do_install:append:pinetab2() {
    install -Dm644 -t "${D}${sysconfdir}" "${WORKDIR}/gbinder.conf" 
}

do_install:append:qemux86-64() {
    install -Dm644 -t "${D}${sysconfdir}" "${WORKDIR}/gbinder.conf" 
}

FILES:${PN} += " \
    ${systemd_system_unitdir} \
    ${sysconfdir} \
    ${libdir} \
    ${datadir}/dbus-1 \
    ${datadir}/polkit-1 \
    ${prefix}/libexec \
    ${webos_applicationsdir}/id.waydro.container \
"

# Usage
# =====
# Below is obsolete since Waydroid can now just be started from Launcher, however it's good to keep for reference
#
# mkdir -p /run/luna-session/
# mount --bind /tmp/luna-session /run/luna-session/
# export XDG_RUNTIME_DIR=/run/luna-session
# export XDG_SESSION_TYPE=wayland
# -- also, make sure /etc/gbinder.conf has "ApiLevel = 30" (Halium 9 needs API 28)
#
# Then:
# 0. waydroid init (just once, but needs network !)
# 1. either
#      waydroid show-full-ui
#    or
#      waydroid session start
#      waydroid app launch com.android.settings
