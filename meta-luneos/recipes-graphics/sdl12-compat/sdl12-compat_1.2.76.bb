SUMMARY = "SDL 1.2 ABI implemented on top of SDL2"
DESCRIPTION = "sdl12-compat provides the SDL 1.2 API and ABI backed by SDL2, so \
that unmodified SDL 1.2 binaries - such as legacy webOS PDK applications - can \
run against a modern, Wayland-capable SDL2."
HOMEPAGE = "https://github.com/libsdl-org/sdl12-compat"
BUGTRACKER = "https://github.com/libsdl-org/sdl12-compat/issues"

LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f81f7fb6803c844def5c392f71c28dcd"

SRC_URI = "git://github.com/libsdl-org/sdl12-compat.git;protocol=https;branch=main"
SRCREV = "293b4cbd9eea61b18b9c9425d0c9200bff037360"

DEPENDS = "libsdl2"

inherit cmake pkgconfig

# SDL12TESTS pulls in the 1.2 test programs, which are of no use in an image.
EXTRA_OECMAKE = "-DSDL12TESTS=OFF -DSDL12DEVEL=ON"

# Take the soname out of the way of the webOS SDL shim.
#
# Legacy PDK applications link libSDL-1.2.so.0 and expect Palm's webOS fork, which
# has ~20 extension entry points stock SDL 1.2 never had (SDL_WebOsHook*, the GLES
# attribute calls, the accelerometer joystick). pdk-luneos supplies those in a shim
# that must therefore *be* libSDL-1.2.so.0 and chain to this library underneath.
#
# So this one is renamed to libSDL12compat.so.0. Doing it here rather than in
# pdk-luneos keeps the dependency graph honest: anything that links this library
# records the name it will actually find at runtime.
DEPENDS:append:pdk-armel = " patchelf-native"

do_install:append:pdk-armel() {
    # Upstream installs the real object as libSDL-1.2.so.<PV> (libSDL-1.2.so.1.2.76),
    # with libSDL-1.2.so.0 and libSDL.so as symlinks onto it - so match on the
    # stem, not on the soname. Drop the symlinks first: the webOS shim provides
    # both of those names itself.
    find ${D}${libdir} -maxdepth 1 -type l \( -name 'libSDL-1.2.so*' -o -name 'libSDL.so' \) -delete

    real=$(find ${D}${libdir} -maxdepth 1 -type f -name 'libSDL-1.2.so*' -print -quit)
    if [ -z "$real" ]; then
        bbfatal "sdl12-compat: no libSDL-1.2.so* to rename - did upstream change its layout?"
    fi

    mv "$real" ${D}${libdir}/libSDL12compat.so.0
    patchelf --set-soname libSDL12compat.so.0 ${D}${libdir}/libSDL12compat.so.0
}

FILES:${PN} += "${libdir}/libSDL-1.2.so.* ${libdir}/libSDL12compat.so.*"
