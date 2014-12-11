SUMMARY = "Tweaks for your webOS device."
SECTION = "webos/apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit webos_public_repo
inherit allarch

PV = "3.0.1+gitr${SRCPV}"

SRCREV = "21c8072380c6d249a85dff84b8557fa8860099de"
SRC_URI = "git://github.com/webOS-ports/tweaks;protocol=git;branch=master"
S = "${WORKDIR}/git"

do_compile() {
}

do_install() {
    install -d ${D}${webos_applicationsdir}/${PN}
    cp -rv ${S}/enjo-app/* ${D}${webos_applicationsdir}/${PN}

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
    ${webos_sysbus_pubservicesdir}"
