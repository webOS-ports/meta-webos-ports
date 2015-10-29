DEPENDS += "luna-service2 pmloglib"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
# Disabled for now, seems it also doesn't work with gstreamer currently.
# PACKAGECONFIG = "gstreamer qtlocation qtsensors"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "857219986db5dcb8f9e05ff95b15a4e2cfb3a47e"
SRCREV_chromium = "18dd25cd76ab7db6bceba541de7635aa25bcd228"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
