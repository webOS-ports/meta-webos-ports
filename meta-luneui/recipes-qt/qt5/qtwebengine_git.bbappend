DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE = "WEBENGINE_CONFIG+=proprietary_codecs"

inherit webos_ports_fork_repo
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"

SRCREV_qtwebengine = "64dc919d31a6ecf818365025eea7b532c5db7eb3"
SRCREV_chromium = "7e655fbb76c155541d8f3a7aa8164f1c551f2777"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
