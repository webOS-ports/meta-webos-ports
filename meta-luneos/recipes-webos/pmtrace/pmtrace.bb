# Copyright (c) 2013-2023 LG Electronics, Inc.

SUMMARY = "lttng-ust tracepoints wrapper library and performance tools"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/libs"

LICENSE = "LGPL-2.1-only & MIT & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPL;md5=e2aa4f66375a24019b0ff5e99cec40ad \
    file://LICENSE.MIT;md5=19b5d9061141f7ab05cfcfdd4404ed08 \
    file://README.md;md5=35108c1521572d2a526926333b233cd7 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "lttng-ust libpbnjson pmloglib glib-2.0"

# See https://github.com/shr-project/meta-webosose/commit/c1163bcddc2b3381881458378e3a383296d7a5d9
# RDEPENDS:${PN} += " \
#     babeltrace \
#     lttng-tools \
#     lttng-modules \
# "

WEBOS_VERSION = "1.0.0-15_407d07f257498472a53bf39c9d176953885cadeb"
PR = "r13"

inherit webos_cmake
inherit webos_lttng
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# The libmemtracker, libpmtrace, pmctl (library/header/binary files) will be installed in all builds except RELEASE mode.
# Only libpmtrace header files need to install in all builds for other modules that are referring to the header files.
# See https://github.com/shr-project/meta-webosose/commit/c1163bcddc2b3381881458378e3a383296d7a5d9
EXTRA_OECMAKE += "-DENABLE_LIBPMTRACE:BOOLEAN=False"
EXTRA_OECMAKE += "-DDEFAULT_LOGGING:STRING=${@'' if ('${WEBOS_DISTRO_PRERELEASE}' == '') else 'pmlog'}"
