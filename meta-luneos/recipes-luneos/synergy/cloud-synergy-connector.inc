# One cloud connector from luneos-synergy-connectors' cloud/<name>/: its service, its account
# template, its dedicated validator app if it has one, and the LS2/ACG files LuneOS needs to put
# the service on the bus.
#
# One recipe per connector, rather than one recipe with a package per connector, because of how
# LuneOS loads ACG files. webos_configure_manifest (WEBOS_SYSTEM_BUS_MANIFEST_TYPE "PACKAGE")
# writes ONE manifest per recipe listing every role/permission/service file the recipe installs,
# and ls-hubd rejects a manifest outright when a file it lists is missing (manifest.cpp returns
# false on the first file that fails to parse). Split into packages, the manifest would sit in one
# of them and point at files in eleven others that need not be installed. One recipe per
# connector gives each connector its own manifest, and lets an image or a test pick connectors
# one by one. The git clone is still shared, so the extra recipes cost no extra fetch.
#
# A recipe sets:
#   CLOUD_CONNECTOR   directory under cloud/ (box, dropbox, ..., yandex)
#   CLOUD_SERVICE     bus name, com.palm.service.<x>. The Photos routing derives it from the
#                     template id, so <x> is the same in both (boxnet, yandexdisk).
#   CLOUD_TEMPLATE    account template id, com.palm.<x>
#   CLOUD_ACCOUNT_DIR the template directory, relative to cloud/<name>: "account/com.palm.<x>"
#                     where it is nested, "account" where the template sits flat in it
#   CLOUD_AUTH_APP    the template's validator.customUI.appId. com.palm.app.cloud-auth (shipped
#                     by synergy-cloudcore) for the OAuth connectors; anything else is shipped
#                     here from cloud/<name>/apps/.

require cloud-synergy.inc

# The Accounts service only sees a new or removed template after a restart; see there.
require synergy-accounts-reload.inc

CLOUD_AUTH_APP ?= "com.palm.app.cloud-auth"

# OAuth app credentials that are the builder's own and stay out of the connectors repository and
# this layer: a connector's config.js carries "PLACEHOLDER_<NAME>_CLIENT_ID" and
# "PLACEHOLDER_<NAME>_CLIENT_SECRET", and they are filled in here from <NAME>_CLIENT_ID /
# <NAME>_CLIENT_SECRET when the builder sets those - in local.conf, or a file it includes that no
# repository holds (as SPOTIFY_CLIENT_ID for luneos-synergy-music). <NAME> is CLOUD_SECRET_NAME,
# the connector directory in capitals unless a recipe says otherwise (BOX, GDRIVE, ONEDRIVE).
# Unset, the placeholder stays and the account type cannot sign in; the build says so, as it does
# for a value set for a connector whose config.js has no placeholder for it. The values
# become part of do_install's task hash, so an sstate cache built with them is as private as they
# are.
CLOUD_SECRET_NAME ?= "${@d.getVar('CLOUD_CONNECTOR').upper()}"
CLOUD_CLIENT_ID = "${@d.getVar(d.getVar('CLOUD_SECRET_NAME') + '_CLIENT_ID') or ''}"
CLOUD_CLIENT_SECRET = "${@d.getVar(d.getVar('CLOUD_SECRET_NAME') + '_CLIENT_SECRET') or ''}"
do_install[vardeps] += "${CLOUD_SECRET_NAME}_CLIENT_ID ${CLOUD_SECRET_NAME}_CLIENT_SECRET"
# "1" where the provider takes a public PKCE client and the connector signs in without a secret
# (_cloudcore/node oauth.js treats an unfilled one as none), so its placeholder left is no warning.
CLOUD_SECRET_OPTIONAL ?= "0"

# The bus files are the connector's own, in cloud/<name>/files/sysbus/ (role, services.d entry,
# api-permissions, groups and client permissions, checked by tests/unit/cloudbus.test.js). They
# put every method in synergy-<id>.operation and grant it to the Accounts service, the
# validator app, the Accounts app (a customUI validator runs as an iframe inside it, so its
# calls arrive as com.palm.app.accounts-<pid>), the Documents/Photos apps and
# com.palm.service.photos.

