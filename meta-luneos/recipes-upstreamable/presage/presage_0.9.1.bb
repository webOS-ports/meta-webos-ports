SECTION = "libs"
DESCRIPTION = "Presage is an intelligent predictive text entry system."

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "sqlite3 libtinyxml ncurses"
DEPENDS:append:class-target = " presage-native"

BBCLASSEXTEND = "native"

# inheriting python3native works only as long as python3-native and target python3
# have the same major version (used in python3-dir), but it's better than using
# python3 from host (e.g. it installs to /usr/lib/python3.7/ on my host with ubuntu-19.04
# and /usr/lib/python3.6 on jenkins with ubuntu-18.04 as shown here:
# http://jenkins.nas-admin.org/job/LuneOS/view/unstable/job/luneos-unstable:qemux86-64/78/consoleFull
inherit autotools gettext pkgconfig python3-dir python3native

SRC_URI = " \
    http://downloads.sourceforge.net/${BPN}/${BP}.tar.gz \
    file://0001-configure.ac-disable-help2man.patch \
    file://0002-iso8859_1.h-Fix-build-with-gcc-6.patch \
    file://0003-predictors-Fix-build-with-gcc-7.patch \
    file://0004-Fix-build-with-gcc-11.patch \
    file://0005-configure.ac-don-t-use-L-usr-local-lib-in-LDFLAGS.patch \
"

SRC_URI[md5sum] = "9667be297912fa0d432e748526d8dd9e"
SRC_URI[sha256sum] = "5ed567e350402a1d72c9053c78ecec3be710b7e72153a0223c6d19a7fe58a366"

EXTRA_OECONF = " \
    --disable-documentation \
    --disable-gpresagemate \
    --disable-gprompter \
    --disable-python-binding \
"

# a lot of cases like:
# presage.cpp:201:5: error: ISO C++17 does not allow dynamic exception specifications
CXXFLAGS += "-std=gnu++14"

FILES:${PN} += "\
    ${libdir}/${PYTHON_DIR} \
    ${libdir}/${PYTHON_DIR}/dist-packages \
    ${libdir}/${PYTHON_DIR}/dist-packages/presage_dbus_service.pyc \
    ${libdir}/${PYTHON_DIR}/dist-packages/presage_dbus_service.pyo \
    ${libdir}/${PYTHON_DIR}/dist-packages/presage_dbus_service.py \
    ${datadir}/dbus-1 \
    ${datadir}/dbus-1/services \
    ${datadir}/dbus-1/services/org.gnome.presage.service \
"

do_configure:prepend:class-target() {
   sed -i "s#\$(top_builddir)/src/tools/text2ngram#${STAGING_BINDIR_NATIVE}/text2ngram#g" ${S}/resources/Makefile.am
}
