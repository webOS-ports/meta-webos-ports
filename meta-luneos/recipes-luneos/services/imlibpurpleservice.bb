SUMMARY = "Instant Messaging service"
SECTION = "webos/services"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=263f341c01743dbd6b06ae75369dbeed \
    file://COPYRIGHT;md5=2ce083d13f0f21e5207b4115c8926450 \
"

DEPENDS = "glib-2.0 db8 pidgin luna-service2 tidy-html5"

PV = "3.0.5+gitr${SRCPV}"
SRCREV = "029c2149b20196ac876546a37d73380a65f90e8d"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

RRECOMMENDS_${PN} += " \
    pidgin-sipe \
    whatsapp-purple \
    libpurple-plugin-autoaccept \
    libpurple-plugin-buddynote \
    libpurple-plugin-idle \
    libpurple-plugin-joinpart \
    libpurple-plugin-log_reader \
    libpurple-plugin-newline \
    libpurple-plugin-offlinemsg \
    libpurple-plugin-psychic \
    libpurple-plugin-ssl \
    libpurple-plugin-ssl-gnutls \
    libpurple-plugin-statenotify \
    libpurple-protocol-aim \
    libpurple-protocol-bonjour \
    libpurple-protocol-gg \
    libpurple-protocol-icq \
    libpurple-protocol-irc \
    libpurple-protocol-msn \
    libpurple-protocol-mxit \
    libpurple-protocol-myspace \
    libpurple-protocol-novell \
    libpurple-protocol-simple \
    libpurple-protocol-xmpp \
    libpurple-protocol-yahoojp \
    libpurple-protocol-zephyr \
"

do_install_append() {
    cp -R --no-dereference --preserve=mode,links -v ${S}/files/etc ${D}
    cp -R --no-dereference --preserve=mode,links -v ${S}/files/usr ${D}
}

CXXFLAGS += "-fpermissive"
