SUMMARY = "Qt 5/6 Library for ConnMan"
HOMEPAGE = "https://github.com/sailfishos/libconnman-qt"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://libconnman-qt/clockmodel.h;endline=8;md5=ea9f724050803f15d2d900ce3c5dac88"
DEPENDS += "qtbase qtdeclarative"

VERSION = "1.4.25"
PV = "${VERSION}+git"

SRCREV = "8aa2e17cd936afa2754992e68836a3c93aafbcf9"
# 0001-connman_vpn_manager.xml-Fix-build-with-Qt-6.5.patch was dropped at 1.4.25:
# upstream added the QtTypeName.Out1 annotation on ConnectionAdded themselves, so
# qdbusxml2cpp no longer chokes on the a{sv} out-argument.
SRC_URI = "git://github.com/sailfishos/libconnman-qt.git;protocol=https;branch=master"

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
