SUMMARY = "Messaging IM Account Templates & Validator"
SECTION = "luneos/messaging"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_app

SRCREV = "01a418e0c580f56ab9693f1b9b2f6e31d2f52300"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    # account templates
    install -d ${D}${webos_accttemplatesdir}/
    cp -vrf ${S}/accounts/* ${D}${webos_accttemplatesdir}/
    # Matrix is com.palm.matrix from purple-combined (prpl-matrix, with browser sign-in and
    # encryption). This generic one maps to the same type_matrix and would be listed as a second
    # "Matrix" in Add Account, with a sign-in that requires a password matrix.org no longer takes.
    rm -rf ${D}${webos_accttemplatesdir}/org.webosports.messaging.matrix

    # account creation application
    install -d ${D}${webos_applicationsdir}/org.webosports.app.imvalidator
    cp -rv ${S}/application/* ${D}${webos_applicationsdir}/org.webosports.app.imvalidator/
}

FILES:${PN} += "${webos_applicationsdir} ${webos_accttemplatesdir}"

