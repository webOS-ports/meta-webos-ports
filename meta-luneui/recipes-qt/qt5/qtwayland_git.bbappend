inherit webos_ports_fork_repo

SRC_URI = "git://github.com/webos-ports/qtwayland;protocol=git;branch=webOS-ports/master-next"
SRCREV = "b6d6e04caca02e99ac67fd1a1ea0af77223b8424"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
