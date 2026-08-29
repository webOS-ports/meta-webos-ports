# Common bits for the SDL 1.2 helper libraries.
#
# Legacy webOS PDK games link these by their SDL 1.2 sonames -
# libSDL_image-1.2.so.0, libSDL_mixer-1.2.so.0, libSDL_ttf-2.0.so.0,
# libSDL_net-1.2.so.0 - and nothing else provides them. The SDL2 versions in
# meta-oe are a different library with a different soname (libSDL2_image-2.0.so.0)
# and do not satisfy these links. Measured across the catalogue:
#
#   libSDL_mixer-1.2.so.0   413 titles
#   libSDL_image-1.2.so.0   271
#   libSDL_ttf-2.0.so.0     123
#   libSDL_net-1.2.so.0      96
#
# They build against sdl12-compat, which installs the SDL 1.2 headers when
# SDL12DEVEL=ON. Its sdl-config hardcodes prefix=/usr and would hand the host's
# paths to a cross build, so SDL_CFLAGS/SDL_LIBS are passed explicitly and
# --disable-sdltest skips the compile-and-run probe that cross builds cannot do.
#
# Upstream stopped touching the 1.2 branch in 2012, so these are pinned to its
# final releases.

HOMEPAGE = "https://github.com/libsdl-org/${SDLLIB}"
LICENSE = "Zlib"
SECTION = "libs"

DEPENDS += "sdl12-compat"

SRC_URI = "git://github.com/libsdl-org/${SDLLIB}.git;protocol=https;branch=${SDLBRANCH}"
inherit autotools pkgconfig

SDL_CFLAGS = "-I${STAGING_INCDIR}/SDL -D_GNU_SOURCE=1 -D_REENTRANT"
# Not -lSDL: on pdk-armel the sdl12-compat recipe renames its library to
# libSDL12compat.so.0 and drops the libSDL.so / libSDL-1.2.so.0 symlinks, so that
# the webOS SDL shim can own that soname. There is no libSDL.so to link against:
#   ld: cannot find -lSDL: No such file or directory
# -l: names the file directly and records the right NEEDED.
SDL_LIBS = "-L${STAGING_LIBDIR} -l:libSDL12compat.so.0 -lpthread"
export SDL_CFLAGS
export SDL_LIBS

EXTRA_OECONF += "--disable-sdltest --disable-static"

# 2012 autotools against a modern autoreconf. acinclude/ carries the project's
# own sdl.m4, which defines AM_PATH_SDL and is not otherwise on the macro path -
# but it also carries libtool.m4 and friends from libtool 2.2.6. aclocal picks
# those up wholesale, so the generated libtool ends up with macro_version=2.2.6
# while OE installs ltmain.sh 2.4.7, and every link fails:
#   libtool: Version mismatch error. This is libtool 2.4.7, but the
#   libtool: definition of this LT_INIT comes from libtool 2.2.6.
# The stale macros are deleted below so the system ones are used instead.
EXTRA_AUTORECONF += "--include=${S}/acinclude"

# These predate automake's default "gnu" strictness, which insists on GNU
# boilerplate the SDL projects never had - they ship CHANGES rather than
# ChangeLog and no AUTHORS or NEWS at all:
#   Makefile.am: error: required file './AUTHORS' not found
# Creating them empty is less invasive than patching AM_INIT_AUTOMAKE in four
# separate configure.in files.
do_configure:prepend() {
    touch ${S}/AUTHORS ${S}/ChangeLog ${S}/NEWS

    # configure.in has AC_CONFIG_AUX_DIR(build-scripts) but the directory is not
    # in the git checkout, and autoreconf will not create it:
    #   autoreconf: error: cannot create $srcdir/build-scripts
    mkdir -p ${S}/build-scripts

    # Drop the vendored libtool macros and the pre-generated aclocal.m4 that
    # embeds them, so autoreconf regenerates against OE's libtool. sdl.m4 and
    # pkg.m4 stay - sdl.m4 is where AM_PATH_SDL comes from.
    rm -f ${S}/acinclude/libtool.m4 ${S}/acinclude/ltdl.m4 \
          ${S}/acinclude/ltoptions.m4 ${S}/acinclude/ltsugar.m4 \
          ${S}/acinclude/ltversion.m4 ${S}/acinclude/lt~obsolete.m4 \
          ${S}/aclocal.m4
}


# These ship an unversioned .so in the runtime package on purpose - PDK
# applications link both the versioned soname and the bare name.
FILES:${PN} += "${libdir}/lib*.so"
INSANE_SKIP:${PN} += "dev-so"

# The codecs are linked statically (--enable-music-*-shared=no), so configure
# records the recipe sysroot in Libs.private of the generated .pc file:
#   Libs.private: -L${RECIPE_SYSROOT}/usr/lib ...
# which fails the buildpaths QA check. Strip it - a consumer resolves those
# libraries from its own sysroot, so the path carries no information.
do_install:append() {
    for pc in ${D}${libdir}/pkgconfig/*.pc; do
        [ -f "$pc" ] || continue
        sed -i -e "s|-L${RECIPE_SYSROOT}${libdir}||g" \
               -e "s|${RECIPE_SYSROOT}||g" \
               -e "s|  *| |g" "$pc"
    done
}
