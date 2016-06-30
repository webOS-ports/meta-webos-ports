inherit webos_ports_fork_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "16a33fe216ca7236c92e2c05d6242afc339c3bc6"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
