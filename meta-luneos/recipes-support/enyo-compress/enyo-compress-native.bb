# Copyright (c) 2026 LuneOS
# Makes the Enyo 1 build tool (support/enyo-compress) available to other recipes.
# enyo-1.0.bb already runs this tool to produce enyo-build.js, but it lives inside
# the enyo-1.0 source tree where nothing else can reach it. core-apps.bb needs it
# to bundle each app's JavaScript at build time.
#

SUMMARY = "Enyo 1.0 UglifyJS-based application compressor (build host tool)"
SECTION = "webos/devtools"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Keep PV/SRCREV in lockstep with enyo-1.0.bb -- same repo, same revision.
PV = "1.0-128.2+git"
SRCREV = "2f02364b761f98ba58732fcc87eba3709ae2568e"

# The tool ships inside the enyo-1.0 repo, not one named after this recipe, so the
# repo name has to be overridden -- WEBOS_REPO_NAME otherwise defaults to ${BPN}.
WEBOS_REPO_NAME = "enyo-1.0"

inherit webos_ports_fork_repo
inherit native

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${base_prefix}/opt/enyo-compress
    cp -R --no-dereference --preserve=mode,links -v \
        ${S}/support/enyo-compress/* ${D}${base_prefix}/opt/enyo-compress
}

SYSROOT_DIRS += "${base_prefix}/opt"
