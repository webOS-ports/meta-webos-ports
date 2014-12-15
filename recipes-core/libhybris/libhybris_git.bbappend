DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

inherit webos_ports_repo

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

SRCREV = "69d82b1aee617388c90c1888e1c5b5faf3a6ed1b"
