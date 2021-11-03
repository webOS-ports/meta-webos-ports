FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "luna-service2 pmloglib"

# Enable proprietary codecs (for MP3 etc), pepper-plugins (Flash & WideVine), Print to PDF, spellchecke
PACKAGECONFIG += "proprietary-codecs pepper-plugins printing-and-pdf spellchecker webrtc"
# Activate some more detailed debug info
# EXTRA_QMAKEVARS_PRE += " CONFIG+=force_debug_info CONFIG+=webcore_debug "

inherit webos_ports_fork_repo

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium${WEBOS_GIT_BRANCH};destsuffix=git/src/3rdparty;protocol=https \
    file://qtwebengine.conf \
"
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master"
# webOS-ports/master-20210429
SRCREV_qtwebengine = "dd7f7a9166186c7fa0fc023324cedb85f02ac6c1"
# webOS-ports/master-20210804
SRCREV_chromium = "7aad021f2edbfc2714365efe2174f3a239c66ac2"

do_install:append() {
    # Create the chromium folder already so users can right away push the required plugins there
    mkdir -p ${D}${libdir}/chromium

    # Install qtwebengine.conf, which contains all the environment variables needed to start qtwebengine
    install -d ${D}${sysconfdir}/luna-next
    install -m 0644 ${WORKDIR}/qtwebengine.conf ${D}${sysconfdir}/luna-next/

    # Store the Chromium version so it can be picked up by LuneOS Components in order to update the UserAgent with the correct Qt and Chromium version automatically
    mkdir -p ${D}${datadir}/qtwebengine
    grep "^chromium_version" ${S}/tools/scripts/version_resolver.py | awk -F  "'" '/1/ {print $2}' > ${D}${datadir}/qtwebengine/chromium-version.txt
    echo ${QT_MODULE_BRANCH} > ${D}${datadir}/qtwebengine/qt-version.txt
}

FILES:${PN} += "${libdir}/chromium ${sysconfdir}/luna-next/"
