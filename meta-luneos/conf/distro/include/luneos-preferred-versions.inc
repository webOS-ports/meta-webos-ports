# Copyright (c) 2012-2023 LG Electronics, Inc.

# Set default chromium version for webruntime and mksnapshot.
# 151 is the default on every machine as of 2026-08-20. Set this back to
# "120.%" to return the whole distro to Chromium 120; both recipe sets are
# still present.
PREFERRED_VERSION_webruntime = "151.%"
PREFERRED_VERSION_webruntime-clang = "${PREFERRED_VERSION_webruntime}"
PREFERRED_VERSION_mksnapshot-cross-${TARGET_ARCH} = "${PREFERRED_VERSION_webruntime}"

PREFERRED_VERSION_lttng-modules:tenderloin    = "2.13.12+git"

# Go 1.27 rather than wrynose's 1.26. The recipes are carried in
# meta-luneos-backports-6.1/recipes-devtools/go, from oe-core blacksail; drop
# them and this block when the layer moves to an oe-core release with
# go >= 1.27. Needed because go 1.26's _rt0_arm_lib leaves SP 4 mod 8 on armv7
# when it calls into C, so a Go c-archive loaded into a process dies with
# SIGBUS in glibc's create_thread (purple-combined's libwhatsmeow.so took
# imlibpurpleservice down). golang/go#58548, fixed in 1.27 by CL 706417.
GOVERSION ?= "1.27%"
PREFERRED_VERSION_go = "${GOVERSION}"
PREFERRED_VERSION_nativesdk-go = "${GOVERSION}"
PREFERRED_VERSION_go-runtime = "${GOVERSION}"
PREFERRED_VERSION_nativesdk-go-runtime = "${GOVERSION}"
PREFERRED_VERSION_go-binary-native = "${GOVERSION}"
PREFERRED_VERSION_go-cross-${TUNE_PKGARCH} = "${GOVERSION}"
PREFERRED_VERSION_go-crosssdk-${SDK_SYS} = "${GOVERSION}"
PREFERRED_VERSION_go-cross-canadian-${TRANSLATED_TARGET_ARCH} = "${GOVERSION}"

# android-tools 35.0.2 rather than wrynose's 5.1.1. The recipes (and
# android-libboringssl, which they link) are carried in
# meta-luneos-backports-6.1/recipes-devtools, from meta-openembedded blacksail;
# drop them and this block when the layer moves to a release that has them.
# LuneOS's adbd changes are in meta-luneos android-tools_35%.bbappend.
PREFERRED_VERSION_android-tools = "35.0.2"
PREFERRED_VERSION_android-tools-native = "35.0.2"
# blacksail has one android-tools-conf, the configfs one, which also PROVIDES
# and RPROVIDES android-tools-conf-configfs (what android-tools 35.0.2
# RDEPENDS on). wrynose still has its own android-tools-conf-configfs recipe
# too, and two providers of one package must not both reach an image (the
# blacksail one RCONFLICTS with it), so name blacksail's for both names. A
# local.conf that sets PREFERRED_RPROVIDER_android-tools-conf to
# "android-tools-conf-configfs" has to drop that line.
PREFERRED_PROVIDER_android-tools-conf ?= "android-tools-conf"
PREFERRED_PROVIDER_android-tools-conf-configfs ?= "android-tools-conf"
PREFERRED_RPROVIDER_android-tools-conf-configfs ?= "android-tools-conf"

# Per-app V8 snapshots stay off under M151.
# ----------------------------------------
# webos_enactjs_app.bbclass defaults WEBOS_ENACTJS_PACK_OPTS to include
# --snapshot, which builds each app a snapshot_blob.bin with mksnapshot-cross.
# A V8 snapshot is only loadable by the V8 that produced it, and blobs built by
# the M120 mksnapshot made every Enact app that ships one fail to start under
# M151 - deleting the 8 blobs on the target was what got those apps running.
# mksnapshot-cross_151 exists and configures now, but it has not been proven per
# target arch, so leave --snapshot off: the apps load without a custom snapshot,
# which costs some startup time but is correct. Dropping it also drops the
# conditional mksnapshot-cross dependency in the class.
WEBOS_ENACTJS_PACK_OPTS = "--production --isomorphic --locales=webos"
