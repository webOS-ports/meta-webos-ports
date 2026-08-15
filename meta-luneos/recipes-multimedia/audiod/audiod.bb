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

WEBOS_VERSION = "1.0.0-78_127c6cd6c9247979b4ead42d9b8fc8b5c48b47a2"
PR = "r36"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_cmake
# CMakeLists.txt drives all of its dependency discovery through
# pkg_check_modules(), so pkg-config-native has to be in the build.
inherit pkgconfig
inherit webos_system_bus
inherit webos_machine_impl_dep
inherit gettext
inherit webos_lttng
inherit webos_public_repo

WEBOS_REPO_NAME = "audiod-pro"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-palmLegacyManager-serve-the-Palm-era-com.palm.au.patch \
    file://0002-Let-audiod-claim-the-legacy-com.palm.audio-bus-names.patch \
    file://0003-palmLegacyManager-route-calls-via-PulseAudio-card-pr.patch \
    file://0004-PulseAudioLink-tolerate-a-libpulse-simple-without-pa.patch \
    file://0005-moduleManager-fix-iterator-invalidation-in-removeModules.patch \
    file://0006-PulseAudioLink-report-play_sample-failures.patch \
    file://0007-systemsounds-fall-back-to-samples-without-the-ondemand-suffix.patch \
"
S = "${WORKDIR}/git"

# Which sound cards audiod looks for. Upstream's CMakeLists picks between an
# emulator config and files/config/audiod_internal_device_loading_open.json,
# whose card names are the OSE reference board's - b1, b2, Headphones. Those
# match nothing on a real device: audiod finds no card, so it creates no sink,
# so there is no device to route to and the machine is silent until somebody
# restarts audiod by hand. A machine that names its cards differently drops a
# replacement in files/<machine>/ and it overwrites the installed one.
#
# The card name is the ALSA one from /proc/asound/cards, and the sink names
# have to be those module-palm-policy knows (see webos-virtual-devices.pa).
SRC_URI:append:pinetab2 = " file://audiod_internal_device_loading.json"

do_install:append:pinetab2() {
    install -m 0644 ${UNPACKDIR}/audiod_internal_device_loading.json \
        ${D}${webos_sysconfdir}/audiod/audiod_internal_device_loading.json
}

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "audiod.service"

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
