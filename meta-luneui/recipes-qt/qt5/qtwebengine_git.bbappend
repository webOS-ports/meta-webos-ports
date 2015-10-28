DEPENDS += "luna-service2 pmloglib"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
PACKAGECONFIG = "gstreamer qtlocation qtsensors"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "8b5bfb9bdb48ee4308003cf54a37f01cb5c362a6"
SRCREV_chromium = "497eb33186d20f7d09fec55dee39a95edc744b7d"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
