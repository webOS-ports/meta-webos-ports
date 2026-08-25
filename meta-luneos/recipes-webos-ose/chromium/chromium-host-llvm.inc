# Copyright (c) 2026 LG Electronics, Inc.
#
# Build Chromium with the host's LLVM instead of the one Chromium ships.
#
# third_party/llvm-build/Release+Asserts is 874 MB of prebuilt clang and is not
# in webos-ports/chromium151 - we compile with OE's clang-native. Anything that
# runs gn against these sources therefore has to say so, or
# //build/config/compiler reads
#     //third_party/llvm-build/Release+Asserts/cr_build_revision
# to identify the bundled toolchain and gn gen stops with "Could not read file".
#
# Required by both webruntime-clang_151.bb and mksnapshot-cross_151.bb. It used
# to live only in the former, which is why mksnapshot broke as soon as it was
# actually built from the git repo rather than the tarball.

# compiler-rt-native supplies libclang_rt.builtins*, which Chromium links
# explicitly and do_add_clang_latest below republishes under the per-triple
# layout. Without it that task finds no runtime to copy, creates only
# latest/include, and the link fails on
#   .../clang/latest/lib/x86_64-unknown-linux-gnu/libclang_rt.builtins.a
DEPENDS += "clang-native lld-native compiler-rt-native"

# 32-bit targets additionally need i386 compiler-rt. V8 builds mksnapshot with
# the target's pointer width, so build/toolchain/cros/BUILD.gn pins
# current_cpu = "x86" whenever target_cpu is arm/x86/mipsel, and that 32-bit
# host binary wants
#   lib/clang/latest/lib/i386-unknown-linux-gnu/libclang_rt.builtins.a
# oe-core's compiler-rt only ever builds the native arch, so meta-luneos carries
# compiler-rt-i386-native for it. Pulled in only where it is needed, since it is
# a second compiler-rt build.
DEPENDS:append:arm = " compiler-rt-i386-native"
DEPENDS:append:x86 = " compiler-rt-i386-native"

# ${STAGING_DIR_NATIVE}/usr, not ${STAGING_DIR_NATIVE}${prefix}: cross.bbclass
# redefines prefix to already include the native sysroot, so the latter doubles
# the path for mksnapshot-cross (inherit cross) and ninja then looks for
# .../recipe-sysroot-native/home/.../recipe-sysroot-native/usr/lib/clang/...
# It happens to be right for target recipes like webruntime-clang, where prefix
# is plain /usr - which is why this only broke once the args were shared.
GN_ARGS += "clang_base_path=\"${STAGING_DIR_NATIVE}/usr\""
GN_ARGS += "clang_use_chrome_plugins=false"
GN_ARGS += "clang_version=\"latest\""

do_add_clang_latest () {
    # Creates a `latest` directory in the native sysroot's /usr/lib/clang that
    # is a copy of /usr/lib/clang/$CLANG_VERSION. Chromium manually links
    # against libclang_rt.builtins.a and uses the `clang_version` GN variable to
    # find it. Headers are copied too so bindgen can find system headers -
    # leaving them out produces a confusing "'limits.h' file not found" from
    # #include_next rather than anything that points at this directory.
    #
    # Lifted from meta-browser's chromium-gn.inc; keep the two in step.
    cd "${STAGING_LIBDIR_NATIVE}/clang"
    rm -rf latest

    for dir in *; do
        if [ -d "$dir" ] && [ -n "$(find $dir -name 'libclang_rt.builtins*' 2>/dev/null)" ]; then
            echo "Copying clang directory $dir to latest (runtime libraries)"
            cp -r "$dir" latest
            break
        fi
    done

    headers_copied=false
    for dir in *; do
        if [ -d "$dir/include" ]; then
            echo "Found headers in $dir/include, copying to latest/include"
            mkdir -p latest/include
            cp -r "$dir/include/"* latest/include/
            headers_copied=true
            break
        fi
    done

    if [ "$headers_copied" = false ]; then
        echo "Warning: No Clang headers found in any versioned directories"
    fi

    # Chromium 151 links compiler-rt from the per-target-triple layout,
    #     lib/<triple>/libclang_rt.builtins.a
    # while OE's compiler-rt still installs the older
    #     lib/linux/libclang_rt.builtins-<arch>.a
    # Without this, do_compile stops immediately with
    #   ninja: error: '.../clang/latest/lib/x86_64-unknown-linux-gnu/libclang_rt.builtins.a',
    #   needed by '.../liballoc_error_handler_impl.a', missing and no known rule to make it
    # Derive the name Chromium wants from the one OE ships. Source BOTH sysroots:
    # the native clang only carries builtins for the build host's arch, so on a
    # cross build (e.g. tissot-halium/aarch64) the target's
    # libclang_rt.builtins-aarch64.a exists only under ${STAGING_LIBDIR}. Looking
    # at latest/lib/linux alone silently yields host-only builtins and do_compile
    # dies on the missing aarch64-unknown-linux-gnu copy.
    for rt in latest/lib/linux/libclang_rt.builtins-*.a \
              ${STAGING_LIBDIR}/clang/*/lib/linux/libclang_rt.builtins-*.a; do
        [ -e "$rt" ] || continue
        arch=$(basename "$rt" .a)
        arch=${arch#libclang_rt.builtins-}
        # OE names the 32-bit ARM runtimes after the float ABI (arm / armhf),
        # but Chromium asks for the Rust-style triple with the armv7 arch name
        # and the eabi suffix - armv7-unknown-linux-gnueabihf. Mapping only the
        # arch, as everything else needs, produces armhf-unknown-linux-gnu and
        # do_compile stops with that file "missing and no known rule to make it".
        case "$arch" in
            armhf) triple="armv7-unknown-linux-gnueabihf" ;;
            arm)   triple="armv7-unknown-linux-gnueabi" ;;
            *)     triple="${arch}-unknown-linux-gnu" ;;
        esac
        echo "Providing compiler-rt as lib/${triple}/libclang_rt.builtins.a"
        mkdir -p "latest/lib/${triple}"
        cp "$rt" "latest/lib/${triple}/libclang_rt.builtins.a"
    done
}
addtask add_clang_latest after do_configure before do_compile
