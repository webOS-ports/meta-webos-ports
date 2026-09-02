SUMMARY = "Waydroid uses a container-based approach to boot a full Android system"
DESCRIPTION = "Runtime for Android applications which runs a full Android system \
    in a container using Linux namespaces (user, ipc, net, mount) to \
    separate the Android system fully from the host."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SECTION = "webos/support"

# Upstream, not a fork. The LuneOS additions - the install target, the launcher
# entry and the webOS application generation - are the patches below, so they
# are reviewable here rather than carried in a personal branch.
SRCREV = "5b7e2e71be3f6bfaaaab3b461251dacaf1ce4991"
SPV = "1.6.3"
PV = "${SPV}"
# Bumped whenever the shipped patches or helper scripts change: they alter what
# the package contains without moving SRCREV or PV, so without this an already
# installed waydroid stays at the previous build. Reset at the 1.6.3 move.
PR = "r2"

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
# mindphone has no room for them: its rootfs is a 2.4G loop image with a few
# hundred MB free, while /var lives on userdata with tens of GB. The images
# have to come from the OTA channel into /var/lib/waydroid/images, or be
# placed there and bind mounted over a preinstalled path.
WAYDROID_IMAGE_RDEPENDS:mindphone = ""
# halium-arm64 is a GSI machine: the vendor type is a property of whichever
# device booted the image, so the pair cannot be frozen at build time. Measured
# on sargo, which reports vndk 32 and therefore HALIUM_13: the channel serves a
# current lineage-20.0 pair for waydroid_arm64, so the OTA path is both correct
# and maintained here, unlike the frozen HALIUM_9 line.
WAYDROID_IMAGE_RDEPENDS:halium-arm64 = ""

RDEPENDS:${PN} += "${WAYDROID_IMAGE_RDEPENDS} lxc python3-gbinder python3-pygobject libgbinder python3-pyclip"

# these modules are directly included in android-flavored kernels
# Note: Waydroid requires kernel >= 3.18 !
RRECOMMENDS:${PN} += " \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
"

SRC_URI = "git://github.com/waydroid/waydroid.git;branch=main;protocol=https \
    file://0001-lxc-copy-host-permissions-on-non-Treble-hosts-too.patch \
    file://0002-Makefile-add-a-LuneOS-install-target.patch \
    file://0003-data-add-the-LuneOS-application-files.patch \
    file://0004-user_manager-give-every-Android-app-a-webOS-app.patch \
    file://waydroid-luneos-prepare.sh \
    file://waydroid-luneos-prepare.service \
    file://waydroid-luneos-session.sh \
    file://waydroid-luneos-session-env.sh \
    file://waydroid-luneos-app.sh \
    file://waydroid-luneos-session.service \
    file://waydroid-luneos.conf.mindphone \
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
COMPATIBLE_MACHINE:mindphone = "(.*)"
COMPATIBLE_MACHINE:halium-arm64 = "(.*)"

inherit pkgconfig
inherit webos_app
inherit webos_filesystem_paths
inherit webos_systemd

# waydroid-luneos-prepare recreates the host state Waydroid needs but does not
# create itself - binder nodes on a binderfs kernel, the image bind mount, host
# GPU HAL bridging - none of which survives a reboot. waydroid-luneos-session
# starts the per-user session against the LuneOS compositor; see the script for
# why that is interim.
# Deliberately no waydroid-init.service.
#
# Running "waydroid init" from a boot-time unit is wrong on a machine that
# fetches its images over the network: on a freshly flashed device there is no
# network yet, so the unit fails, and because waydroid-container.service was
# ordered After= it, the container then failed too - which is exactly the state
# tissot was found in. Initialising is a user action, and it now happens on
# demand from the launcher, which is also the only place that can wait for it.
WEBOS_SYSTEMD_SERVICE = "waydroid-container.service \
    waydroid-luneos-prepare.service waydroid-luneos-session.service"

CLEANBROKEN = "1"

EXTRA_OEMAKE = "SYSD_DIR=${systemd_system_unitdir} USE_NFTABLES="1" WAYDROID_VERSION=${SPV}"

do_install() {
    # oe_runmake, not make: EXTRA_OEMAKE carries WAYDROID_VERSION, which is what
    # replaces __VERSION__ in the launcher entry. Called as plain make it never
    # arrived, and every build shipped an app declaring version 0.0.0.
    oe_runmake install_luneos DESTDIR=${D}

    install -d ${D}${libexecdir}
    install -m 0755 ${UNPACKDIR}/waydroid-luneos-prepare.sh ${D}${libexecdir}/waydroid-luneos-prepare
    install -m 0755 ${UNPACKDIR}/waydroid-luneos-session.sh ${D}${libexecdir}/waydroid-luneos-session
    install -m 0644 ${UNPACKDIR}/waydroid-luneos-session-env.sh ${D}${libexecdir}/waydroid-luneos-session-env
    install -m 0755 ${UNPACKDIR}/waydroid-luneos-app.sh ${D}${libexecdir}/waydroid-luneos-app

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/waydroid-luneos-prepare.service ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/waydroid-luneos-session.service ${D}${systemd_system_unitdir}
}

# Device specifics for waydroid-luneos-prepare. Only machines that need one
# ship a file; the script no-ops on every step when it is absent.
do_install:append:mindphone() {
    install -d ${D}${sysconfdir}/default
    install -m 0644 ${UNPACKDIR}/waydroid-luneos.conf.mindphone ${D}${sysconfdir}/default/waydroid-luneos
}

FILES:${PN} += " \
    ${sysconfdir} \
    ${libdir} \
    ${datadir}/dbus-1 \
    ${datadir}/polkit-1 \
    ${prefix}/libexec \
    ${webos_applicationsdir}/Waydroid \
"

# Usage
# =====
# Below is obsolete since Waydroid can now just be started from Launcher, however it's good to keep for reference
#
# mkdir -p /run/luna-session/
# mount --bind /tmp/luna-session /run/luna-session/
# export XDG_RUNTIME_DIR=/run/luna-session
# export XDG_SESSION_TYPE=wayland
# -- /etc/gbinder.conf comes from libgbinder now, with the API level set per
#    machine through GBINDER_API_LEVEL
#
# Then:
# 0. waydroid init (just once, but needs network !)
# 1. either
#      waydroid show-full-ui
#    or
#      waydroid session start
#      waydroid app launch com.android.settings
