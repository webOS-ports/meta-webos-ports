SUMMARY = "Audio mixer library for SDL 1.2"
DESCRIPTION = "SDL_mixer 1.2, providing libSDL_mixer-1.2.so.0 - the single most \
widely linked helper in the legacy webOS PDK catalogue at 413 titles. \
\
Palm's own build of this library pulled in libnapp.so and libhelpers-ex.so, both \
proprietary, plus ffmpeg 0.6. Upstream needs none of that."

SDLLIB = "SDL_mixer"
SDLBRANCH = "SDL-1.2"
SRCREV = "50517740a3916e5ffd719c053c6e7b65f933e23a"

LIC_FILES_CHKSUM = "file://COPYING;md5=a37a47a0e579e461474cd03b9e05199d"

require sdl12-libs.inc

DEPENDS += "libvorbis libogg flac libmikmod"

# MIDI via fluidsynth is deliberately off: it drags in libinstpatch and glib,
# and no PDK title uses MIDI. Music is Ogg Vorbis or tracker formats.
EXTRA_OECONF += " \
    --enable-music-ogg --enable-music-ogg-shared=no \
    --enable-music-flac --enable-music-flac-shared=no \
    --enable-music-mod --enable-music-mod-shared=no \
    --disable-music-fluidsynth-midi \
    --disable-music-mp3-mad-gpl \
    --disable-music-mp3 \
"
