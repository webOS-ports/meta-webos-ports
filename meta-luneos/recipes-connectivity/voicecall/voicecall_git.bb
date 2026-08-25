SUMMARY = "Voice call management engine"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://src/main.cpp;beginline=1;endline=18;md5=a513bca9b17080e89422924a39fd0eb0"

DEPENDS += "qtbase qtmultimedia libqofono"

PV = "0.9.0+git"
SRCREV = "8e6ec88c8f6588f07edb310379b19564359513b8"

inherit qt6-qmake
inherit systemd
inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/voicecall.git;protocol=https;branch=master \
           file://voicecall-manager.service \
           file://0001-Support-building-against-Qt-6.patch \
           file://0002-Build-cell-broadcast-support-without-mlite.patch \
"

#enable debugging in voicecall. This is now merged upstream so we don't need patches anymore to enable this for each individual file.
EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-debug VOICECALL_INSTALL_LIBDIR=${libdir}"

# Separated build dirs doesn't work with this component currently due to the way it deals
# with its self build dependencies.
B = "${S}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "voicecall-manager.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/voicecall-manager.service ${D}${systemd_unitdir}/system/
}

# The cell broadcast controller reads its channel catalog from
# /usr/share/cell-broadcast-provider-info/channels.json. Without it cell
# broadcast just reports itself unavailable; calls are unaffected, so recommend
# rather than depend.
RRECOMMENDS:${PN} += "cell-broadcast-provider-info"

FILES:${PN} += "${OE_QMAKE_PATH_QML} \
                ${libdir}/oneshot.d \
                ${datadir}/ngfd \
"
