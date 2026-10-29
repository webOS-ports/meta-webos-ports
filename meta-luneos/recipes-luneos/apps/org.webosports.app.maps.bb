SUMMARY = "Google Maps app re-written from scratch in Enyo 2"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_application
inherit webos_app

PV = "0.0.1+git"
SRCREV = "6d5852620a72b269c2cec113e2ab5dbcec88da1a"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# Google Maps API key for the optional Google engine. None is committed
# anywhere: the app's sources carry "PLACEHOLDER_GOOGLE_MAPS_API_KEY" (the key
# they once had is public and is to be revoked), and the builder's own key goes
# in its place here - set it in local.conf, or in a file it includes that no
# repository holds (luneos-secrets' secrets.conf):
#
#   GOOGLE_MAPS_API_KEY = "<key>"
#
# Unset, the placeholder stays and the app offers only its open engine. The
# key is readable by anyone with the image and is part of do_install's task
# hash, so an sstate cache built with it is as private as the key. Restrict it
# in the Google console to the Maps JavaScript, Places, Directions, Street View
# Static and Maps Static APIs, and to LuneOS' referrer where possible.
#
# Needs a SRCREV with the placeholder (org.webosports.app.maps branch
# herrie/security-WP4-no-google-key); until then the build warns.
GOOGLE_MAPS_API_KEY ?= ""

do_install:append() {
    app=${D}${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}
    files=$(grep -rl 'PLACEHOLDER_GOOGLE_MAPS_API_KEY' "$app" || true)
    if [ -z "$files" ]; then
        bbwarn "${PN} has no PLACEHOLDER_GOOGLE_MAPS_API_KEY; GOOGLE_MAPS_API_KEY is not used"
    elif [ -n "${GOOGLE_MAPS_API_KEY}" ]; then
        case "${GOOGLE_MAPS_API_KEY}" in
            *[!A-Za-z0-9_-]*) bbfatal "GOOGLE_MAPS_API_KEY holds characters a Google API key does not" ;;
        esac
        echo "$files" | while read -r f; do
            sed -i 's|PLACEHOLDER_GOOGLE_MAPS_API_KEY|${GOOGLE_MAPS_API_KEY}|g' "$f"
        done
    fi
}
