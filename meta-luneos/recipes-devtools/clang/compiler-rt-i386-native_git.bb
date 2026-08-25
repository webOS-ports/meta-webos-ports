# Copyright (c) 2026 LG Electronics, Inc.
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "LLVM compiler-rt builtins for 32-bit x86, built for the build host"
DESCRIPTION = "Only the builtins, only i386, only -native. V8 requires mksnapshot \
to have the same pointer width as the target, so Chromium's cros toolchain builds \
it with current_cpu=\"x86\" for every 32-bit target (see the target_cpu test in \
build/toolchain/cros/BUILD.gn). Building that 32-bit host binary needs i386 \
compiler-rt, and oe-core's compiler-rt only ever builds for the native arch, so a \
32-bit ARM target like tenderloin-halium fails do_compile with \
  ninja: error: '.../clang/latest/lib/i386-unknown-linux-gnu/libclang_rt.builtins.a', \
  needed by 'v8_snapshot_clang_arm/...', missing and no known rule to make it \
This installs into lib/clang/<ver>/lib/linux/libclang_rt.builtins-i386.a, which is \
where webruntime's do_add_clang_latest already looks when it republishes runtimes \
under the per-triple layout Chromium expects."
HOMEPAGE = "http://compiler-rt.llvm.org/"
SECTION = "base"

require recipes-devtools/clang/common-clang.inc
require recipes-devtools/clang/common-source.inc

BPN = "compiler-rt-i386-native"

inherit cmake native

LIC_FILES_CHKSUM = "\
    file://compiler-rt/LICENSE.TXT;md5=d846d1d65baf322d4c485d6ee54e877a \
    file://libunwind/LICENSE.TXT;md5=f66970035d12f196030658b11725e1a1 \
"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS += "clang-native"

INSTALL_VER = "${@oe.utils.trim_version("${PV}", 1)}"

OECMAKE_SOURCEPATH = "${S}/runtimes"

# -m32 everywhere: the compiler is the native clang, only the output is 32-bit.
# The build host must have 32-bit glibc headers (gnu/stubs-32.h) available,
# which is the same requirement Chromium itself has for this toolchain.
CFLAGS:append = " -m32"
CXXFLAGS:append = " -m32"
LDFLAGS:append = " -m32"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=RelWithDebInfo \
                  -DCMAKE_C_COMPILER_WORKS=ON \
                  -DCMAKE_CXX_COMPILER_WORKS=ON \
                  -DCMAKE_ASM_FLAGS=-m32 \
                  -DCOMPILER_RT_STANDALONE_BUILD=ON \
                  -DCOMPILER_RT_INCLUDE_TESTS=OFF \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -DCOMPILER_RT_BUILD_SANITIZERS=OFF \
                  -DCOMPILER_RT_BUILD_MEMPROF=OFF \
                  -DCOMPILER_RT_BUILD_LIBFUZZER=OFF \
                  -DCOMPILER_RT_BUILD_ORC=OFF \
                  -DCOMPILER_RT_BUILD_PROFILE=OFF \
                  -DCOMPILER_RT_BUILD_CTX_PROFILE=OFF \
                  -DCOMPILER_RT_BUILD_BUILTINS=ON \
                  -DCOMPILER_RT_DEFAULT_TARGET_ONLY=ON \
                  -DCOMPILER_RT_DEFAULT_TARGET_ARCH=i386 \
                  -DCMAKE_C_COMPILER_TARGET=i686-linux-gnu \
                  -DCMAKE_ASM_COMPILER_TARGET=i686-linux-gnu \
                  -DLLVM_ENABLE_RUNTIMES=compiler-rt \
                  -DLLVM_ENABLE_PER_TARGET_RUNTIME_DIR=OFF \
                  -DLLVM_APPEND_VC_REV=OFF \
                  -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
                  -DCOMPILER_RT_INSTALL_PATH=${nonarch_libdir}/clang/${INSTALL_VER} \
"

FILES:${PN} += "${nonarch_libdir}/clang/${INSTALL_VER}/lib/linux/*"

# Nothing here is for the target; this is a build-time helper only.
BBCLASSEXTEND = ""
