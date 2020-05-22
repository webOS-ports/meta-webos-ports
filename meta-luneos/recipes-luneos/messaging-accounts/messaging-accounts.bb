SUMMARY = "Messaging IM Account Templates & Validator"
SECTION = "luneos/messaging"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_app

SRCREV = "e9391922868126b0a700dc3be313cbe43905ab70"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
WEBOS_GIT_PARAM_BRANCH = "herrie/acg"

do_install() {
    # account templates
    install -d ${D}${webos_accttemplatesdir}/
    cp -vrf ${S}/accounts/* ${D}${webos_accttemplatesdir}/

    # account creation application
    install -d ${D}${webos_applicationsdir}/org.webosports.app.imvalidator
    cp -rv ${S}/application/* ${D}${webos_applicationsdir}/org.webosports.app.imvalidator/
}

FILES_${PN} += "${webos_applicationsdir} ${webos_accttemplatesdir}"

