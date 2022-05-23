SUMMARY = "Voice call management engine"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://src/main.cpp;beginline=1;endline=18;md5=a513bca9b17080e89422924a39fd0eb0"

DEPENDS += "qtbase qtmultimedia libqofono"

PV = "0.7.14+git${SRCPV}"
SRCREV = "35f734b35899a7c57bd0547f8c0975cc4e2489bf"

inherit qt6-qmake
inherit systemd
inherit pkgconfig

SRC_URI = "git://github.com/herrie82/voicecall.git;protocol=https;branch=herrie/qt6 \
           file://voicecall-manager.service"

S = "${WORKDIR}/git"

#enable debugging in voicecall. This is now merged upstream so we don't need patches anymore to enable this for each individual file.
EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-debug"

# Separated build dirs doesn't work with this component currently due to the way it deals
# with its self build dependencies.
B = "${S}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "voicecall-manager.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/voicecall-manager.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += "${OE_QMAKE_PATH_QML}"
