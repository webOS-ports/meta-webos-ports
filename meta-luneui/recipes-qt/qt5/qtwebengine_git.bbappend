DEPENDS += "luna-service2 pmloglib qtlocation"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
# PACKAGECONFIG = "gstreamer qtlocation qtsensors"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "4ca5ff38d7fbe43233832207f08b147b6a14c496"
SRCREV_chromium = "55d8f2d6e3761e0dc6fb5efa13ac606322360f29"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"

do_fetch[vardeps] += "SRCREV_chromium"

