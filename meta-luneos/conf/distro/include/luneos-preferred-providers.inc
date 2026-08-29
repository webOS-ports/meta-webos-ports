# Copyright (c) 2012-2023 LG Electronics, Inc.

# Place distro-wide PREFERRED_PROVIDER settings here; place MACHINE-specific
# settings in <MACHINE>.conf .

# Empty will use default gcc or you can use -clang"
WEBRUNTIME_TOOLCHAIN = "-clang"

SKIP_RECIPE[jsoncpp-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[libpbnjson-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[media-resource-calculator-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[umediaserver-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[wam-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[googletest-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[media-codec-interface-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"
SKIP_RECIPE[g-media-pipeline-clang] ?= "${@oe.utils.conditional('WEBRUNTIME_TOOLCHAIN', '-clang', '', 'WEBRUNTIME_TOOLCHAIN is not set to -clang', d)}"

# oe-core now ships llvm and clang as separate recipes (clang DEPENDS on
# llvm-native), so clang must no longer be forced as the llvm provider:
# doing so makes clang-native depend on itself.
PROVIDES:pn-nativesdk-clang = "nativesdk-llvm"

PREFERRED_PROVIDER_virtual/webruntime = "webruntime${WEBRUNTIME_TOOLCHAIN}"
VIRTUAL-RUNTIME_webruntime = "webruntime${WEBRUNTIME_TOOLCHAIN}"

# Use bash instead of Busybox in LuneOS since we don't have CONFIG_BASH_IS_ASH=y enabled in Busybox defconfig
WEBOS_PREFERRED_PROVIDER_FOR_BASH = "bash"
PREFERRED_PROVIDER_bash = "${@oe.utils.conditional('WEBOS_PREFERRED_PROVIDER_FOR_BASH', 'busybox', '${MLPREFIX}busybox', '${MLPREFIX}bash', d)}"
VIRTUAL-RUNTIME_bash = "${@oe.utils.conditional('WEBOS_PREFERRED_PROVIDER_FOR_BASH', 'busybox', '${MLPREFIX}busybox', '${MLPREFIX}bash', d)}"

# Use the busybox Tar instead of GNU Tar
WEBOS_PREFERRED_PROVIDER_FOR_TAR = "busybox"
PREFERRED_PROVIDER_tar = "${@oe.utils.conditional('WEBOS_PREFERRED_PROVIDER_FOR_TAR', 'busybox', 'busybox', 'tar', d)}"
VIRTUAL-RUNTIME_tar = "${@oe.utils.conditional('WEBOS_PREFERRED_PROVIDER_FOR_TAR', 'busybox', 'busybox', 'tar', d)}"

# Prefer libjpeg-turbo from meta-oe as it provides SIMD optimizations
PREFERRED_PROVIDER_jpeg = "libjpeg-turbo"
PREFERRED_PROVIDER_jpeg-native = "libjpeg-turbo-native"

# Just for documentation; these values are the defaults set in the recipes that
# use them.
VIRTUAL-RUNTIME_cpushareholder = "cpushareholder-stub"

PREFERRED_PROVIDER_stat = "busybox"
VIRTUAL-RUNTIME_stat = "busybox"

# Used for testing IME Manager from webOS OSE instead of webos-keyboard
VIRTUAL-RUNTIME_webos-ime = "webos-keyboard"
# VIRTUAL-RUNTIME_webos-ime = "imemanager"

# Which audio control service ships. Both claim com.palm.audio and
# org.webosports.service.audio, so exactly one of them can be installed.
#
#   packagegroup-luneos-audiod - audiod (webOS OSE) plus module-palm-policy and
#                                palmLegacyManager, which serves the Palm-era
#                                com.palm.audio API that Enyo 1.0's
#                                com.palm.app.phone and the enyo framework use.
#   audio-service              - the previous LuneOS service. Thin libpulse
#                                wrapper: master volume, mute and setCallMode
#                                only, no /phone, /ringtone, /dtmf or /media.
#
# Set to audio-service in a machine conf to fall back while bringing a device up.
VIRTUAL-RUNTIME_audio_service ?= "packagegroup-luneos-audiod"

VIRTUAL-RUNTIME_bluetooth_service ?= "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'com.webos.service.bluetooth2', '', d)}"

# Options are: bluez4, bluez5. This will only be used if "bluetooth" appears in
# DISTRO_FEATURES.
VIRTUAL-RUNTIME_bluetooth_stack = "bluez5"
BLUETOOTH_SIL_NAME = "bluez5"

BLUEZ = "${VIRTUAL-RUNTIME_bluetooth_stack}"
BLUEZ:class-native = "bluez5"
BLUEZ:class-nativesdk = "bluez5"

# Just for documentation; these values are the defaults set in the recipes that
# use them.
VIRTUAL-RUNTIME_cpushareholder = "cpushareholder-stub"

# Atlas is the default browser; enactbrowser remains available as an alternative provider
VIRTUAL-RUNTIME_com.webos.app.browser ?= "org.webosports.app.atlas"
VIRTUAL-RUNTIME_webappmanager = "wam${WEBRUNTIME_TOOLCHAIN}"

VIRTUAL-RUNTIME_initscripts = "webos-initscripts"
PREFERRED_RPROVIDER_initd-functions = "webos-initscripts"

# Don't install big /boot/vmlinux* file by default in qemux86 images (included in oprofile's RRECOMMENDS)
# Use --no-vmlinux option to opcontrol or install it manually if you need to see samples from kernel
BAD_RECOMMENDATIONS:append:qemux86 = " kernel-vmlinux"

# used by meta-networking/recipes-connectivity/autossh/autossh_1.4g.bb
PREFERRED_RPROVIDER_ssh = "openssh"

# systemd-compat-units was removed from oe-core (d9ec9e20ee "systemd: Stop
# supporting sysvinit compatibility")
VIRTUAL-RUNTIME_init_manager = "systemd systemd-machine-units"

# node-red can no longer be built: bitbake disabled the npmsw fetcher
# ("security issues and no maintainer"), which node-red's shrinkwrap
# SRC_URI depends on. Drop it from the image until the recipe is
# reworked to fetch another way.
VIRTUAL-RUNTIME_nodejs-module-node-red = ""

# With upstart we don't need update-rc.d, as bonus fixes following avahi issue for us:
# http://lists.openembedded.org/pipermail/openembedded-core/2013-November/086901.html
BAD_RECOMMENDATIONS += "update-rc.d"
UPDATERCD:class-target = ""

# Don't install big /boot/vmlinux* file by default in qemux86 images (included in oprofile's RRECOMMENDS)
# Use --no-vmlinux option to opcontrol or install it manually if you need to see samples from kernel
BAD_RECOMMENDATIONS:append:qemux86 = " kernel-vmlinux"

PREFERRED_PROVIDER_virtual/docker = "docker-moby"

# Make wam preferred provider for webappmanager-webos
PREFERRED_PROVIDER_virtual/webappmanager-webos ?= "wam${WEBRUNTIME_TOOLCHAIN}"

VIRTUAL-RUNTIME_ntp = "ntp sntp"

# Options: luna-surface-manager, auto-luna-surface-manager
VIRTUAL-RUNTIME_surface-manager = "luna-surfacemanager-base"
VIRTUAL-RUNTIME_surface-manager-conf ?= "luna-surfacemanager-conf"
VIRTUAL-RUNTIME_surface-manager-extension ?= ""
VIRTUAL-RUNTIME_surface-manager-tests = "luna-surfacemanager-base-tests"

# Options: com.webos.service.pdm, physical-device-manager, physical-device-manager2
VIRTUAL-RUNTIME_pdm = "com.webos.service.pdm"

# We want to distinguish between ofono (mainline etc) and ofono-halium (Halium based devices)
VIRTUAL-RUNTIME_ofono = "ofono"
VIRTUAL-RUNTIME_ofono-tests = "ofono-tests"
