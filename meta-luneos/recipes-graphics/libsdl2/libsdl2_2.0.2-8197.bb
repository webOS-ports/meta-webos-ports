SUMMARY = "Simple DirectMedia Layer"
DESCRIPTION = "Simple DirectMedia Layer is a cross-platform multimedia \
library designed to provide low level access to audio, keyboard, mouse, \
joystick, 3D hardware via OpenGL, and 2D video framebuffer."
HOMEPAGE = "http://www.libsdl.org"
BUGTRACKER = "http://bugzilla.libsdl.org/"

SECTION = "libs"

LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=67dcb7fae16952557bc5f96e9eb5d188"

PROVIDES = "virtual/libsdl2"

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
           ${@base_contains('DISTRO_FEATURES', 'x11', 'virtual/libx11 libxext libxrandr libxrender', '', d)} \
           ${@base_contains('DISTRO_FEATURES', 'wayland', 'wayland libxkbcommon', '', d)} \
           tslib virtual/libgles2 virtual/egl"

# We have to add libxkbcommon as runtime dependency as SDL performs dynamic library
# loading and therefor OE can't detect it's linkage against libxkbcommon
RDEPENDS_${PN} += "${@base_contains('DISTRO_FEATURES', 'wayland', 'xkeyboard-config libxkbcommon', '', d)}"

# Don't add virtual/libgl as we need virtual/libgles2 and virtual/egl instead
# ${@base_contains('DISTRO_FEATURES', 'opengl', 'virtual/libgl', '', d)}

DEPENDS_class-nativesdk = "${@base_contains('DISTRO_FEATURES', 'x11', 'virtual/nativesdk-libx11 nativesdk-libxrandr nativesdk-libxrender nativesdk-libxext', '', d)}"

SRC_URI = "http://www.libsdl.org/tmp/SDL-${PV}.tar.gz \
    file://xkb-crash-fix.patch"

S = "${WORKDIR}/SDL-${PV}"

SRC_URI[md5sum] = "cbc3195e6d36c985f684f118a62c1cb0"
SRC_URI[sha256sum] = "c6e3bbbba74ee5db475072708233bbf40082b7d5b0d0788afcdf38798ac13c38"

inherit autotools lib_package binconfig pkgconfig

EXTRA_OECONF = "--disable-oss --disable-esd --disable-arts \
                --disable-diskaudio --disable-nas --disable-esd-shared --disable-esdtest \
                --disable-video-dummy \
                --enable-input-tslib --enable-pthreads \
                ${@base_contains('DISTRO_FEATURES', 'directfb', '--enable-video-directfb', '--disable-video-directfb', d)} \
                ${@base_contains('DISTRO_FEATURES', 'opengl', '--enable-video-opengl', '--disable-video-opengl', d)} \
                ${@base_contains('DISTRO_FEATURES', 'x11', '--enable-video-x11', '--disable-video-x11', d)} \
                ${@base_contains('DISTRO_FEATURES', 'wayland', '--enable-video-wayland', '--disable-video-wayland', d)} \
                --enable-sdl-dlopen \
                --disable-rpath \
                --disable-pulseaudio"

EXTRA_OECONF += "--enable-video-wayland-qt-touch"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)}"
PACKAGECONFIG[alsa] = "--enable-alsa --disable-alsatest,--disable-alsa,alsa-lib,"

PARALLEL_MAKE = ""

EXTRA_AUTORECONF += "--include=acinclude --exclude=autoheader"

do_configure_prepend() {
        # Remove old libtool macros.
        MACROS="libtool.m4 lt~obsolete.m4 ltoptions.m4 ltsugar.m4 ltversion.m4"
        for i in ${MACROS}; do
               rm -f ${S}/acinclude/$i
        done
        export SYSROOT=$PKG_CONFIG_SYSROOT_DIR
}
