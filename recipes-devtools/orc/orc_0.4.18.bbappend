
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " file://disable-mmap.patch"

do_configure_prepend() {
    sed -i -e '/doc\//d' ${S}/configure.ac
    sed -i -e 's/doc//' ${S}/Makefile.am
    sed -i -e 's/doc//' ${S}/Makefile.in

    # required to complete the rootfs install
    #sed -i -e '/tools\//d' ${S}/configure.ac
    #sed -i -e 's/tools//' ${S}/Makefile.am
    #sed -i -e 's/tools//' ${S}/Makefile.in

    sed -i -e '/examples\//d' ${S}/configure.ac
    sed -i -e 's/examples//' ${S}/Makefile.am
    sed -i -e 's/examples//' ${S}/Makefile.in

    sed -i -e '/testsuite\//d' ${S}/configure.ac
    sed -i -e 's/testsuite//' ${S}/Makefile.am
    sed -i -e 's/testsuite//' ${S}/Makefile.in
}
