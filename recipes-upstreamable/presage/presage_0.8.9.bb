SECTION = "libs"
DESCRIPTION = "Presage is an intelligent predictive text entry system."

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "sqlite3 libtinyxml"
DEPENDS_append_class-target = " presage-native"

BBCLASSEXTEND = "native"

inherit autotools gettext

SRC_URI = " \
    http://downloads.sourceforge.net/${BPN}/${BP}.tar.gz \
    file://disable-help2man.patch \
    file://no-automake-werror.patch \
"

SRC_URI[md5sum] = "13221794c5f55f2fde5c5938c59c8548"
SRC_URI[sha256sum] = "5541e9b350cc603a8d412704dcfa21342369b5b07c6da91947c7523c51678cd0"

EXTRA_OECONF = " \
    --disable-documentation \
    --disable-gpresagemate \
    --disable-gprompter \
    --disable-python-binding \
"

do_configure_prepend_class-target() {
   sed -i "s#\$(top_builddir)/src/tools/text2ngram#${STAGING_BINDIR_NATIVE}/text2ngram#g" ${S}/resources/Makefile.am
}
