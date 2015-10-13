inherit webos_ports_fork_repo

SRCREV_qtwebengine = "ec0a2f759cd1fb2675ae6f761c6172004cf8af23"
SRCREV_chromium = "f56a7c96049bda8b60a281de334dc830982e2825"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
