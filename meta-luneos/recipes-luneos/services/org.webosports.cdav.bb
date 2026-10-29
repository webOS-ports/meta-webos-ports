SUMMARY = "Node.js service to provide synergy connector for CardDav and CalDav"
SECTION = "webos/services"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_ports_ose_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus
inherit webos_app

PV = "0.3.34+git"
SRCREV = "191071e6d87602af81aceb68ed07622bb1c222b5"

SERVICE_NAME = "org.webosports.service.cdav"
WEBOS_REPO_NAME = "org.webosports.service.contacts.carddav"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

CLEANBROKEN = "1"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install:append() {
    # the service itself
    install -d ${D}${webos_servicesdir}/org.webosports.service.cdav
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/org.webosports.service.cdav
    rm -rf ${D}${webos_servicesdir}/org.webosports.service.cdav/configuration
    
    # ACG configuration files
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.groups.json ${D}${webos_sysbus_groupsdir}/${SERVICE_NAME}.groups.json
    
    # db8 kinds and permissions
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -vrf ${S}/service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/
    cp -vrf ${S}/service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/

    # account templates
    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav
    cp -vrf ${S}/accounts-enyo/* ${D}${webos_accttemplatesdir}/org.webosports.cdav

    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.google
    cp -vrf ${S}/accounts-google/* ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.google

    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.icloud
    cp -vrf ${S}/accounts-icloud/* ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.icloud

    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.yahoo
    cp -vrf ${S}/accounts-yahoo/* ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.yahoo

    # account creation application
    install -d ${D}${webos_applicationsdir}/org.webosports.app.cdav
    cp -rv ${S}/app-enyo/* ${D}${webos_applicationsdir}/org.webosports.app.cdav/

    # copy urlschemes.js from service dir to application dir
    cp -v ${S}/service/javascript/urlschemes.js ${D}${webos_applicationsdir}/org.webosports.app.cdav/CrossAppTarget/
}

# Google OAuth client for the Google C+DAV account type. None is committed
# anywhere: the account app carries "PLACEHOLDER_GOOGLE_CDAV_CLIENT_ID" and
# "PLACEHOLDER_GOOGLE_CDAV_CLIENT_SECRET" (the client it once had is public and
# is to be replaced), filled in here from the builder's own values - in
# local.conf, or a file it includes that no repository holds (luneos-secrets'
# secrets.conf). Unset, the Google sign-in page says sign-in is not available.
# The values become part of do_install's task hash, so an sstate cache built
# with them is as private as they are.
#
# Needs a SRCREV with the placeholders (org.webosports.service.contacts.carddav
# branch herrie/security-WP4-no-oauth-secret); until then the build warns.
GOOGLE_CDAV_CLIENT_ID ?= ""
GOOGLE_CDAV_CLIENT_SECRET ?= ""

do_install:append() {
    js=${D}${webos_applicationsdir}/org.webosports.app.cdav/GoogleOauth/CrossAppTarget.js
    fill_google_client() {
        if ! grep -q "\"PLACEHOLDER_$1\"" "$js"; then
            bbwarn "${PN}: GoogleOauth/CrossAppTarget.js has no PLACEHOLDER_$1; $1 is not used"
            return 0
        fi
        [ -n "$2" ] || return 0
        case "$2" in
            *[!A-Za-z0-9._~-]*) bbfatal "$1 holds characters a client id or secret does not" ;;
        esac
        sed -i "s|\"PLACEHOLDER_$1\"|\"$2\"|" "$js"
    }
    fill_google_client GOOGLE_CDAV_CLIENT_ID "${GOOGLE_CDAV_CLIENT_ID}"
    fill_google_client GOOGLE_CDAV_CLIENT_SECRET "${GOOGLE_CDAV_CLIENT_SECRET}"
}

FILES:${PN} += "${webos_applicationsdir} ${webos_servicesdir} ${webos_accttemplatesdir}"
