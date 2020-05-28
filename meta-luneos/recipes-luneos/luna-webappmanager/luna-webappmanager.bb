SUMMARY = "This is the web application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=0f93d2cf04b94ac3f04a789a1fb11ead \
    file://COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

DEPENDS = "qtbase qtdeclarative qtwebengine luna-sysmgr-common libconnman-qt5"
RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtwebengine-qmlplugins \
"

PV = "0.4.1-3+git${SRCPV}"
SRCREV = "f8cec8f6370ca2efada4fd26a1881a142bc87388"

SERVICE_NAME = "org.webosports.webappmanager"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
#WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
SRC_URI = "git://github.com/Tofee/luna-webappmanager.git;branch=herrie/acg"

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"

S = "${WORKDIR}/git"

inherit pkgconfig
inherit webos_system_bus
inherit webos_ports_repo
inherit webos_cmake_qt5
inherit webos_filesystem_paths
inherit webos_systemd

#WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
#WEBOS_SYSTEM_BUS_FILES_LOCATION = "${WORKDIR}/git/files/sysbus"

do_install_append() {
    # Install the ACG configuration
 #   install -d ${D}${webos_sysbus_servicedir}
  #  install -d ${D}${webos_sysbus_permissionsdir}

   # install -d ${D}${webos_sysbus_rolesdir}

    #install -d ${D}${webos_sysbus_apipermissionsdir}

    #install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    #install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json    
    #install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    #install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json
    install -v -m 0644 ${S}/files/sysbus/com.palm.luna-apps.role.json ${D}${webos_sysbus_rolesdir}/com.palm.luna-apps.role.json
}

FILES_${PN} += "${webos_frameworksdir}"
