inherit webos_ports_fork_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"
SRCREV = "472c909a8f36e2ba897624dd224ef22b01b4d1f8"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
