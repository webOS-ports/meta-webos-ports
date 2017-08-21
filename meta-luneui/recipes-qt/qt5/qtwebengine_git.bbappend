FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "luna-service2 pmloglib qtlocation"

# Enable extra codecs
#FIXME for GYN
#EXTRA_QMAKEVARS_PRE += "WEBENGINE_CONFIG+=use_proprietary_codecs"
#EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=use_pulseaudio"
#EXTRA_QMAKEVARS_PRE += "GYP_CONFIG+=enable_plugins=1"

#SRC_URI += " \
#    file://0001-Add-PalmSystemBridge-feature.patch \
#    file://0002-WebEngineView-provide-additionnal-features-from-wind.patch \
#    file://0003-WebEngineNewViewRequest-provide-the-requested-URL-as.patch \
#    file://0004-Implement-Sync-IPC-messaging-through-qt.webChannelTr.patch \
#    file://0005-Make-properties-for-some-settings-for-PalmBridgeServ.patch \
#    file://0006-WebEngineSettings-Add-a-standardFontFamily-property-.patch \
#    file://0007-WebEngineSettings-add-also-Serif-Fixed-and-Cursive-f.patch \
#    file://0008-Store-the-additional-window-features-required-by-the.patch \
#    file://0009-QQuickWebEngineNewViewRequest-use-initialTargetUrl-f.patch \
#    file://0010-WebEngineNewViewRequest-add-possibility-to-get-reque.patch \
#    file://0011-GetScreenInfo-use-default-screen-when-no-window-is-a.patch \
#    file://0012-Enable-password-echo.patch \
#    file://0013-Implement-RequestQuotePermission.patch \
#    file://0014-WebEngineView-re-introduce-devicePixelRatio-property.patch \
#    file://0015-WebEngineView-add-extraContextMenuEntriesComponent-p.patch \
#    file://chromium/0001-Add-PalmServiceBridge-to-WebEngine.patch;patchdir=./src/3rdparty \
#    file://chromium/0002-WebContents-provide-additional-features-from-window..patch;patchdir=./src/3rdparty \
#    file://chromium/0003-Store-the-additional-window-features-related-to-Lune.patch;patchdir=./src/3rdparty \
#    file://chromium/0004-Fix-JSON-additional-window-features-parsing.patch;patchdir=./src/3rdparty \
#    file://chromium/0005-WindowFeatures-Chrome-lower-the-minimum-height-to-5.patch;patchdir=./src/3rdparty \
#    file://chromium/0006-Enable-password-echo.patch;patchdir=./src/3rdparty \
#    file://chromium/0007-storage-browser-quota-workaround-for-crash-on-cache-.patch;patchdir=./src/3rdparty \
#    file://chromium/0008-html.css-themeWin.css-Add-Prelude-as-default-font-in.patch;patchdir=./src/3rdparty \
#    file://chromium/0010-PalmServiceBridge-adapt-to-Chromium-53-code-base.patch;patchdir=./src/3rdparty \
#    file://chromium/0011-Disable-some-sandboxing-capabilities.patch;patchdir=./src/3rdparty \
#    "
