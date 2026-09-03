SUMMARY = "ARM soft-float userland for legacy webOS PDK applications"
DESCRIPTION = "The complete environment a 2010-2012 webOS PDK/SDL game runs \
inside: glibc, Mesa, SDL2 + sdl12-compat, the SDL helper libraries, and the \
pdk-luneos shims that supply the Palm-only APIs. Built at the soft-float ABI and \
shipped inside a normal (hard-float) LuneOS image at /opt/pdk/sysroot."

LICENSE = "MIT"

# This is not a bootable image - it is a library tree that another image carries.
# No kernel, no init, no package manager, no login.
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""
IMAGE_FSTYPES = "tar.xz"
NO_RECOMMENDATIONS = "1"

inherit image

IMAGE_INSTALL = " \
    ${PDK_SYSROOT_BASE} \
    ${PDK_SYSROOT_GRAPHICS} \
    ${PDK_SYSROOT_SDL} \
    ${PDK_SYSROOT_MEDIA} \
    ${PDK_SYSROOT_SHIMS} \
"

# glibc and the handful of C-library-adjacent things every title touches.
PDK_SYSROOT_BASE = " \
    base-files \
    glibc \
    libgcc \
    libstdc++ \
    zlib \
    curl \
    openssl \
    sqlite3 \
    libpng \
    jpeg \
    freetype \
    fontconfig \
"

# Mesa, and the Wayland client side. There is no X11 in here: SDL2 talks Wayland
# to luna-surfacemanager directly.
# Mesa 26 restructured its packaging: there is no "mesa" package any more, and
# the DRI megadriver was replaced by libgallium, which is where the actual
# driver code and ${libdir}/dri/* now live. Naming the old packages fails at
# rootfs time rather than parse time:
#   opkg_prepare_url_for_install: Couldn't find anything to satisfy 'mesa'
PDK_SYSROOT_GRAPHICS = " \
    libgallium \
    libgbm \
    libegl-mesa \
    libgles1-mesa \
    libgles2-mesa \
    wayland \
    libxkbcommon \
"

# The SDL 1.2 helper sonames, not the SDL2 ones. Games link
# libSDL_image-1.2.so.0 / libSDL_mixer-1.2.so.0 / libSDL_ttf-2.0.so.0 /
# libSDL_net-1.2.so.0; libSDL2_image-2.0.so.0 and friends are different
# libraries and satisfy none of them. Shipping the SDL2 set here was a real bug:
# only titles needing no helper at all could load.
PDK_SYSROOT_SDL = " \
    libsdl2 \
    sdl12-compat \
    sdl-image \
    sdl-mixer \
    sdl-ttf \
    sdl-net \
"

# Games load music through SDL_mixer, which needs the codecs present at runtime.
PDK_SYSROOT_MEDIA = " \
    libvorbis \
    libogg \
    flac \
    alsa-lib \
    libpulse \
    libpulse-simple \
"

PDK_SYSROOT_SHIMS = "pdk-luneos"

# No fonts here. Applications open /usr/share/fonts by absolute path, and that
# resolves against the host rootfs - qemu-user falls back to the real path when
# the sysroot has no such file, and on native ARM there is no translation at all.
# So pdk-sysroot-fonts is installed by packagegroup-luneos-pdk in the main
# configuration, where LuneOS's own luna-init-fonts lives.

# Nothing in this tree is ever executed by the host system directly, and the ABI
# deliberately differs from the image that carries it.
IMAGE_ROOTFS_EXTRA_SPACE = "0"
