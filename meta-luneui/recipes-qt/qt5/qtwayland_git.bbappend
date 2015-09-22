inherit webos_ports_fork_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "cc7c50fdc35d5e765d1c011f6a494b06562ef14c"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
