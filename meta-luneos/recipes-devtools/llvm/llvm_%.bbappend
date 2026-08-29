# Tell LLVM which machine it is being built *for*.
#
# LLVM's CMake derives LLVM_NATIVE_ARCH from the host it is configured on, and
# oe-core's llvm recipe never overrides it for class-target. Cross-building for
# ARM on an x86-64 machine therefore produces a target libLLVM whose
# llvm-config.h says:
#
#   #define LLVM_HOST_TRIPLE "x86_64-unknown-linux-gnu"
#   #define LLVM_NATIVE_ARCH X86
#   /* #undef LLVM_NATIVE_TARGET */
#
# X86 is not in LLVM_TARGETS_TO_BUILD (which is "AMDGPU;<target arch>"), so
# LLVM_NATIVE_TARGET is left undefined - and LLVMInitializeNativeTarget() is
#
#   #ifdef LLVM_NATIVE_TARGET  ... #else return 1;
#
# so it registers nothing and reports failure. Anything relying on the native
# target then fails: llvmpipe cannot create an execution engine at all, and
# reports "Unable to find target for this triple (no targets are registered)".
#
# This is not emulation-specific. It applies to every cross-built OE target,
# which means llvmpipe has never had a working JIT on real ARM devices either.
EXTRA_OECMAKE:append:class-target = " -DLLVM_HOST_TRIPLE=${TARGET_SYS}"
