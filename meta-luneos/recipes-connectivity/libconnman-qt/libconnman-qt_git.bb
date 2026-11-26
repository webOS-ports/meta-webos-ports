SUMMARY = "Qt 5/6 Library for ConnMan"
HOMEPAGE = "https://github.com/sailfishos/libconnman-qt"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://libconnman-qt/clockmodel.h;endline=8;md5=ea9f724050803f15d2d900ce3c5dac88"
DEPENDS += "qtbase qtdeclarative"

VERSION = "1.4.25"
PV = "${VERSION}+git"

SRCREV = "105761b334c9ce5461d24a32b69dea39635b6cdf"
# 0001-connman_vpn_manager.xml-Fix-build-with-Qt-6.5.patch was dropped at 1.4.25:
# upstream added the QtTypeName.Out1 annotation on ConnectionAdded themselves, so
# qdbusxml2cpp no longer chokes on the a{sv} out-argument.
SRC_URI = "git://github.com/sailfishos/libconnman-qt.git;protocol=https;branch=master"
# The fix for queued signals overflowing a quint64 (67 signals) was the old
# 0001; upstream merged it as sailfishos PR #56. 0001 needs it and adds
# NetworkService.useDeviceMACAddress for connman's per-network MAC address
# opt-out (connman 0008). It stays here: their connman has no per-network MAC
# address for it to control. 0002 answers connman's RequestBrowser, which
# otherwise holds up the agent's other requests.
SRC_URI += " \
    file://0001-libconnman-qt-Add-NetworkService.useDeviceMACAddress.patch \
    file://0002-libconnman-qt-Answer-RequestBrowser.patch \
"

inherit pkgconfig
inherit qt6-qmake

EXTRA_QMAKEVARS_PRE = "CONFIG+=no-module-prefix VERSION=${VERSION}"

RDEPENDS:${PN} += "connman"

do_install:append() {
    if ls ${D}${libdir}/pkgconfig/connman-qt5.pc >/dev/null 2>/dev/null; then
        sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/connman-qt5.pc
    fi
}
FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/Connman/ \
"
FILES:${PN}-dev += " \
    ${libdir}/libconnman-qt6.prl \
"
# ERROR: libconnman-qt-1.3.3+git-r0 do_package_qa: QA Issue: File /usr/lib/libconnman-qt6.prl in package libconnman-qt-dev contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"

# Same .prl issue webos_qmake6.bbclass handles for the qmake6 recipes: qmake
# records QMAKE_PRL_BUILD_DIR = ${B}, which meta-qt6's sanitising pass does not
# cover because it only rewrites the staging directories, so the build path
# ships in the -dev package and trips buildpaths QA. This recipe does not
# inherit that class, so drop the line here too.
do_install:append() {
    find ${D} -name "*.prl" -exec sed -i -e '/^QMAKE_PRL_BUILD_DIR/d' {} \;
}
