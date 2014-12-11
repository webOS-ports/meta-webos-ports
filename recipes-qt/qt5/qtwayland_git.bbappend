inherit webos_ports_fork_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "a0c5ba81f4646e574eb1e436f87bde55da46942c"

# don't remove create_cmake
EXTRA_QMAKEVARS_POST_remove = "CONFIG-=create_cmake"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
