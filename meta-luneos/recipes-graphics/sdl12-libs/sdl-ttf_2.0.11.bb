SUMMARY = "TrueType font rendering library for SDL 1.2"
DESCRIPTION = "SDL_ttf 2.0.11, the last SDL 1.2 release, providing \
libSDL_ttf-2.0.so.0. 123 titles link it, and several open a hardcoded font path \
without checking TTF_OpenFont for NULL - see pdk-sysroot-fonts."

SDLLIB = "SDL_ttf"
SDLBRANCH = "SDL-1.2"
SRCREV = "3af6dd26174bb719c241447d1ea55e40597bb9a6"

LIC_FILES_CHKSUM = "file://COPYING;md5=22800d1b3701377aae0b61ee36f5c303"

require sdl12-libs.inc

DEPENDS += "freetype"

# configure does AC_PATH_PROG(FREETYPE_CONFIG, freetype-config) and bails without
# it, but freetype dropped freetype-config in 2.9.1 in favour of pkg-config:
#   checking for freetype-config... no
#   configure: error: *** Unable to find FreeType2 library
# Rather than patch configure.in, put a shim on PATH that answers in terms of
# pkg-config. --ftversion is reported from freetype2.pc's own version rather
# it only wants --cflags and --libs.
do_configure:prepend() {
    mkdir -p ${WORKDIR}/ft-shim
    cat > ${WORKDIR}/ft-shim/freetype-config <<'SHIM'
#!/bin/sh
case "$1" in
    --cflags)          exec pkg-config --cflags freetype2 ;;
    --libs)            exec pkg-config --libs freetype2 ;;
    --ftversion)       exec pkg-config --modversion freetype2 ;;
    --version)         exec pkg-config --modversion freetype2 ;;
    --prefix|--exec-prefix) exec pkg-config --variable=prefix freetype2 ;;
    *) exit 1 ;;
esac
SHIM
    chmod +x ${WORKDIR}/ft-shim/freetype-config
}

PATH:prepend = "${WORKDIR}/ft-shim:"
