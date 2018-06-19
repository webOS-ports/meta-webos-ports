FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "luna-service2 pmloglib qtlocation"

# Chromium uses libpci to determine which optimizations/workarounds to apply
RDEPENDS_${PN}_append_x86 = " libpci"

# Enable proprietary codecs (for MP3 etc), pepper-plugins (Flash & WideVine), Print to PDF, spellchecker, WebRTC & embedded build
EXTRA_QMAKEVARS_CONFIGURE += "-proprietary-codecs -pepper-plugins -printing-and-pdf -spellchecker -webrtc"
# Activate some more detailed debug info
# EXTRA_QMAKEVARS_PRE += " CONFIG+=force_debug_info CONFIG+=webcore_debug "

SRC_URI += " \
    file://qtwebengine.conf \
    file://0001-WebEngineView-provide-additionnal-features-from-wind.patch \
    file://0002-WebEngineNewViewRequest-provide-the-requested-URL-as.patch \
    file://0003-Implement-Sync-IPC-messaging-through-qt.webChannelTr.patch \
    file://0004-WebEngineSettings-Add-LuneOS-fonts-properties.patch \
    file://0005-Store-the-additional-window-features-required-by-the.patch \
    file://0006-QQuickWebEngineNewViewRequest-use-initialTargetUrl-f.patch \
    file://0007-WebEngineNewViewRequest-add-possibility-to-get-reque.patch \
    file://0008-GetScreenInfo-use-default-screen-when-no-window-is-a.patch \
    file://0009-Enable-password-echo.patch \
    file://0010-Implement-RequestQuotePermission.patch \
    file://0011-WebEngineView-re-introduce-devicePixelRatio-property.patch \
    file://0012-WebEngineView-add-extraContextMenuEntriesComponent-p.patch \
    file://0013-gn_generator-fix-debug-build.patch \
    file://chromium/0001-WebContents-provide-additional-features-from-window..patch;patchdir=src/3rdparty \
    file://chromium/0002-WindowFeatures-Chrome-lower-the-minimum-height-to-5.patch;patchdir=src/3rdparty \
    file://chromium/0003-Enable-password-echo.patch;patchdir=src/3rdparty \
    file://chromium/0004-storage-browser-quota-workaround-for-crash-on-cache-.patch;patchdir=src/3rdparty \
    file://chromium/0005-html.css-themeWin.css-Add-Prelude-as-default-font-in.patch;patchdir=src/3rdparty \
    file://chromium/0006-Fix-webGL2-Textures.patch;patchdir=src/3rdparty \
"

do_install_append() {
    #Create the chromium folder already so users can right away push the required plugins there
    mkdir -p ${D}${libdir}/chromium

    #Install qtwebengine.conf, which contains all the environment variables needed to start qtwebengine
    install -d ${D}${sysconfdir}/luna-next
    install -m 0644 ${WORKDIR}/qtwebengine.conf ${D}${sysconfdir}/luna-next/
}

FILES_${PN} += "${libdir}/chromium ${sysconfdir}/luna-next/"
