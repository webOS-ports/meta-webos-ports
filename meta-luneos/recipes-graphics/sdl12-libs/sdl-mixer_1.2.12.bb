SUMMARY = "Audio mixer library for SDL 1.2"
DESCRIPTION = "SDL_mixer 1.2, providing libSDL_mixer-1.2.so.0 - the single most \
widely linked helper in the legacy webOS PDK catalogue at 413 titles. \
\
Palm's own build of this library pulled in libnapp.so and libhelpers-ex.so, both \
proprietary, plus ffmpeg 0.6. Upstream needs none of that."

SDLLIB = "SDL_mixer"
SDLBRANCH = "SDL-1.2"
SRCREV = "1a14d94ed4271e45435ecb5512d61792e1a42932"

LIC_FILES_CHKSUM = "file://COPYING;md5=a37a47a0e579e461474cd03b9e05199d"

require sdl12-libs.inc

# configure.in line 3 already has AC_CONFIG_AUX_DIR(build-scripts). Line 44 then
# repeats it as AC_CONFIG_AUX_DIRS($srcdir/build-scripts), where $srcdir is not
# expanded at autoreconf time, so autoconf tries to create a directory literally
# called '$srcdir':
#   autoreconf: error: cannot create $srcdir/build-scripts: No such file or directory
# Upstream never fixed it because the 1.2 branch stopped in 2012.
do_configure:prepend() {
    sed -i '/AC_CONFIG_AUX_DIRS(\$srcdir\/build-scripts)/d' ${S}/configure.in
}

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
