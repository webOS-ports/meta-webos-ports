inherit webos_ports_fork_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "f9d440bbedbf0a1daae43c05838d415f49893a73"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
