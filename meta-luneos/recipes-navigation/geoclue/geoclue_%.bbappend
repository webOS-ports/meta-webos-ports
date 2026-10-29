# WiFi geolocation goes to geoclue's build-time default, BeaconDB
# (meson option default-wifi-url, https://api.beacondb.net/v1/geolocate), which
# needs no API key. LuneOS used to patch in Google's service with a project API
# key committed to this layer; that key is public and is to be revoked.
#
# To use Google's Geolocation API instead, set your own key in local.conf or a
# file it includes that no repository holds (luneos-secrets' secrets.conf):
#
#   GEOCLUE_GOOGLE_API_KEY = "<key>"
#
# The key then ends up in /etc/geoclue/geoclue.conf of the image, readable by
# anyone with the image, and in do_install's task hash, so an sstate cache built
# with it is as private as the key. Restrict it in the Google console (API
# restriction: Geolocation API only).
GEOCLUE_GOOGLE_API_KEY ?= ""

do_install:append() {
    if [ -n "${GEOCLUE_GOOGLE_API_KEY}" ]; then
        case "${GEOCLUE_GOOGLE_API_KEY}" in
            *[!A-Za-z0-9_-]*) bbfatal "GEOCLUE_GOOGLE_API_KEY holds characters a Google API key does not" ;;
        esac
        conf=${D}${sysconfdir}/geoclue/geoclue.conf
        grep -q '^#url=https://www.googleapis.com/geolocation/v1/geolocate?key=YOUR_KEY$' "$conf" || \
            bbfatal "geoclue.conf has no commented-out Google URL to enable; check the template of ${PV}"
        sed -i 's|^#url=https://www.googleapis.com/geolocation/v1/geolocate?key=YOUR_KEY$|url=https://www.googleapis.com/geolocation/v1/geolocate?key=${GEOCLUE_GOOGLE_API_KEY}|' "$conf"
    fi
}

# Avoid putting a dependency on modemmanager
PACKAGECONFIG = "nmea lib"
