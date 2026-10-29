# Copyright (c) 2020-2023 LG Electronics, Inc.

DESCRIPTION = "location framework which provides location based services implementing location handlers, plugins, and Luna location service"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/location"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=7be9908f876cc5f1edaf1124d0084067 \
"

DEPENDS = "glib-2.0 libpbnjson libxml2 pmloglib luna-service2 luna-prefs loc-utils boost"

SRCREV = "a86e28803af0d5b02b13b4b448ea6e72f5ca42f4"

PV = "1.0.0-109"
PR = "r4"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += "${libdir}/location/plugins/lib*.so"

# Google API key for the Google network-location backend and for geocoding.
# None is committed anywhere: the one the component once shipped in
# /etc/location/wsp.conf is public and is to be revoked. Network positions use
# beaconDB, the default, which needs no key. A builder with its own key sets it
# in local.conf, or in a file it includes that no repository holds
# (luneos-secrets' secrets.conf):
#
#   GOOGLE_LOCATION_API_KEY = "<key>"
#
# It is written base64-encoded into "apiKey" of the installed wsp.conf, which
# is how the service reads it. Unset, "apiKey" is emptied, whatever the
# component ships. The key is readable by anyone with the image and is part of
# do_install's task hash, so an sstate cache built with it is as private as
# the key. Restrict it in the Google console to the Geolocation and
# Geocoding APIs.
GOOGLE_LOCATION_API_KEY ?= ""

python luneos_location_set_api_key() {
    import base64, re
    key = d.getVar('GOOGLE_LOCATION_API_KEY') or ''
    if key and not re.fullmatch(r'[A-Za-z0-9_-]+', key):
        bb.fatal('GOOGLE_LOCATION_API_KEY holds characters a Google API key does not')
    conf = os.path.join(d.getVar('D') + d.getVar('sysconfdir'), 'location', 'wsp.conf')
    with open(conf) as f:
        text = f.read()
    encoded = base64.b64encode(key.encode()).decode() if key else ''
    text, n = re.subn(r'("apiKey"\s*:\s*")[^"]*(")', lambda m: m.group(1) + encoded + m.group(2), text)
    if n != 1:
        bb.fatal('%s: expected one "apiKey" entry, found %d' % (conf, n))
    with open(conf, 'w') as f:
        f.write(text)
}
do_install[postfuncs] += "luneos_location_set_api_key"
