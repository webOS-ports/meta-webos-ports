SUMMARY = "Tidy corrects and cleans up HTML content by fixing markup errors."
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://license.html;md5=dd71141e2a02dcb46089c09e63d00657"

PV = "0.99+gitr${SRCPV}"
SRCREV = "0cf6d998431a61f1bd630e7883d4d43df4ad8ccc"

SRC_URI = " \
    git://github.com/w3c/tidy-html5;branch=master;protocol=git \
    file://0001-Correctly-deploy-automake-files-at-the-right-place.patch \
"
S = "${WORKDIR}/git"

inherit autotools autotools-brokensep

do_configure_prepend() {
    touch ${S}/NEWS
    touch ${S}/ChangeLog
    touch ${S}/AUTHORS
    touch ${S}/README
}

FILES_${PN} = " \
  ${bindir}/tidy \
  ${bindir}/tab2space \
  ${libdir}/libtidy* \
  ${includedir}/*.h \
"
FILES_${PN}-dbg += "${libdir}/.debug"
