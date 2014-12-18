SUMMARY = "Instant Messaging Account Validator service"
SECTION = "webos/services"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYRIGHT;md5=b928fe818deef43f0b52d28bd42f56f2 \
"

DEPENDS = "glib-2.0 db8 pidgin luna-service2"

PV = "3.0.5+gitr${SRCPV}"
SRCREV = "71f8fa6a7113f339eb275868aef1136737acdfc5"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    cp -rva ${S}/files/usr ${D}
}
