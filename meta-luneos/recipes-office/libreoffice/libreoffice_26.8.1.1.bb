# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "LibreOffice core without a user interface, for LibreOfficeKit"
DESCRIPTION = "The LibreOffice import and export filters and the \
LibreOfficeKit library, built headless (--disable-gui: no X11, Wayland, GTK \
or Qt). Services load it with lok_init() to convert documents and, later, \
to render and edit them."
HOMEPAGE = "https://www.libreoffice.org"
SECTION = "office"
LICENSE = "MPL-2.0 & LGPL-3.0-or-later & GPL-3.0-or-later"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
    file://COPYING.LGPL;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.MPL;md5=9741c346eef56131163e13b9db1241b3 \
"

DIRV = "${@'.'.join(d.getVar('PV').split('.')[0:3])}"
SRC_URI = " \
    https://download.documentfoundation.org/libreoffice/src/${DIRV}/libreoffice-${PV}.tar.xz \
    file://0001-configure-probe-isystem-with-a-directory-in-the-buil.patch \
    file://0002-gbuild-create-UnpackedTarball-before-writing-a-.vers.patch \
"
SRC_URI[sha256sum] = "296ba78e7ab0d381b94c3d6342f4ebb2320ee5097ca58781b9b744cab90b9785"

S = "${UNPACKDIR}/libreoffice-${PV}"

# LibreOffice cross-compiles itself: configure runs a second configure for the
# build machine in CONF-FOR-BUILD and gbuild builds the tools it needs
# (cross-toolset) before the target. meta-office's split into a -native recipe
# and 17 gbuild patches is not needed for that, and did not survive 7.4.
inherit autotools-brokensep pkgconfig python3native perlnative

# HarfBuzz and Graphite are the bundled ones: a system HarfBuzz must be built
# with Graphite, and no layer here has a graphite2 recipe. They build with the
# meson in download.lst: OE's meson is a shell wrapper, and configure runs it
# with python. The -native
# libraries are for the build machine's configure (CONF-FOR-BUILD).
DEPENDS = " \
    bison-native flex-native gperf-native zip-native unzip-native ninja-native \
    libxml2-native libxslt-native zlib-native expat-native fontconfig-native freetype-native \
    cairo fontconfig freetype icu \
    libxml2 libxslt expat zlib zstd libpng libjpeg-turbo libwebp tiff lcms \
    curl nss nspr hunspell \
"

# The bundled externals (download.lst) that are not taken from the system are
# fetched by LibreOffice's own Makefile.fetch into one directory shared by all
# machines. TODO: list them in SRC_URI so that do_fetch gets them.
LO_EXTERNAL_TAR = "${DL_DIR}/libreoffice-externals"
do_configure[network] = "1"
do_compile[network] = "1"

LO_LANGS ?= "en-US"

# configure has no --disable-static and warns about it.
DISABLE_STATIC = ""

EXTRA_OECONF = " \
    --build=${BUILD_SYS} --host=${HOST_SYS} \
    --prefix=${prefix} --libdir=${libdir} --sysconfdir=${sysconfdir} \
    --with-external-tar=${LO_EXTERNAL_TAR} \
    --with-parallelism=${@oe.utils.cpu_count()} \
    --enable-release-build \
    --with-vendor=LuneOS \
    --with-lang='${LO_LANGS}' \
    --disable-gui \
    --disable-gtk3 --disable-gtk4 --disable-qt5 --disable-qt6 --disable-kf5 --disable-kf6 \
    --disable-dbus --disable-dconf --disable-gio --disable-cups --disable-cpdb \
    --disable-gstreamer-1-0 --disable-evolution2 --disable-randr --disable-avahi \
    --disable-odk --disable-online-update --disable-skia --disable-opencl \
    --enable-cairo-rgba \
    --enable-mergelibs \
    --disable-lto \
    --disable-ccache \
    --disable-python \
    --without-java --without-junit --without-help --without-helppack-integration \
    --without-doxygen --without-package-format --disable-epm \
    --with-galleries=no --with-theme=colibre \
    --disable-scripting-beanshell --disable-scripting-javascript \
    --disable-ext-wiki-publisher --disable-report-builder --disable-ext-nlpsolver \
    --disable-sdremote --disable-sdremote-bluetooth \
    --disable-postgresql-sdbc --disable-firebird-sdbc --disable-mariadb-sdbc \
    --disable-lpsolve --disable-coinmp --disable-poppler --disable-pdfium --disable-pdfimport \
    --disable-gpgmepp --disable-ldap --disable-zxing --disable-libcmis \
    --with-tls=nss \
    --with-system-cairo --with-system-fontconfig --with-system-freetype \
    --without-system-harfbuzz --without-system-graphite --with-system-icu \
    --with-system-libxml --with-system-expat --with-system-zlib --with-system-zstd \
    --with-system-libpng --with-system-jpeg --with-system-libwebp --with-system-libtiff \
    --with-system-lcms2 --with-system-curl --with-system-nss --with-system-hunspell \
    --with-system-dicts --with-external-dict-dir=${datadir}/hunspell \
    --with-fonts \
"

