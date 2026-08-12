# Copyright (c) 2018-2025 LG Electronics, Inc.

SUMMARY = "Video output service"
DESCRIPTION = "Service which controls video output: connecting media pipelines to display \
sinks, setting the display window and managing video/graphics compositing. Talks to the \
hardware through a video output adaptation layer (VAL) implementation."
SECTION = "webos/multimedia"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

# Which VAL implementation to build against. Machines with a real video output
# backend should override this; the mock is a no-op that just lets the service
# build and run.
VAL_IMPL_PROVIDER ?= "videooutput-adaptation-layer-mock"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib videooutput-adaptation-layer-api ${VAL_IMPL_PROVIDER}"
RDEPENDS:${PN} += "${VAL_IMPL_PROVIDER}"

WEBOS_VERSION = "1.0.0-13_931b3e59260a97ea0741ebbd1d84fd30b405b484"
PR = "r0"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# The component configures and installs its own videooutputd.service into
# ${sysconfdir}/systemd/system, so webos_systemd (which expects the unit to come
# from SRC_URI) must not be inherited here.
FILES:${PN} += "${sysconfdir}/systemd/system"
