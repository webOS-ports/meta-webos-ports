DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE = "WEBENGINE_CONFIG+=proprietary_codecs"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "e31654aabfac010a6a0e8f24c933a0db9e7d6dfb"
SRCREV_chromium = "55d8f2d6e3761e0dc6fb5efa13ac606322360f29"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"

do_fetch[vardeps] += "SRCREV_chromium"

