SUMMARY = "QtWebEngine combines the power of Chromium and Qt"
LICENSE = "LGPL-2.1 & BSD"
LIC_FILES_CHKSUM = " \
  file://src/core/browser_context_qt.cpp;md5=c23e24b6a534c8b6cc879a397b35db29;beginline=1;endline=40 \
  file://src/3rdparty/chromium/LICENSE;md5=d2d164565cc10f298390174d9cb6d18d \
"

PV = "0.9.0+git${SRCPV}"

DEPENDS += " \
    ninja-native \
    qtbase qtdeclarative qtxmlpatterns qtquickcontrols \
    libdrm fontconfig pixman openssl pango cairo icu pciutils \
"

inherit qmake5
inherit gettext
inherit pythonnative
inherit perlnative

SRC_URI = " \
    git:///home/morphis/work/wop/work/qtwebengine-snapshots;protocol=file;branch=master \
    file://0001-Drop-build-time-only-dependency-on-x11-libraries.patch \
    file://0002-Use-ninja-supplied-by-environment-variable-NINJA_PAT.patch \
    file://0001-Strip-unwanted-echo-compiling-prefox-from-CC.patch \
"
S = "${WORKDIR}/git"

SRCREV = "29fd954370add1d083d3c2688416252d0d7f8f95"

# To avoid trouble start with not separated build directory
SEPB = "${S}"
B = "${SEPB}"

do_configure() {
    # replace LD with CXX, to workaround a possible gyp inheritssue?
    LD="${CXX}" export LD
    CC="${CC}" export CC
    CXX="${CXX}" export CXX
    CC_host="gcc" export CC_host
    CXX_host="g++" export CXX_host

    ${OE_QMAKE_QMAKE} -r QTWEBENGINE_ROOT="${S}" NINJA_PATH="${STAGING_BINDIR_NATIVE}/ninja" \
        QMAKE_CXX="${OE_QMAKE_CXX}" QMAKE_CC="${OE_QMAKE_CC}" \
        QMAKE_LINK="${OE_QMAKE_LINK}" \
        QMAKE_CFLAGS="${OE_QMAKE_CFLAGS}" \
        QMAKE_CXXFLAGS="${OE_QMAKE_CXXFLAGS}" \
        QMAKE_AR="${OE_QMAKE_AR}"
}
