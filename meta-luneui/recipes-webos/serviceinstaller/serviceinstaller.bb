# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "An extensible object oriented component used to add service components to webOS"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "librolegen glib-2.0 libpbnjson luna-service2"

PV = "2.0.0-3+git${SRCPV}"
SRCREV = "f7ac01e06bba81de4f21f004533dbee507a85f1c"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-Make-sure-generated-service-files-get-a-.service-suf.patch \
"
S = "${WORKDIR}/git"

ALLOW_EMPTY:${PN} = "1"
