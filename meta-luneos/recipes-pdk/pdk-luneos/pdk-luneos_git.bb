SUMMARY = "Compatibility shims for legacy webOS PDK/SDL applications"
DESCRIPTION = "libpdl.so, the webOS SDL 1.2 extension shim, the GLES1 extension \
shim and the SDL_cinema stub - the Palm-only libraries that the 2010-2012 native \
webOS game catalogue links against, reimplemented against Mesa, SDL2/sdl12-compat \
and current LuneOS services."
HOMEPAGE = "https://github.com/webOS-ports/pdk-luneos"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.0+git"

# Pin this to a release commit before shipping. AUTOREV is only appropriate while
# the shims are still moving.
SRCREV = "37682a67a408c6f4913138e5e8105b3443ad5f8a"

# Plain git fetch rather than meta-luneos's webos_ports_repo class, so this layer
# parses standalone without meta-webos-ports present.
PDK_GIT_REPO ?= "git://github.com/webOS-ports/pdk-luneos.git"
SRC_URI = "${PDK_GIT_REPO};protocol=https;branch=main"
# These are the Palm-named replacements, so they must be built with the same ABI
# as the applications that load them. That is what the pdk-armel multiconfig is
# for - building this recipe in the main LuneOS build produces hard-float
# libraries that will corrupt every float crossing into them.
COMPATIBLE_MACHINE = "pdk-armel"

DEPENDS = "sdl12-compat libsdl2 virtual/egl virtual/libgles1 virtual/libgles2 \
           curl openssl sqlite3"

inherit pkgconfig

# B defaults to S, which has the standalone build's Makefile in it; base
# do_configure would run "oe_runmake clean" through it on any reconfigure.
do_configure[noexec] = "1"

# The Makefile in the repository targets a standalone clang cross-build against a
# Debian armel sysroot. Under BitBake the toolchain is already correct, so the
# shims are compiled directly and the Makefile's cross-compilation machinery is
# bypassed entirely.
do_compile() {
    mkdir -p ${B}/out

    # libpdl.so: --no-as-needed is load-bearing. The original libpdl pulled in
    # libcurl/libssl/libcrypto/libsqlite3, and other Palm libraries reached those
    # symbols transitively through it. Dropping them breaks 7+ titles at load with
    # "undefined symbol: curl_easy_init".
    ${CC} ${CFLAGS} ${LDFLAGS} -fPIC -shared -Wl,-soname,libpdl.so \
        -o ${B}/out/libpdl.so ${S}/src/libpdl.c -ldl \
        -Wl,--no-as-needed -lcurl -lssl -lcrypto -lsqlite3

    # The webOS SDL extensions, chained to the renamed sdl12-compat.
    ${CC} ${CFLAGS} ${LDFLAGS} -fPIC -shared -Wl,-soname,libSDL-1.2.so.0 \
        -o ${B}/out/libSDL-1.2.so.0 ${S}/src/sdl_webos_shim.c \
        -l:libSDL12compat.so.0 -ldl

    # GLES1 extension entry points that Mesa only exposes via eglGetProcAddress.
    #
    # --no-as-needed on libGLESv1_CM is load-bearing. This shim resolves every
    # symbol it forwards through eglGetProcAddress/dlsym, so it never references
    # a GLESv1 symbol at link time - and OE puts -Wl,--as-needed in LDFLAGS by
    # default, which then drops the library from NEEDED entirely. Applications
    # link libGLES_CM.so expecting core GLES1 to resolve *through* it, so the
    # chain has to be recorded even though this object does not use it:
    #   ./giddy3: symbol lookup error: undefined symbol: glMatrixMode
    ${CC} ${CFLAGS} ${LDFLAGS} -fPIC -shared -Wl,-soname,libGLES_CM.so \
        -o ${B}/out/libGLES_CM.so ${S}/src/gles_oes_shim.c \
        -Wl,--no-as-needed -lGLESv1_CM -lEGL -Wl,--as-needed -ldl

    # Stub for Palm's media-service video library. NB: CIN_Init returns non-zero
    # for success; see docs/architecture.md.
    ${CC} ${CFLAGS} ${LDFLAGS} -fPIC -shared -Wl,-soname,libSDL_cinema.so \
        -o ${B}/out/libSDL_cinema.so ${S}/src/sdl_cinema_stub.c

    # LD_PRELOAD fault reporter. See PDK_PRELOAD in docs/running.md.
    ${CC} ${CFLAGS} ${LDFLAGS} -fPIC -shared -funwind-tables \
        -o ${B}/out/crashcatch.so ${S}/src/crashcatch.c -ldl
}

do_install() {
    install -d ${D}${libdir}
    install -m 0644 ${B}/out/libpdl.so         ${D}${libdir}/
    install -m 0644 ${B}/out/libSDL-1.2.so.0   ${D}${libdir}/
    install -m 0644 ${B}/out/libGLES_CM.so     ${D}${libdir}/
    install -m 0644 ${B}/out/libSDL_cinema.so  ${D}${libdir}/
    install -m 0644 ${B}/out/crashcatch.so     ${D}${libdir}/

    # Palm's device carried unversioned .so symlinks and plenty of packages link
    # those names directly - including libPDL.so with a capital PDL. Without them
    # the loader gives up before the application runs.
    ln -sf libSDL-1.2.so.0 ${D}${libdir}/libSDL.so
    ln -sf libSDL-1.2.so.0 ${D}${libdir}/libSDL-1.2.so
    ln -sf libpdl.so       ${D}${libdir}/libPDL.so
}

PACKAGES = "${PN} ${PN}-dbg"

FILES:${PN} = " \
    ${libdir}/libpdl.so \
    ${libdir}/libPDL.so \
    ${libdir}/libSDL-1.2.so.0 \
    ${libdir}/libSDL-1.2.so \
    ${libdir}/libSDL.so \
    ${libdir}/libGLES_CM.so \
    ${libdir}/libSDL_cinema.so \
    ${libdir}/crashcatch.so \
"

# crashcatch.so ships in the main package, not -dbg. It is a 16KB LD_PRELOAD
# fault reporter and the moment you want it is when a title is crashing on a
# device - which is exactly when the -dbg package will not be installed:
#   ERROR: ld.so: object '/usr/lib/crashcatch.so' from LD_PRELOAD cannot be
#   preloaded (cannot open shared object file): ignored.

RDEPENDS:${PN} = "sdl12-compat"

# Deliberately unversioned .so files in a runtime package - these are replacements
# for libraries that shipped with those exact names, not development symlinks.
INSANE_SKIP:${PN} += "dev-so"

# The shims resolve some symbols lazily through dlopen/eglGetProcAddress rather
# than by linking, which the link-time checks read as under-linkage.
INSANE_SKIP:${PN} += "ldflags"
