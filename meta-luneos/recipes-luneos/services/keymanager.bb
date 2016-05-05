SUMMARY = "Keymanager implementation for webOS ports"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6c4db32a2fa8717faffa1d4f10136f47"

#Disabled until we have node-sqlite3 working again. 
#RDEPENDS_${PN} = "node-sqlite3"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "f01e8b9abb64a0f0cc85d44ec2996622e81fae20"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/sysbus"

do_install() {
    # the service itself
    install -d ${D}${webos_servicesdir}/com.palm.keymanager
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/com.palm.keymanager
    rm -rf ${D}${webos_servicesdir}/com.palm.keymanager/sysbus
}

FILES_${PN} += "${webos_servicesdir}"
