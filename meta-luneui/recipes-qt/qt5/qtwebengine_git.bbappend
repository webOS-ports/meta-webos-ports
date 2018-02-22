FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "luna-service2 pmloglib qtlocation"

# Chromium uses libpci to determine which optimizations/workarounds to apply
RDEPENDS_${PN}_append_x86 = " libpci"

# Enable proprietary codecs (for MP3 etc), pepper-plugins (Flash & WideVine), Print to PDF, spellchecker, WebRTC & embedded build
# EXTRA_QMAKEVARS_CONFIGURE += "-proprietary-codecs -pepper-plugins -printing-and-pdf -spellchecker -webrtc"
# Activate some more detailed debug info
# EXTRA_QMAKEVARS_PRE += " CONFIG+=force_debug_info CONFIG+=webcore_debug "

do_install_append() {
    #Create the chromium folder already so users can right away push the required plugins there
    mkdir -p ${D}${libdir}/chromium
}

FILES_${PN} += "${libdir}/chromium"
