inherit webos_ports_fork_repo

SRCREV_qtwebengine = "282f6b5ad2ff3a7c3d2bdc543235d1a90d5ac789"
SRCREV_chromium = "643a53c0d84a1d9578d3011105eb34c68f1c0176"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
