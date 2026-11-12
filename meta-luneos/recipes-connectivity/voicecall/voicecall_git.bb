SUMMARY = "Voice call management engine"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://src/main.cpp;beginline=1;endline=18;md5=a513bca9b17080e89422924a39fd0eb0"

DEPENDS += "qtbase qtmultimedia qtpositioning libqofono"

PV = "0.9.0+git"
SRCREV = "ae99f39867569f691d2d909a75468ec5fa1b7d63"

inherit qt6-qmake
inherit systemd
inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/voicecall.git;protocol=https;branch=master \
           file://voicecall-manager.service \
           file://voicecall-manager.role.json \
           file://voicecall-manager.perm.json \
           file://voicecall-manager.manifest.json \
           file://0001-voicecall-Support-building-against-Qt-6.patch \
           file://0002-cellbroadcast-Build-cell-broadcast-support-without-m.patch \
           file://0003-voicecall-Report-an-ended-incoming-call-as-disconnec.patch \
           file://0004-voicecall-Implement-merge-and-split-in-the-ofono-plu.patch \
           file://0005-voicecall-Only-use-Q_MOC_INCLUDE-on-Qt-6.patch \
           file://0006-voicecall-Build-warning-clean-against-Qt-6.patch \
           file://0007-build-Build-warning-clean-with-GCC-and-Clang.patch \
           file://0008-voicecall-Forget-a-provider-s-calls-when-the-provide.patch \
           file://0009-voicecall-Don-t-keep-plugins-that-failed-to-configur.patch \
           file://0010-playback-manager-Don-t-un-mute-the-microphone-when-m.patch \
           file://0011-oneshot-Don-t-follow-user-controlled-links-when-movi.patch \
           file://0012-commhistory-Log-missed-calls-and-call-start-times-co.patch \
           file://0013-ofono-Fix-call-state-direction-duration-and-start-ti.patch \
           file://0014-telepathy-Fix-farstream-callback-lifetimes-DTMF-and-.patch \
           file://0015-declarative-Fix-recordings-reconnection-and-conferen.patch \
           file://0016-cellbroadcast-Keep-suppressed-and-dismissed-alerts-d.patch \
           file://0017-tests-Add-a-hardening-harness-sanitizers-valgrind-an.patch \
           file://0018-ofono-Test-merge-and-split-also-with-two-modems.patch \
           file://0019-ofono-Make-providers-for-modems-that-exist-when-voic.patch \
           file://0020-voicecall-Keep-an-ended-call-on-D-Bus-until-its-last.patch \
           file://0021-cellbroadcast-Keep-the-alerts-oFono-sends-without-up.patch \
           file://0022-tests-Leave-the-D-Bus-test-out-of-the-TSan-run.patch \
           file://0023-tests-Drop-the-analysis-findings-the-last-fixes-took.patch \
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

    # The cell broadcast geofence asks the LuneOS Qt position plugin for a
    # fix. That plugin calls com.webos.service.location over luna-service2 as
    # qtpositioning_<applicationName>, so voicecall-manager needs a role that
    # may claim that name and the location.query group for it; without them
    # the plugin cannot register and every geofenced alert waits out its
    # deadline and is shown regardless of where the phone is. The hub only
    # reads role and permission files that a manifest lists.
    install -d ${D}${datadir}/luna-service2/roles.d \
               ${D}${datadir}/luna-service2/client-permissions.d \
               ${D}${datadir}/luna-service2/manifests.d
    install -m 0644 ${UNPACKDIR}/voicecall-manager.role.json ${D}${datadir}/luna-service2/roles.d/
    install -m 0644 ${UNPACKDIR}/voicecall-manager.perm.json ${D}${datadir}/luna-service2/client-permissions.d/
    install -m 0644 ${UNPACKDIR}/voicecall-manager.manifest.json ${D}${datadir}/luna-service2/manifests.d/
}

# The cell broadcast controller reads its channel catalog from
# /usr/share/cell-broadcast-provider-info/channels.json. Without it cell
# broadcast just reports itself unavailable; calls are unaffected, so recommend
# rather than depend.
RRECOMMENDS:${PN} += "cell-broadcast-provider-info"

FILES:${PN} += "${OE_QMAKE_PATH_QML} \
                ${libdir}/oneshot.d \
                ${datadir}/ngfd \
                ${datadir}/luna-service2 \
"

# The unit tests (tests/tests.pro) install under /opt/tests/voicecall, with
# a tests.xml for test-definition runners. They run on the device without
# a modem: the oFono, mlite and position source parts are faked.
PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests = "/opt/tests"

# QtTest compiles the build directory into every test as
# QT_TESTCASE_BUILDDIR, QFINDTESTDATA's last fallback, which leaves TMPDIR
# in the binaries [buildpaths]. The tests look next to their own binary
# first; point the fallback at the installed tests too. qmake only writes
# the subdirectory Makefiles when make gets there, so have them written
# now (qmake_all). B is S, so also drop test objects of an earlier build:
# make does not rebuild them for a changed define.
do_configure:append() {
    oe_runmake -C ${B} qmake_all
    sed -i -e "s#QT_TESTCASE_BUILDDIR='[^']*'#QT_TESTCASE_BUILDDIR='\"/opt/tests/voicecall\"'#" \
        ${B}/tests/Makefile.*
    find ${B}/tests -name '*.o' -delete
}
