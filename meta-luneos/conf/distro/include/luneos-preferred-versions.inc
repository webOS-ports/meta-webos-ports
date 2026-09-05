# Copyright (c) 2012-2023 LG Electronics, Inc.

# Set default chromium version for webruntime and mksnapshot.
# 151 is the default on every machine as of 2026-08-20. Set this back to
# "120.%" to return the whole distro to Chromium 120; both recipe sets are
# still present.
PREFERRED_VERSION_webruntime = "151.%"
PREFERRED_VERSION_webruntime-clang = "${PREFERRED_VERSION_webruntime}"
PREFERRED_VERSION_mksnapshot-cross-${TARGET_ARCH} = "${PREFERRED_VERSION_webruntime}"

PREFERRED_VERSION_lttng-modules:tenderloin    = "2.13.12+git"

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
