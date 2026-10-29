SUMMARY = "webOS Ports Developer mode management service"
SECTION = "webos/services"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+git"
SRCREV = "589dacc81e524fba6092f5a34ad1725cfeb48852"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/files
    rm -rf ${D}${webos_servicesdir}/${PN}/tests
    
    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service ${D}${webos_sysbus_servicedir}/${BPN}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.groups.json ${D}${webos_sysbus_groupsdir}/${BPN}.groups.json
    # The shell's group for the adb key questions (devmode-service.shell); in the
    # service from herrie/security-WP1b-devmode-access on.
    if [ -e ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}-shell.perm.json ]; then
        install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}-shell.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}-shell.perm.json
    fi
}

FILES:${PN} += "${webos_servicesdir}/${PN}"

inherit useradd

# Let the devmode recipe, add the developer users & group
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = " \
    -u 504 -r -g developer --system -c developer -d /home/developer -s /bin/sh developer; \
    -u 2003 -g developer -d /var -s /usr/sbin/nologin debug; \
    -u 2004 -g developer -d /var -s /usr/sbin/nologin dev-func; \
"

GROUPADD_PARAM:${PN} = " \
    -g 504 -f developer; \
"

GROUPMEMS_PARAM:${PN} = " \
    -a developer -g developer; \
    -a dev-func -g developer; \ 
"
