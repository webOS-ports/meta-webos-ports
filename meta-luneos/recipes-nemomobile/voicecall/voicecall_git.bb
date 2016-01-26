SUMMARY = "Voice call management engine"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://src/main.cpp;beginline=1;endline=18;md5=a513bca9b17080e89422924a39fd0eb0"

DEPENDS += "qtbase qtmultimedia libqofono"

PV = "0.6.6+gitr${SRCPV}"
SRCREV = "b2c2b0abeeac5513e5226b97c1f7b4339eb54a3c"

inherit qmake5
inherit systemd

SRC_URI = "git://git.merproject.org/mer-core/voicecall.git;protocol=git;branch=master \
           file://0001-Adapt-thingstoworkinLuneOS.patch"

S = "${WORKDIR}/git"

# Separated build dirs doesn't work with this component currently due to the way it deals
# with its self build dependencies.
B = "${S}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "voicecall-manager.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/src/voicecall-manager.service ${D}${systemd_unitdir}/system/
}

FILES_${PN}-dbg += " \
    ${OE_QMAKE_PATH_QML}/*/*/*/.debug \
    ${libdir}/voicecall/plugins/.debug \
"
FILES_${PN} += "${OE_QMAKE_PATH_QML}"
