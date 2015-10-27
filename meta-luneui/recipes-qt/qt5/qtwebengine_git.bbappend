inherit webos_ports_fork_repo

SRCREV_qtwebengine = "87a22df809382984b3a6b7ebbebc6ea21ad06e06"
SRCREV_chromium = "497eb33186d20f7d09fec55dee39a95edc744b7d"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium${WEBOS_GIT_BRANCH};name=chromium;destsuffix=git/src/3rdparty \
"
