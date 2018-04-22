# Copyright (c) 2013-2014 LG Electronics, Inc.

SUMMARY = "A userspace service that provides access to the Open webOS database"
SECTION = "webos/base"
AUTHOR = "Maksym Sditanov <maxim.sditanov@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# db8 is also the provider for mojodb
PROVIDES = "mojodb"

DEPENDS = "luna-service2 jemalloc icu pmloglib curl glib-2.0 leveldb leveldb-tl boost"

PV = "3.2.0-145+git${SRCPV}"
SRCREV = "efa412ab7ec65f14839477544f53d76f9a317e6d"

RDEPENDS_${PN} += "leveldb bash"
RDEPENDS_${PN}-tests += "bash"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit pkgconfig
inherit systemd

EXTRA_OECMAKE += "-DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE -DWEBOS_DB8_BACKEND:STRING='leveldb;sandwich' -DCMAKE_SKIP_RPATH:BOOL=TRUE"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}@.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/${SYSTEMD_SERVICE_${PN}} ${D}${systemd_unitdir}/system/

    install -d ${D}${bindir}
    install -m 0755 ${S}/files/scripts/db8-prestart.sh ${D}${bindir}
}

PACKAGES =+ "${PN}-tests"

FILES_${PN}-tests = "${libdir}/${PN}/tests/*"
FILES_${PN}-dbg += "${libdir}/${PN}/tests/.debug"

CXXFLAGS += "-fpermissive"
