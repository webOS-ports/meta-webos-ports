SUMMARY = "Preware 2, the on-device homebrew installer for LuneOS and legacy webOS."
SECTION = "webos/apps"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_enyojs_application
inherit webos_app

PV = "2.1.0+git"
# TODO: the webOS-ports/webOS-OSE commit that merges webOS-ports/preware#54 (app id com.palm.app.preware2)
SRCREV = "fe35a454388ea2f56c6d64a4fb1bd40b7f5f399b"

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# Preware 2's app id was org.webosports.app.preware. It is com.palm.app.preware2 now,
# on LuneOS and on legacy webOS alike (one package for both), so that the original
# Mojo Preware (org.webosinternals.preware) can be installed next to it.
RPROVIDES:${PN} += "org.webosports.app.preware"
RREPLACES:${PN} += "org.webosports.app.preware"
RCONFLICTS:${PN} += "org.webosports.app.preware"
