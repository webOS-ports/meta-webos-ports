SUMMARY = "Tidy corrects and cleans up HTML content by fixing markup errors."
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://license.html;md5=dd71141e2a02dcb46089c09e63d00657"

PV = "0.99+git"
SRCREV = "0cf6d998431a61f1bd630e7883d4d43df4ad8ccc"

SRC_URI = " \
    git://github.com/w3c/tidy-html5;branch=master;protocol=https \
    file://0001-Correctly-deploy-automake-files-at-the-right-place.patch \
"
S = "${WORKDIR}/git"

inherit autotools autotools-brokensep

do_configure:prepend() {
    touch ${S}/NEWS
    touch ${S}/ChangeLog
    touch ${S}/AUTHORS
    touch ${S}/README
}

FILES:${PN} = " \
    ${bindir}/tidy \
    ${bindir}/tab2space \
    ${libdir}/libtidy* \
    ${includedir}/*.h \
"

# Upstream still declares cmake_minimum_required(VERSION < 3.5), which CMake 4
# rejects. This recipe inherits plain cmake, not webos_cmake, so it needs the
# flag inline.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
