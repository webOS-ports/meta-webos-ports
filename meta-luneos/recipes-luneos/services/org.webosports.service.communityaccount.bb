SUMMARY = "webOS Community Account identity bridge"
DESCRIPTION = "Answers com.palm.accountservices and com.palm.deviceprofile (the \
names the Accounts app palmID flow and First Use already call, keyed by nduId) \
against the webOSArchive device.php backend, keeping the local token \
getAccountToken serves to every other app. The backend bridge LuneOS was \
missing: the callers exist, but nothing answered them against a live server."
SECTION = "webos/services"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-or-later;md5=1c76c4cc354acaac30ed4d5eefea7245"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

# NOTE: this fetches github.com/webOS-ports/org.webosports.service.communityaccount,
# which does not exist upstream yet. The service currently lives only in a local
# checkout committed at the SRCREV below (a real commit, not a placeholder).
# Push that repo to the webOS-ports org before building an image from this branch.
PV = "1.0.0+git"
PE = "1"
SRCREV = "9ea6ddde567b1034915dc5ee35ac92e363849673"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

CLEANBROKEN = "1"

# A pure modern-Node service (run-js-service / jsservicelauncher at runtime);
# nothing to compile.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

# One process registers both bus names; the service directory is named for the
# primary one.
SERVICE_DIR = "org.webosports.service.communityaccount"

do_install() {
    # the service itself (code only; not tests, sysbus files, or docs)
    install -d ${D}${webos_servicesdir}/${SERVICE_DIR}
    install -m 0644 ${S}/service.js ${D}${webos_servicesdir}/${SERVICE_DIR}/
    install -m 0644 ${S}/package.json ${D}${webos_servicesdir}/${SERVICE_DIR}/
    cp -rv ${S}/lib ${D}${webos_servicesdir}/${SERVICE_DIR}/

    # ACG configuration files, both bus names
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}

    # com.palm.accountservices (full ACG set) + its deviceprofile companion .service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/com.palm.accountservices.service ${D}${webos_sysbus_servicedir}/com.palm.accountservices.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/com.palm.deviceprofile.service ${D}${webos_sysbus_servicedir}/com.palm.deviceprofile.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/com.palm.accountservices.perm.json ${D}${webos_sysbus_permissionsdir}/com.palm.accountservices.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/com.palm.accountservices.role.json ${D}${webos_sysbus_rolesdir}/com.palm.accountservices.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/com.palm.accountservices.api.json ${D}${webos_sysbus_apipermissionsdir}/com.palm.accountservices.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/com.palm.accountservices.groups.json ${D}${webos_sysbus_groupsdir}/com.palm.accountservices.groups.json
}

# run-js-service is a file of mojoservicelauncher, not a package; palmbus
# comes from nodejs-module-webos-sysbus.
RDEPENDS:${PN} += "nodejs mojoservicelauncher nodejs-module-webos-sysbus"

FILES:${PN} += "${webos_servicesdir}"