# The build machine's half: same compilers bitbake uses for native code, and
# pkg-config pointed at the native sysroot.
export CC_FOR_BUILD = "${BUILD_CC}"
export CXX_FOR_BUILD = "${BUILD_CXX}"
export PKG_CONFIG_FOR_BUILD = "pkg-config-native"
export PYTHON_FOR_BUILD = "${PYTHON}"
# The build machine's pyuno: its configure asks distutils for the headers, and
# Python 3.12 has no distutils. The target side has --disable-python.
export PYTHON_CFLAGS = "-I${STAGING_INCDIR_NATIVE}/python${PYTHON_BASEVERSION}"
export PYTHON_LIBS = "-L${STAGING_LIBDIR_NATIVE} -lpython${PYTHON_BASEVERSION}"

do_configure() {
    mkdir -p ${LO_EXTERNAL_TAR}
    # The build machine's configure unsets CC and CXX but not CPP and CXXCPP,
    # and would take the target's preprocessor; let both sides find their own.
    unset CPP CXXCPP
    # CONFIG_SITE holds the target's autoconf answers (ac_cv_sizeof_long=4 on
    # arm), which the build machine's configure would take as its own. The
    # target's configure compiles its checks and does not need them either.
    unset CONFIG_SITE
    # configure in the source tree: gbuild wants SRCDIR == BUILDDIR here.
    # CONFIGUREOPTS is not used: LibreOffice's configure refuses --target and
    # the libtool options autotools.bbclass adds.
    ${S}/configure ${EXTRA_OECONF}
}

do_compile() {
    # The bundled HarfBuzz finds the bundled Graphite through
    # graphite2-uninstalled.pc, which pkgconfig.bbclass tells pkg-config to
    # ignore.
    unset PKG_CONFIG_DISABLE_UNINSTALLED
    # gbuild runs make itself with --with-parallelism; PARALLEL_MAKE would
    # multiply it.
    oe_runmake -j1 build
}

do_install() {
    oe_runmake -j1 DESTDIR=${D} distro-pack-install
    # LibreOfficeKit headers, for the services that load it
    install -d ${D}${includedir}/LibreOfficeKit
    install -m 0644 ${S}/include/LibreOfficeKit/*.h ${S}/include/LibreOfficeKit/*.hxx \
        ${D}${includedir}/LibreOfficeKit/
    # Headless: no launchers, and no MIME or desktop registration that would
    # claim the Documents app's types
    rm -rf ${D}${bindir} ${D}${datadir}/applications ${D}${datadir}/mime \
        ${D}${datadir}/icons ${D}${datadir}/metainfo ${D}${datadir}/bash-completion \
        ${D}${mandir}
}

LO_DIR = "${libdir}/libreoffice"

PACKAGES =+ "${PN}-fonts ${PN}-lok-dev"
FILES:${PN}-fonts = "${LO_DIR}/share/fonts"
FILES:${PN}-lok-dev = "${includedir}/LibreOfficeKit"
FILES:${PN} += "${LO_DIR} ${datadir}"
# The .so files under program/ are dlopen()ed modules, not development links.
FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"

# The bundled pixman (for the bundled cairo, which --enable-cairo-rgba
# uses) has the system's soname; it is LibreOffice's own copy.
PRIVATE_LIBS = "libpixman-1.so.0"

# TODO: the externals LibreOffice builds itself (libwpd, libmwaw, raptor...)
# are linked without OE's LDFLAGS (no GNU_HASH), the bundled cairo has an
# RPATH into the build tree, and libmergedlo/libuno_sal/cairo embed build
# paths. Skipped for now; to be fixed before this goes upstream.
INSANE_SKIP:${PN} += "ldflags rpaths buildpaths"
INSANE_SKIP:${PN}-src += "buildpaths"
RRECOMMENDS:${PN} += "${PN}-fonts"
