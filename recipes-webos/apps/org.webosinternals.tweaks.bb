SUMMARY = "Tweaks for your webOS device."
SECTION = "webos/apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit webos_public_repo
inherit webos_arch_indep

PV = "3.0.1+gitr${SRCPV}"

SRCREV = "8183f1cc70e5a1e05a60e57198d22912bd210d73"
SRC_URI = " \
    git://github.com/webos-internals/tweaks;protocol=git;branch=master \
    file://0001-Fix-service-path.patch \
"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_applicationsdir}/${PN}
    cp -rv ${S}/enjo-app/* ${D}${webos_applicationsdir}/${PN}

    install -d ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs
    cp -rv ${S}/node-service/* ${D}${webos_servicesdir}/org.webosinternals.tweaks.prefs

    install -d ${D}${webos_sysconfdir}/db/kinds
    install -m 0644 ${S}/node-service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds

    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/node-service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

PACKAGES = "${PN}"
FILES_${PN} = " \
    ${webos_applicationsdir}/org.webosinternals.tweaks \
    ${webos_servicesdir}/org.webosinternals.tweaks.prefs \
    ${webos_sysconfdir}"
