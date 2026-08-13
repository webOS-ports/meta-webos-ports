SUMMARY = "Image loading library for SDL 1.2"
DESCRIPTION = "SDL_image 1.2, providing libSDL_image-1.2.so.0. 271 titles in \
the legacy webOS PDK catalogue link this soname; the SDL2 build in meta-oe is a \
different library and does not satisfy them."

SDLLIB = "SDL_image"
SDLBRANCH = "SDL-1.2"
SRCREV = "220be3fd43a85921138072ed847b7e4bc5ad163e"

LIC_FILES_CHKSUM = "file://COPYING;md5=613734b7586e1580ef944961c6d62227"

require sdl12-libs.inc

DEPENDS += "libpng jpeg tiff libwebp zlib"

# The loaders are compiled in rather than dlopened, so the codec libraries are
# recorded as real dependencies and cannot go missing at runtime.
EXTRA_OECONF += " \
    --enable-png --enable-png-shared=no \
    --enable-jpg --enable-jpg-shared=no \
    --enable-tif --enable-tif-shared=no \
    --enable-webp --enable-webp-shared=no \
"
