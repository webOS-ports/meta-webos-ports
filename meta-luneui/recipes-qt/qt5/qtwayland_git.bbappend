inherit webos_ports_fork_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "50d6c0ae941bfb7089743351f600e595e817828b"

# don't remove create_cmake
EXTRA_QMAKEVARS_POST_remove = "CONFIG-=create_cmake"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
