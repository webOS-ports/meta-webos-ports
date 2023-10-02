SUMMARY = "Keymanager implementation for webOS ports"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6c4db32a2fa8717faffa1d4f10136f47"

#Disabled until we have node-sqlite3 working again. 
#RDEPENDS:${PN} = "node-sqlite3"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+git"
SRCREV = "7c88a02bc3db9c0550baa98de25b1f3cd061f075"

WEBOS_REPO_NAME = "keymanager"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

do_install:append() {
    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}

    install -d ${D}${webos_sysbus_permissionsdir}

    install -d ${D}${webos_sysbus_rolesdir}

    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service ${D}${webos_sysbus_servicedir}/${BPN}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.api.json

    # Install the service itself
    install -d ${D}${webos_servicesdir}/${BPN}
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/${BPN}
}

FILES:${PN} += "${webos_servicesdir}"
