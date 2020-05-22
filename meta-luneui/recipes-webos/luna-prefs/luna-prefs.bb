# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Open webOS preferences manager"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 json-c sqlite3 glib-2.0 nyx-lib"

PV = "2.0.0-7+git${SRCPV}"
SRCREV = "fd4609cc1fbe0979a818c6292c0d05f315ba8e29"

RDEPENDS_${PN} = "luna-prefs-data"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # CFISH-930: remove "other" perms granted by pmmakefiles (aka palmmake):
    chmod o-rwx ${D}${bindir}/luna-prefs-service
    chmod o-rwx ${D}${bindir}/lunaprop
    
    # Workaround for version mismatch in libluna-prefs
    ln -svnf libluna-prefs.so.3 ${D}${libdir}/libluna-prefs.so.2
    
    
    install -d ${D}${sysconfdir}/prefs/properties
}
