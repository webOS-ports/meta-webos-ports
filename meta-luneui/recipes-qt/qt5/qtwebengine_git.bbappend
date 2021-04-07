FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "luna-service2 pmloglib"

# Enable proprietary codecs (for MP3 etc), pepper-plugins (Flash & WideVine), Print to PDF, spellchecke
PACKAGECONFIG += "proprietary-codecs pepper-plugins printing-and-pdf spellchecker webrtc"
# Activate some more detailed debug info
# EXTRA_QMAKEVARS_PRE += " CONFIG+=force_debug_info CONFIG+=webcore_debug "

inherit webos_ports_fork_repo

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE};name=qtwebengine \
    ${WEBOS_PORTS_GIT_REPO}/qtwebengine-chromium;name=chromium${WEBOS_GIT_BRANCH};destsuffix=git/src/3rdparty \
    file://qtwebengine.conf \
"
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next"
# webOS-ports/master-next-20210311b
SRCREV_qtwebengine = "89c6a9c79b7c1e13e366693ef5bcd2e15e8495cf"
# webOS-ports/master-next-20210311e
SRCREV_chromium = "687d322faec50e018c684bf4830ceb40b01d7959"

do_install_append() {
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

FILES_${PN} += "${libdir}/chromium ${sysconfdir}/luna-next/"
