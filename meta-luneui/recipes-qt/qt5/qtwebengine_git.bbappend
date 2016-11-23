DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=use_proprietary_codecs"
EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=use_pulseaudio"
EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=enable_plugins=1"

inherit webos_ports_fork_repo

SRCREV_qtwebengine = "e5a861002f2736072259340cdd8ecca937be8e1c"
SRCREV_chromium = "a846f8521f1aa6f95e1b0ea9b1d31fdb95261a62"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
