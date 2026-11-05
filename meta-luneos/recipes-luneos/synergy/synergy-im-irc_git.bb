SUMMARY = "IRC account type for Synergy messaging"
DESCRIPTION = "The com.palm.irc account template and the com.palm.app.irc sign-in app from luneos-synergy-connectors. The protocol itself is libpurple's own prpl-irc (libpurple-protocol-irc), which imlibpurpleservice signs in; its channels show in the Messaging app's Servers tab."

require luneos-synergy-connectors.inc

LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

PE = "3"
PV = "1.0+git"

S = "${LSC_SRCDIR}"

# A template, an Enyo app and images: nothing is compiled.
inherit allarch

# webos_filesystem_paths for webos_accttemplatesdir and webos_applicationsdir. webos_app makes
# the sign-in app's LS2 role and client permissions from the requiredPermissions in its
# appinfo.json (accountsservice, networkconnection), as purple-connectors.inc does for the other
# messaging plug-ins' apps.
inherit webos_filesystem_paths
inherit webos_app

# The Accounts service only sees a new or removed template after a restart; see there.
require synergy-accounts-reload.inc

do_install() {
    # Account template, so IRC can be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${S}/im/irc/account/com.palm.irc ${D}${webos_accttemplatesdir}/

    # Sign-in app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/im/irc/apps/com.palm.app.irc ${D}${webos_applicationsdir}/
}

FILES:${PN} += "${webos_accttemplatesdir} ${webos_applicationsdir}"

# The protocol plug-in, purple-2/libirc.so. imlibpurpleservice, which signs it in, recommends
# this package along with the other messaging plug-ins.
RDEPENDS:${PN} += "libpurple-protocol-irc"
