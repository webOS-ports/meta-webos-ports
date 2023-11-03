SUMMARY = "Waydroid uses a container-based approach to boot a full Android system"
DESCRIPTION = "Runtime for Android applications which runs a full Android system \
    in a container using Linux namespaces (user, ipc, net, mount) to \
    separate the Android system fully from the host."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SECTION = "webos/support"

SRCREV = "209c90d47330ddb357b5209309c80c4257f05fe3"
PV = "1.2.0+git"

RDEPENDS:${PN} += "waydroid-data lxc python3-gbinder python3-pygobject libgbinder python3-pyclip"

# these modules are directly included in android-flavored kernels
# Note: Waydroid requires kernel >= 3.18 !
RRECOMMENDS:${PN} += " \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
"

SRC_URI = "git://github.com/waydroid/waydroid.git;branch=main;protocol=https \
           file://0001-config_4-Disable-this-for-now-since-Waydroid-doesn-t.patch \
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

inherit pkgconfig

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "waydroid-container.service"

do_install:append() {
    install -dm755 "${D}/usr/lib/waydroid"
    install -dm755 "${D}/usr/share/applications"
    install -dm755 "${D}/usr/bin"
    cp -r ${S}/tools ${S}/data "${D}/usr/lib/waydroid/"
    cp "${D}/usr/lib/waydroid/data/Waydroid.desktop" "${D}/usr/share/applications"
    cp ${S}/waydroid.py "${D}/usr/lib/waydroid/"
    ln -s /usr/lib/waydroid/waydroid.py "${D}/usr/bin/waydroid"

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/systemd/waydroid-container.service ${D}${systemd_unitdir}/system/

    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${S}/dbus/id.waydro.Container.conf ${D}${sysconfdir}/dbus-1/system.d/
    install -m 0644 ${S}/dbus/id.waydro.Container.service ${D}${sysconfdir}/dbus-1/system.d/
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

FILES:${PN} += "${systemd_unitdir} ${sysconfdir}"

# Usage
# =====
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
