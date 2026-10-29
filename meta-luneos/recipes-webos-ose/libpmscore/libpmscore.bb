# Copyright (c) 2020-2025 LG Electronics, Inc.

SUMMARY = "pmscore handles device states"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=ceae6b60c3da8e439a7f94eb5f011878 \
"

DEPENDS = "luna-service2 glib-2.0 pmloglib libpbnjson nyx-lib"

WEBOS_VERSION = "1.0.0-11_f7dd5e7ec8cb39154634aab0d70667c29253e0be"
PR = "r3"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit webos_public_repo
inherit webos_library

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

# CMake 4: @VAR@ is no longer expanded in unquoted arguments (CMP0053),
# which broke the install() DESTINATIONs in this component.
SRC_URI += "file://0001-CMakeLists-use-CMake-variable-syntax-instead-of-VAR.patch"

# The Pms*Log() helpers passed the formatted message as the format string,
# which is why the recipe used to build with SECURITY_STRINGFORMAT = "".
SRC_URI += "file://0002-PmsLogging-pass-the-formatted-message-as-an-argument.patch"

# snprintf() returns the untruncated length, so a long "class::function "
# prefix moved the message past the end of the stack buffer.
SRC_URI += "file://0003-PmsLogging-keep-the-message-inside-the-buffer-when-t.patch"

# PmsLogNonClass() (the MSG_*2() macros) also used the formatted message as
# a format string.
SRC_URI += "file://0004-PmsLogging-PmsLogNonClass-must-not-format-the-messag.patch"
