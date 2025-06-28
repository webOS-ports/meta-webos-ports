SUMMARY = "Messaging app written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "0.0.1-2+git"
SRCREV = "36ed3eb7a3c4d6c540da866eb76552702d417d00"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_enyodev_application
inherit webos_app

WEBOS_REPO_NAME = "org.webosports.messaging"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${UNPACKDIR}/git/app"

# Workaround for network access issue during do_compile task
# unfortunately enyo-dev fetches the dependencies during the do_compile
# instead of proper OE dependencies fetched during do_fetch
do_compile[network] = "1"
