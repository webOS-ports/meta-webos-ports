DEPENDS += "luna-service2 pmloglib"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
# Disabled for now, seems it also doesn't work with gstreamer currently.
# PACKAGECONFIG = "gstreamer qtlocation qtsensors"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "d9ff659726db7c583adc99b3a7bf149be01d63f7"
SRCREV_chromium = "55d8f2d6e3761e0dc6fb5efa13ac606322360f29"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
