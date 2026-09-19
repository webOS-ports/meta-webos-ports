# Copyright (c) 2020-2023 LG Electronics, Inc.

DESCRIPTION = "location framework which provides location based services implementing location handlers, plugins, and Luna location service"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/location"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=7be9908f876cc5f1edaf1124d0084067 \
"

DEPENDS = "glib-2.0 libpbnjson libxml2 pmloglib luna-service2 luna-prefs loc-utils boost"

# Built from the webOS-ports fork's herrie/fixes branch (webosose plus the
# former patch stack committed, plus the audit fixes: buffer overflows in the
# stored-position and CEP-log paths, the 64-bit SNTP packet layout, the
# subscription-key truncations, geofence-id bounds and the GNSS-callback UB).
# Pinned with a plain SRCREV: submission tags are a webosose convention and
# this branch carries none.
SRCREV = "96d89ae078c6a50cf751b0d82ee289989d7889b8"
WEBOS_GIT_PARAM_BRANCH = "herrie/sleepd-resume-types"

# Set outright rather than derived from a submission tag. Kept monotonic:
# the patch-stack recipe shipped 1.0.0-108 r8.
PV = "1.0.0-109"
PR = "r1"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += "${libdir}/location/plugins/lib*.so"

# The branch builds cleanly with -Werror=format-security, so the distro-wide
# hardening no longer needs to be disabled for this component.
