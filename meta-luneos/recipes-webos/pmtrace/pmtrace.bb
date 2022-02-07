# Copyright (c) 2013-2021 LG Electronics, Inc.

SUMMARY = "lttng-ust tracepoints wrapper library and performance tools"
AUTHOR = "Andre Rosa <andre.rosa@lge.com>"
SECTION = "webos/libs"
LICENSE = "LGPLv2.1 & MIT & Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=e2aa4f66375a24019b0ff5e99cec40ad \
                    file://LICENSE.MIT;md5=19b5d9061141f7ab05cfcfdd4404ed08 \
                    file://README.md;md5=35108c1521572d2a526926333b233cd7 \
                    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "lttng-ust libpbnjson pmloglib glib-2.0"

RDEPENDS_${PN} += " \
    babeltrace \
    lttng-tools \
    lttng-modules \
"

#WEBOS_VERSION = "1.0.0-9_baf2c8ce00a722e9f58a1359993bf887b30d27e5"
#PR = "r11"

SRCREV = "f730615ed39d43210f4bd6c8802570e2b376c68a"

#inherit webos_component
inherit webos_cmake
#inherit webos_enhanced_submissions
#inherit webos_library
inherit webos_lttng
inherit webos_public_repo
#inherit webos_prerelease_dep

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# The libmemtracker, libpmtrace, pmctl (library/header/binary files) will be installed in all builds except RELEASE mode.
# Only libpmtrace header files need to install in all builds for other modules that are referring to the header files.
EXTRA_OECMAKE += "-DENABLE_LIBPMTRACE:BOOLEAN=False"
EXTRA_OECMAKE += "-DDEFAULT_LOGGING:STRING=${@'' if ('${WEBOS_DISTRO_PRERELEASE}' == '') else 'pmlog'}"

SRC_URI += "file://0001-use-python3.patch" 
