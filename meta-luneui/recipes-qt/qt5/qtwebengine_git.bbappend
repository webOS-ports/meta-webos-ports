DEPENDS += "luna-service2 pmloglib"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
# Disabled for now, seems it also doesn't work with gstreamer currently.
# PACKAGECONFIG = "gstreamer qtlocation qtsensors"

inherit webos_ports_fork_repo
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"

SRCREV_qtwebengine = "6ded9295000039be360cd134542fb788d032958d"
SRCREV_chromium = "3f6319f4faa731f4363a94ac94ee1b3e3d48576c"

QT_MODULE_BRANCH_CHROMIUM = "${WEBOS_GIT_PARAM_BRANCH}"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
"
