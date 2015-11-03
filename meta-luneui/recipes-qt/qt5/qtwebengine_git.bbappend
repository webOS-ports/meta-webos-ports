DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=proprietary_codecs"

inherit webos_ports_fork_repo
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"

SRCREV_qtwebengine = "8a39e97ae6e17916ddd84bda6f3ae0cfbbcb8196"
SRCREV_chromium = "6fbccb8606a1252e325f1e9fc76ce495c376e45c"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
