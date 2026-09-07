SUMMARY = "Backup and Restore app for LuneOS"
SECTION = "webos/apps"
# The repository carries no LICENSE file and no per-file headers. Declared from
# its ancestry - it is a port of com.palm.service.backup and com.palm.app.backup
# from Open webOS, both Apache-2.0 - and should be pinned to a real LICENSE file
# once upstream adds one.
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "3.1.1+git"
SRCREV = "417e6b03f43c13ed19faa3f6234afed13e0021af"

# Not webos_ports_repo: the app is not under github.com/webOS-ports. Split into
# variables so retargeting it - to an upstream fork, or once it moves into the
# organisation - is a one-line change rather than an edit to SRC_URI.
WOCE_GIT_REPO   ?= "github.com/Herrie82/woce-backup"
WOCE_GIT_BRANCH ?= "herrie/LuneOS"

SRC_URI = "git://${WOCE_GIT_REPO};protocol=https;branch=${WOCE_GIT_BRANCH}"

# The ACG files ship in the app repository, beside the code whose interface they
# describe. They are inert on webOS 3.0.5, which has no ACG model at all.
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

# Working files that palm-package would happily copy into the app directory.
# Mirrors packaging/package-exclude.txt in the repository; kept in step with it.
WOCE_EXCLUDE = "CLAUDE.md AGENTS.md .claude node_modules __pycache__ *.orig *.rej *.bak *~ .DS_Store Thumbs.db"

do_install:append() {
    # The app and its JS service. run-js-service starts the service on demand
    # from the .service file below, so there is no unit to install.
    # Not cp -a: that preserves the checkout's ownership, so everything under
    # ${D} ends up owned by whoever ran bitbake and do_package fails with
    # "uid not found ... may be due to host contamination". Mode and timestamps
    # are worth keeping; ownership is not ours to carry.
    install -d ${D}${webos_applicationsdir}/${BPN}
    cp -R --no-dereference --preserve=mode,timestamps,links ${S}/${BPN}/. ${D}${webos_applicationsdir}/${BPN}/

    install -d ${D}${webos_servicesdir}/${BPN}.service
    cp -R --no-dereference --preserve=mode,timestamps,links ${S}/${BPN}.service/. ${D}${webos_servicesdir}/${BPN}.service/

    # noglob: the list holds *.orig and friends, and an unquoted `for` would
    # expand them against the build directory before the loop ever ran.
    set -f
    for pattern in ${WOCE_EXCLUDE}; do
        find ${D}${webos_applicationsdir}/${BPN} ${D}${webos_servicesdir}/${BPN}.service \
            -name "$pattern" -exec rm -rf {} + || true
    done
    set +f

    # device/ is deliberately not installed. woce-backupd is a root helper for
    # webOS 3.0.5, where the service is jailed away from /var/preferences and
    # /media/cryptofs and cannot read what it has to back up. LuneOS does not
    # jail it - it already runs as uid 0 with those trees readable - so the
    # service does that work in-process, and shipping a second root daemon here
    # would add attack surface to solve a problem this platform does not have.
    # device/postinst and device/prerm exist only to install and remove that
    # daemon, so they go with it.

    # ACG configuration.
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -d ${D}${webos_sysbus_manifestsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.app.json ${D}${webos_sysbus_rolesdir}/${BPN}.app.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.service.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.app.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.app.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.service.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.service.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service.groups.json ${D}${webos_sysbus_groupsdir}/${BPN}.service.groups.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.manifest.json ${D}${webos_sysbus_manifestsdir}/${BPN}.manifest.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service.service ${D}${webos_sysbus_servicedir}/${BPN}.service.service
}

FILES:${PN} += " \
    ${webos_applicationsdir}/${BPN} \
    ${webos_servicesdir}/${BPN}.service \
"
