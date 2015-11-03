DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=proprietary_codecs"

inherit webos_ports_fork_repo
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"

SRCREV_qtwebengine = "a738dcd61b3f2ce4afc6726e7bbfbef1adaa27d8"
SRCREV_chromium = "5d15465955e43ea46c8639492cc630b5a2a7b913"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
