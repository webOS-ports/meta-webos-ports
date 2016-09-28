DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=use_proprietary_codecs"
EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=use_pulseaudio"
EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=enable_plugins=1"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "76924fcfd3ed239cbc19672a8c682ac776a671a7"
SRCREV_chromium = "33a68cc60b69a3b0d5831bac2517e6bd5681c93c"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
