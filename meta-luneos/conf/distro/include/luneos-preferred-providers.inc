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
VIRTUAL-RUNTIME_rdx-utils = "rdxd"

# Don't install big /boot/vmlinux* file by default in qemux86 images (included in oprofile's RRECOMMENDS)
# Use --no-vmlinux option to opcontrol or install it manually if you need to see samples from kernel
BAD_RECOMMENDATIONS_append_qemux86 = " kernel-vmlinux"

# switch from upstart to systemd
VIRTUAL-RUNTIME_initscripts = "webos-initscripts"
PREFERRED_RPROVIDER_initscripts-functions = "webos-initscripts"

VIRTUAL-RUNTIME_init_manager = "systemd systemd-compat-units systemd-machine-units"
BAD_RECOMMENDATIONS = "update-rc.d"

VIRTUAL-RUNTIME_stat = "busybox"

# Make wam preferred provider for webappmanager-webos
PREFERRED_PROVIDER_webappmanager-webos ?= "wam"

# Use Enactbrowser by default, except for armv45
VIRTUAL-RUNTIME_com.webos.app.browser = "com.webos.app.enactbrowser"
VIRTUAL-RUNTIME_com.webos.app.browser_armv4 = ""
VIRTUAL-RUNTIME_com.webos.app.browser_armv5 = ""

VIRTUAL-RUNTIME_webappmanager = "wam"
VIRTUAL-RUNTIME_webappmanager_armv4 = ""
VIRTUAL-RUNTIME_webappmanager_armv5 = ""