do_install() {
    src=${S}/cloud/${CLOUD_CONNECTOR}

    # The service. Its service.js requires the shared runtime as ../_cloudcore/node, which
    # synergy-cloudcore installs into the same directory.
    install -d ${D}${webos_servicesdir}
    cp -R --no-preserve=ownership $src/service/${CLOUD_SERVICE} ${D}${webos_servicesdir}/

    # The builder's OAuth credentials, into the placeholders (see CLOUD_SECRET_NAME above).
    cfg=${D}${webos_servicesdir}/${CLOUD_SERVICE}/config.js
    fill_secret() {
        [ -n "$2" ] || return 0
        case "$2" in
            *[!A-Za-z0-9._~-]*) bbfatal "$1 holds characters a client id or secret does not" ;;
        esac
        # Set ahead of a connector that does not take it yet (kDrive before its OAuth change)
        # is not an error; the value just goes unused.
        if ! grep -q "\"PLACEHOLDER_$1\"" $cfg; then
            bbwarn "$1 is set but cloud/${CLOUD_CONNECTOR} has no PLACEHOLDER_$1; not used"
            return 0
        fi
        sed -i "s|\"PLACEHOLDER_$1\"|\"$2\"|" $cfg
    }
    fill_secret ${CLOUD_SECRET_NAME}_CLIENT_ID "${CLOUD_CLIENT_ID}"
    fill_secret ${CLOUD_SECRET_NAME}_CLIENT_SECRET "${CLOUD_CLIENT_SECRET}"
    left=$(grep -o '"PLACEHOLDER_[A-Z0-9_]*"' $cfg | tr -d '"' | tr '\n' ' ' || true)
    if [ "${CLOUD_SECRET_OPTIONAL}" = "1" ]; then
        left=$(echo "$left" | sed "s/PLACEHOLDER_${CLOUD_SECRET_NAME}_CLIENT_SECRET //")
    fi
    if [ -n "$left" ]; then
        bbwarn "${CLOUD_SERVICE} is built with $left- set them to sign in to this account type"
    fi

    # The account template, with its icons and localized copies. Installed under the template
    # id whether the source is nested or flat, as packaging/cloud/stage.sh does.
    install -d ${D}${webos_accttemplatesdir}/${CLOUD_TEMPLATE}
    cp -R --no-preserve=ownership $src/${CLOUD_ACCOUNT_DIR}/. ${D}${webos_accttemplatesdir}/${CLOUD_TEMPLATE}/

    # CLOUD_AUTH_APP is copied from the template by hand; fail here rather than ship an account
    # type whose validator app or bus grant points at the wrong app.
    tmpl=${D}${webos_accttemplatesdir}/${CLOUD_TEMPLATE}/${CLOUD_TEMPLATE}.json
    [ -f $tmpl ] || bbfatal "no template ${CLOUD_TEMPLATE}.json in cloud/${CLOUD_CONNECTOR}/${CLOUD_ACCOUNT_DIR}"
    grep -q '"appId"[[:space:]]*:[[:space:]]*"${CLOUD_AUTH_APP}"' $tmpl || \
        bbfatal "${CLOUD_TEMPLATE}.json does not name ${CLOUD_AUTH_APP} as its validator app"

    if [ "${CLOUD_AUTH_APP}" != "com.palm.app.cloud-auth" ]; then
        install -d ${D}${webos_applicationsdir}
        cp -R --no-preserve=ownership $src/apps/${CLOUD_AUTH_APP} ${D}${webos_applicationsdir}/
    fi

    # ACG, from the connector's own files.
    sysbus=$src/files/sysbus
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_rolesdir} \
               ${D}${webos_sysbus_apipermissionsdir} ${D}${webos_sysbus_groupsdir} \
               ${D}${webos_sysbus_permissionsdir}
    install -m 0644 $sysbus/${CLOUD_SERVICE}.service   ${D}${webos_sysbus_servicedir}/
    install -m 0644 $sysbus/${CLOUD_SERVICE}.role.json ${D}${webos_sysbus_rolesdir}/
    install -m 0644 $sysbus/${CLOUD_SERVICE}.api.json  ${D}${webos_sysbus_apipermissionsdir}/
    install -m 0644 $sysbus/${CLOUD_SERVICE}.groups.json ${D}${webos_sysbus_groupsdir}/
    install -m 0644 $sysbus/${CLOUD_SERVICE}.perm.json ${D}${webos_sysbus_permissionsdir}/
}

RDEPENDS:${PN} = "synergy-cloudcore"
