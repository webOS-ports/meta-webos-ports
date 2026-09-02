# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "webOS Audiod daemon and utilities"
AUTHOR = "Sushovan G <sushovan.g@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

# pulseaudio-module-palm-policy supplies pulse/module-palm-policy.h, the wire
# protocol audiod speaks over the socket that module serves. In webOS OSE these
# headers come from LG's PulseAudio fork; LuneOS builds the module standalone
# against upstream PulseAudio instead, so the header dependency is explicit.
DEPENDS = "glib-2.0 libpbnjson luna-service2 pmloglib luna-prefs boost pulseaudio pulseaudio-module-palm-policy"
# Deliberately not the bare "pulseaudio" package that the upstream recipe lists:
# it is empty in this configuration, and pulseaudio-server below is what actually
# carries the daemon.
RDEPENDS:${PN} = "\
    libasound \
    libasound-module-pcm-pulse \
    libpulsecore \
    pulseaudio-lib-cli \
    pulseaudio-lib-protocol-cli \
    pulseaudio-misc \
    pulseaudio-module-cli-protocol-tcp \
    pulseaudio-module-cli-protocol-unix \
    pulseaudio-server \
    pulseaudio-module-palm-policy \
"
# Halium machines drive the codec through the Android HAL, so their sink is
# named by module-droid-card and not by audiod. webos-system.pa aliases it to
# the pcm_output name audiod routes by, which needs this module present.
RDEPENDS:${PN} += "pulseaudio-module-remap-sink"

# Built from the webOS-ports fork rather than webosose/audiod-pro: the fifteen
# patches this recipe used to carry are commits on its webOS-ports/webOS-OSE
# branch, which also starts from the submission below.
#WEBOS_VERSION = "1.0.0-78_127c6cd6c9247979b4ead42d9b8fc8b5c48b47a2"
PV = "1.0.0-78+git"
SRCREV = "e7f74089c98a79ba7dc1e117b14dc8758ad42899"
PR = "r36"

inherit webos_component
inherit webos_cmake
# CMakeLists.txt drives all of its dependency discovery through
# pkg_check_modules(), so pkg-config-native has to be in the build.
inherit pkgconfig
inherit webos_system_bus
# Deliberately not webos_machine_impl_dep. That class exists to give a recipe the
# hardware/emulator override, and it sets PACKAGE_ARCH = MACHINE_ARCH to do it,
# which rebuilds audiod separately for every machine. audiod does not need that:
# the only thing the distinction reaches is WEBOS_SOC_TYPE, and despite the name
# that is not a SoC selector. It is used in exactly one place, audioRouter, to
# decide whether the device has a second display:
#
#   mMapSinkRoutingInfo["display1"] = outputRoutingInfo;
#   if (WEBOS_SOC_TYPE == "RPI4")
#       mMapSinkRoutingInfo["display2"] = outputRoutingInfo;
#
# and then whether stream categories are looked up by name or all forced onto
# display1. The stock policy configs do carry display2 entries, and the emulator
# branch folds them into display1 rather than leaving them on a display nothing
# uses - so the hardware branch is the better behaviour for a single-display
# device, and it is what every real machine was already getting.
#
# Pin it and let audiod build once per architecture instead of once per machine.
EXTRA_OECMAKE += "-DWEBOS_TARGET_MACHINE_IMPL=hardware"
inherit gettext
inherit webos_lttng
inherit webos_ports_ose_repo

WEBOS_REPO_NAME = "audiod-pro"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://audiod-after-pulseaudio.conf \
"

# The device configuration lives in audiod-conf, not here, so that changing a
# machine's audio setup does not rebuild audiod - the same split
# luna-surfacemanager-conf uses. audiod's own build installs a config naming the
# OSE reference board's cards, which matches nothing on any real device; drop it
# so the two packages do not both own the file and audiod-conf's is what lands.
do_install:append() {
    rm -f ${D}${webos_sysconfdir}/audiod/audiod_internal_device_loading.json
}

RDEPENDS:${PN} += "audiod-conf"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "audiod.service"

# audiod's own unit orders itself after ls-hubd only, so it races PulseAudio -
# see the drop-in for what that costs.
do_install:append() {
    install -d ${D}${systemd_unitdir}/system/audiod.service.d
    install -m 0644 ${UNPACKDIR}/audiod-after-pulseaudio.conf \
        ${D}${systemd_unitdir}/system/audiod.service.d/10-after-pulseaudio.conf
}

FILES:${PN} += "${systemd_unitdir}/system/audiod.service.d"

EXTRA_OECMAKE += "${@bb.utils.contains('WEBOS_LTTNG_ENABLED', '1', '-DWEBOS_LTTNG_ENABLED:BOOLEAN=True', '', d)}"

# Deliberately no -DAUDIOD_PALM_LEGACY here. In the audiod-pro generation this
# recipe was originally written against, that flag gated the Palm-era methods
# (hacSet/hacGet/museSet/CallStatusUpdate/bluetoothAudioPropertiesSet, mic gain
# and latency). In the revision pinned above it survives only in CMakeLists.txt
# and is referenced nowhere under src/, so passing it did nothing but suggest
# otherwise. The legacy com.palm.audio surface is provided instead by the
# palmLegacyManager module, loaded via audiod_module_config.json.
EXTRA_OECMAKE += "-DAUDIOD_TEST_API:BOOLEAN=True"

FILES:${PN} += "${datadir}/systemsounds"
