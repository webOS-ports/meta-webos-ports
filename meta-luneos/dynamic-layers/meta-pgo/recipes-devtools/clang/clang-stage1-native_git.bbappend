# meta-pgo's stage1 clang builds only the X86 backend, and compiler-rt's
# builtins are then configured for the build host. On an aarch64 build host
# (the OSL arm64 runner) that fails with "unknown target triple 'unknown'"
# once the 3000-step LLVM build is done. The stage1 clang only ever compiles
# native recipes (clang-profdata-native, clang-native, llvm-native), so the
# build host's own backend is the one it needs; LLVM resolves "host" to it
# (llvm/cmake/config-ix.cmake), which on x86-64 is X86 as before.
EXTRA_OECMAKE:remove = "-DLLVM_TARGETS_TO_BUILD=X86"
EXTRA_OECMAKE:append = " -DLLVM_TARGETS_TO_BUILD=host"
