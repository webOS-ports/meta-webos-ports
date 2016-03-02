DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=proprietary_codecs"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "e31654aabfac010a6a0e8f24c933a0db9e7d6dfb"
SRCREV_chromium = "cb8a8ebcb80ebc247119fcfae03188b4960bc896"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"

do_fetch[vardeps] += "SRCREV_chromium"

