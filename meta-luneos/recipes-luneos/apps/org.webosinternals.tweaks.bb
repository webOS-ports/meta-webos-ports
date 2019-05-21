SUMMARY = "Tweaks for your webOS device."
SECTION = "webos/apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit webos_filesystem_paths
inherit webos_ports_repo
inherit webos_configure_manifest
inherit allarch

PV = "3.0.3+git${SRCPV}"
SRCREV = "a26059d4784b3cd9c06b8364d7ff42ecaa6a9cdc"

WEBOS_REPO_NAME = "tweaks"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_compile() {
}

do_install() {
    install -d ${D}${webos_applicationsdir}/${PN}
    cp -rv ${S}/enyo-app/* ${D}${webos_applicationsdir}/${PN}

    install -d ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs
    cp -rv ${S}/node-service/* ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs

    # Install service configuration
    mkdir -p ${D}${webos_sysbus_prvrolesdir}
    cp ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs/org.webosinternals.tweaks.prefs.json \
        ${D}${webos_sysbus_prvrolesdir}
    mkdir -p ${D}${webos_sysbus_pubrolesdir}
    cp ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs/org.webosinternals.tweaks.prefs.json \
        ${D}${webos_sysbus_pubrolesdir}
    mkdir -p ${D}${webos_sysbus_prvservicesdir}
    cp ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs/org.webosinternals.tweaks.prefs.service \
        ${D}${webos_sysbus_prvservicesdir}
    mkdir -p ${D}${webos_sysbus_pubservicesdir}
    cp ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs/org.webosinternals.tweaks.prefs.service \
        ${D}${webos_sysbus_pubservicesdir}

    # Install ACG
    install -d ${D}${webos_sysbus_apipermissionsdir}
    cp -vrf $SERVICE/files/sysbus/*.api.json ${D}${webos_sysbus_apipermissionsdir} 2> /dev/null || true
    install -d ${D}${webos_sysbus_permissionsdir}
    cp -vrf $SERVICE/files/sysbus/*.perm.json ${D}${webos_sysbus_permissionsdir} 2> /dev/null || true

    install -d ${D}${webos_sysconfdir}/db/kinds
    install -m 0644 ${S}/node-service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds

    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/node-service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

PACKAGES = "${PN}"
FILES_${PN} = " \
    ${webos_applicationsdir}/org.webosinternals.tweaks \
    ${webos_servicesdir}/org.webosinternals.tweaks.prefs \
    ${webos_sysconfdir} \
    ${webos_sysbus_prvrolesdir} \
    ${webos_sysbus_pubrolesdir} \
    ${webos_sysbus_prvservicesdir} \
    ${webos_sysbus_pubservicesdir} \
    ${webos_sysbus_manifestsdir} ${webos_sysbus_apipermissionsdir} ${webos_sysbus_permissionsdir}"
