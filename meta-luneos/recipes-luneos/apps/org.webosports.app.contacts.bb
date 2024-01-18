SUMMARY = "Enyo 2 Contacts App for LuneOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyodev_application
inherit webos_filesystem_paths
inherit webos_application
inherit webos_app

PV = "0.0.1+git"
SRCREV = "56b9a27bc413fd77ab540379f5898af353d67e5b"

# For compatibility reasons we have to name the app internally as the old palm contacts
# app. Maybe possible to switch that later.
WEBOS_APPLICATION_NAME = "com.palm.app.contacts"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# Workaround for network access issue during do_compile task
# unfortunately enyo-dev fetches the dependencies during the do_compile
# instead of proper OE dependencies fetched during do_fetch
do_compile[network] = "1"
