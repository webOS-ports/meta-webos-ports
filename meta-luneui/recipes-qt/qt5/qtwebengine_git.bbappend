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
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master-next-5.15"

# webOS-ports/master-next-20200511-5.15
SRCREV_qtwebengine = "943b44d69c28bf4447dd553fa79d82220030dd5b"
# webOS-ports/master-next-20200511-5.15
SRCREV_chromium = "332ad7f5f75d8084766a89ad8b34c8344bc0ead1"

do_install_append() {
    #Create the chromium folder already so users can right away push the required plugins there
    mkdir -p ${D}${libdir}/chromium

    #Install qtwebengine.conf, which contains all the environment variables needed to start qtwebengine
    install -d ${D}${sysconfdir}/luna-next
    install -m 0644 ${WORKDIR}/qtwebengine.conf ${D}${sysconfdir}/luna-next/
}

FILES_${PN} += "${libdir}/chromium ${sysconfdir}/luna-next/"
