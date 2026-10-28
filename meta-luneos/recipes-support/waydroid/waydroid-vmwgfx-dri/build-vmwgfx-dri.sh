#!/bin/sh
# Build the prebuilt that waydroid-vmwgfx-dri.bb installs: Mesa 23.2.1's svga
# driver for the Waydroid (LineageOS 18.1, Android 11, x86_64) image, with the
# two patches next to this script. It replaces the image's own
# /vendor/lib64/dri/vmwgfx_dri.so through Waydroid's vendor overlay, so it has
# to be built against bionic (the Android NDK), not with the Yocto toolchain,
# and from the same Mesa release as the image's libEGL_mesa.so.
#
# Usage: build-vmwgfx-dri.sh <workdir> <outdir>
# Needs on PATH: curl, sha256sum, unzip, tar, xz, patch, pkg-config, ninja,
# and a meson (>= 1.1) whose Python has mako. The NDK zip is 668 MB.
set -eu

[ $# -eq 2 ] || { echo "usage: $0 <workdir> <outdir>" >&2; exit 1; }
here=$(cd "$(dirname "$0")" && pwd)
mkdir -p "$1" "$2"
work=$(cd "$1" && pwd)
out=$(cd "$2" && pwd)

fetch() { # url sha256
    f=$work/${1##*/}
    if [ ! -f "$f" ]; then
        curl -fL -o "$f.part" "$1"
        mv "$f.part" "$f"
    fi
    echo "$2  $f" | sha256sum -c -
}
fetch https://archive.mesa3d.org/mesa-23.2.1.tar.xz \
    64de0616fc2d801f929ab1ac2a4f16b3e2783c4309a724c8a259b20df8bbc1cc
fetch https://dri.freedesktop.org/libdrm/libdrm-2.4.120.tar.xz \
    3bf55363f76c7250946441ab51d3a6cc0ae518055c0ff017324ab76cdefb327a
fetch https://dl.google.com/android/repository/android-ndk-r26d-linux.zip \
    eefeafe7ccf177de7cc57158da585e7af119bb7504a63604ad719e4b2a328b54

cd "$work"
rm -rf mesa-23.2.1 libdrm-2.4.120 sysroot build-libdrm build-mesa
[ -d android-ndk-r26d ] || unzip -q android-ndk-r26d-linux.zip
tar -xf mesa-23.2.1.tar.xz
tar -xf libdrm-2.4.120.tar.xz
for p in "$here"/0*.patch; do patch -d mesa-23.2.1 -p1 < "$p"; done

tc=$work/android-ndk-r26d/toolchains/llvm/prebuilt/linux-x86_64/bin
# Android 11 is API 30. --undefined-version: Mesa's version scripts name
# symbols this build does not have, which lld rejects by default.
cat > android-x86_64.cross <<CROSS
[binaries]
c = '$tc/x86_64-linux-android30-clang'
cpp = '$tc/x86_64-linux-android30-clang++'
ar = '$tc/llvm-ar'
strip = '$tc/llvm-strip'
pkg-config = 'pkg-config'

[properties]
needs_exe_wrapper = true

[built-in options]
c_args = ['-fPIC', '-ffile-prefix-map=$work/=']
cpp_args = ['-fPIC', '-ffile-prefix-map=$work/=']
c_link_args = ['-Wl,--undefined-version']
cpp_link_args = ['-static-libstdc++', '-Wl,--undefined-version']

[host_machine]
system = 'android'
cpu_family = 'x86_64'
cpu = 'x86_64'
endian = 'little'
CROSS

# libdrm is linked in statically: the image's vendor has no libdrm.so that
# matches what Mesa expects from a pkg-config dependency.
meson setup build-libdrm libdrm-2.4.120 --cross-file android-x86_64.cross \
    --prefix="$work/sysroot" -Ddefault_library=static -Dtests=false \
    -Dman-pages=disabled -Dvalgrind=disabled -Dcairo-tests=disabled \
    -Dudev=false -Dinstall-test-programs=false -Damdgpu=disabled \
    -Dradeon=disabled -Dnouveau=disabled -Dintel=disabled -Dvc4=disabled \
    -Dfreedreno=disabled -Detnaviv=disabled -Dexynos=disabled \
    -Domap=disabled -Dtegra=disabled
ninja -C build-libdrm install

PKG_CONFIG_LIBDIR=$work/sysroot/lib/pkgconfig PKG_CONFIG_SYSROOT_DIR= \
meson setup build-mesa mesa-23.2.1 --cross-file android-x86_64.cross \
    --prefix=/vendor -Dbuildtype=release -Dplatforms=android \
    -Dplatform-sdk-version=30 -Dandroid-stub=true \
    -Dandroid-libbacktrace=disabled -Dgallium-drivers=svga \
    -Dvulkan-drivers= -Dllvm=disabled -Dglx=disabled -Dgbm=disabled \
    -Degl=enabled -Dgles1=disabled -Dgles2=enabled -Dshared-glapi=enabled \
    -Dxmlconfig=disabled -Dexpat=disabled -Dzstd=disabled \
    -Dvalgrind=disabled -Dlibunwind=disabled -Dlmsensors=disabled \
    -Dperfetto=false -Dbuild-tests=false -Dgallium-va=disabled \
    -Dgallium-vdpau=disabled -Dgallium-xa=disabled -Dgallium-nine=false \
    -Dgallium-opencl=disabled -Dgallium-omx=disabled -Dosmesa=false \
    -Dvideo-codecs= -Degl-lib-suffix=_mesa -Dgles-lib-suffix=_mesa
ninja -C build-mesa src/gallium/targets/dri/libgallium_dri.so

"$tc/llvm-strip" --strip-unneeded -o "$out/vmwgfx_dri.so" \
    build-mesa/src/gallium/targets/dri/libgallium_dri.so
xz -9 -k -f "$out/vmwgfx_dri.so"
cd "$out" && sha256sum vmwgfx_dri.so vmwgfx_dri.so.xz
