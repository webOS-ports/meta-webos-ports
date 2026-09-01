SUMMARY = "Waydroid uses a container-based approach to boot a full Android system"
DESCRIPTION = "Runtime for Android applications which runs a full Android system \
    in a container using Linux namespaces (user, ipc, net, mount) to \
    separate the Android system fully from the host."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SECTION = "webos/support"

SRCREV = "41f309f4c185a2c716723c081274eb56eb9263ff"
SPV = "1.4.2"
PV = "${SPV}+git"
# Bumped for the host-permissions patch added below: it changes what the
# package contains without moving SRCREV or PV, so without this an already
# installed waydroid stays at the unpatched build.
PR = "r1"

# Pre-installed images, for machines whose system/vendor pairing is frozen.
#
# Waydroid disables OTA once it finds images under
# /usr/share/waydroid-extra/images: it sets system_ota and vendor_ota to
# "None". That is what we want on tissot and mido, whose HALIUM_9 vendor line
# upstream stopped building, and the opposite of what a Treble target wants -
# there the vendor type is a property of whichever device booted the image,
# known only at runtime, so the images have to come from the OTA channel.
#
# Caveat for the 1.4.2 pinned above, measured on tissot: 1.4.2 still contacts
# the channel first and only falls back to "None" after the request fails, and
# http.retrieve() catches just ValueError and HTTPError - a DNS failure raises
# URLError and aborts "waydroid init". So on 1.4.2 a pre-installed machine
# still needs working DNS to initialise. 1.6.3 returns before any network call,
# which is one more reason to land that rebase.
#
# Machines that should resolve their own images set this empty.
WAYDROID_IMAGE_RDEPENDS ?= "waydroid-data"

RDEPENDS:${PN} += "${WAYDROID_IMAGE_RDEPENDS} lxc python3-gbinder python3-pygobject libgbinder python3-pyclip"

# these modules are directly included in android-flavored kernels
# Note: Waydroid requires kernel >= 3.18 !
RRECOMMENDS:${PN} += " \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
"

SRC_URI = "git://github.com/herrie82/waydroid.git;branch=herrie/luneos;protocol=https \
    file://gbinder.conf \
    file://0001-lxc-copy-host-permissions-on-non-Treble-hosts-too.patch \
"

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
COMPATIBLE_MACHINE:tissot-halium = "(.*)"

inherit pkgconfig
inherit webos_app
inherit webos_filesystem_paths
inherit webos_systemd

WEBOS_SYSTEMD_SERVICE = "waydroid-init.service waydroid-container.service"

CLEANBROKEN = "1"

EXTRA_OEMAKE = "SYSD_DIR=${systemd_system_unitdir} USE_NFTABLES="1" WAYDROID_VERSION=${SPV}"

do_install() {
    make install_luneos DESTDIR=${D}
}

# Provided by libgbinder already for Halium devices, but necessary to add for non-Halium devices.

do_install:append:pinephone() {
    install -Dm644 -t "${D}${sysconfdir}" "${UNPACKDIR}/gbinder.conf"
}

do_install:append:pinephonepro() {
    install -Dm644 -t "${D}${sysconfdir}" "${UNPACKDIR}/gbinder.conf"
}

do_install:append:pinetab2() {
    install -Dm644 -t "${D}${sysconfdir}" "${UNPACKDIR}/gbinder.conf"
}

do_install:append:qemux86-64() {
    install -Dm644 -t "${D}${sysconfdir}" "${UNPACKDIR}/gbinder.conf"
}

FILES:${PN} += " \
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
