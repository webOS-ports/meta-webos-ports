DEPENDS += "luna-service2 pmloglib qtlocation"

#Temporary add these here until meta-qt5 is updated.
LIC_FILES_CHKSUM = " \
    file://src/core/browser_context_qt.cpp;md5=5fe719c44250955a5d5f8fb15fc8b1da;beginline=1;endline=35 \
    file://src/3rdparty/chromium/LICENSE;md5=537e0b52077bf0a616d0a0c8a79bc9d5 \
    file://LICENSE.LGPLv3;md5=8211fde12cc8a4e2477602f5953f5b71 \
    file://LICENSE.GPLv3;md5=88e2b9117e6be406b5ed6ee4ca99a705 \
    file://LICENSE.GPLv2;md5=c96076271561b0e3785dad260634eaa8 \
"

# Enable extra codecs
EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=use_proprietary_codecs"
EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=use_pulseaudio"

inherit webos_ports_fork_repo
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"

SRCREV_qtwebengine = "8a39e97ae6e17916ddd84bda6f3ae0cfbbcb8196"
SRCREV_chromium = "6fbccb8606a1252e325f1e9fc76ce495c376e45c"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
