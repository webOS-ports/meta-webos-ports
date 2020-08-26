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
# webOS-ports/master-20200527
SRCREV_qtwebengine = "7b53f3018b25f92fdccdb438a204b82543835d0e"
# webOS-ports/master-20200826
SRCREV_chromium = "7a95dc3f791a49cf97cbe278f4a75fc95fa57a54"

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
