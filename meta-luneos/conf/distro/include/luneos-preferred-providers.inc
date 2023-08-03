# Copyright (c) 2012-2014 LG Electronics, Inc.

# Place distro-wide PREFERRED_PROVIDER settings here; place MACHINE-specific
# settings in <MACHINE>.conf .

PREFERRED_PROVIDER_virtual/webruntime = "webruntime"
VIRTUAL-RUNTIME_webruntime = "webruntime"

# Prefer libjpeg-turbo from meta-oe as it provides SIMD optimizations
PREFERRED_PROVIDER_jpeg = "libjpeg-turbo"
PREFERRED_PROVIDER_jpeg-native = "libjpeg-turbo-native"

# Just for documentation; these values are the defaults set in the recipes that
# use them.
VIRTUAL-RUNTIME_cpushareholder = "cpushareholder-stub"

# Don't install big /boot/vmlinux* file by default in qemux86 images (included in oprofile's RRECOMMENDS)
# Use --no-vmlinux option to opcontrol or install it manually if you need to see samples from kernel
BAD_RECOMMENDATIONS:append:qemux86 = " kernel-vmlinux"

# switch from upstart to systemd
VIRTUAL-RUNTIME_initscripts = "webos-initscripts"
PREFERRED_RPROVIDER_initscripts-functions = "webos-initscripts"

# used by meta-networking/recipes-connectivity/autossh/autossh_1.4g.bb
PREFERRED_RPROVIDER_ssh = "openssh"

VIRTUAL-RUNTIME_init_manager = "systemd systemd-compat-units systemd-machine-units"
BAD_RECOMMENDATIONS = "update-rc.d"

VIRTUAL-RUNTIME_stat = "busybox"

# Used for testing IME Manager from webOS OSE instead of webos-keyboard
VIRTUAL-RUNTIME_webos-ime = "webos-keyboard"
# VIRTUAL-RUNTIME_webos-ime = "imemanager"

# Make wam preferred provider for webappmanager-webos
PREFERRED_PROVIDER_webappmanager-webos ?= "wam"

VIRTUAL-RUNTIME_com.webos.app.browser ?= "com.webos.app.enactbrowser"
VIRTUAL-RUNTIME_webappmanager = "wam"

PREFERRED_PROVIDER_llvm = "clang"
PREFERRED_PROVIDER_llvm-native = "clang-native"
PREFERRED_PROVIDER_nativesdk-llvm = "nativesdk-clang"
PROVIDES:pn-clang = "llvm"
PROVIDES:pn-clang-native = "llvm-native"
PROVIDES:pn-nativesdk-clang = "nativesdk-llvm"
