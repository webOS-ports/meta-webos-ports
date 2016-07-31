SUMMARY = "Google Maps app re-written from scratch in Enyo 2"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_application

PV = "0.0.1+gitr${SRCPV}"
SRCREV = "Signed-off-by: Herman van Hazendonk <github.com@herrie.org>"

SRC_URI = "6ef88ec2bd8f00bfbc1bc2e665ee19f59f2799d5"
S = "${WORKDIR}/git"
