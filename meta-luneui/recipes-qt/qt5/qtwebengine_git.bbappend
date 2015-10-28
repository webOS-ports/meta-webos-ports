DEPENDS += "luna-service2 pmloglib"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
PACKAGECONFIG = "gstreamer qtlocation qtsensors"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "857219986db5dcb8f9e05ff95b15a4e2cfb3a47e"
SRCREV_chromium = "497eb33186d20f7d09fec55dee39a95edc744b7d"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
