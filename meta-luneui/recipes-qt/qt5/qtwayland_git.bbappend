inherit webos_ports_fork_repo

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "b2d181c5b5c732165a6ee791c07a39a849bc4d6d"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
