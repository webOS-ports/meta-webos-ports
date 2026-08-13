# The stage0 snapshot rustc overflows its thread stack while compiling
# rustc_traits and dies with SIGSEGV:
#
#   error: rustc interrupted by SIGSEGV, printing backtrace
#   help: you can increase rustc's stack size by setting RUST_MIN_STACK=16777216
#   error: could not compile `rustc_traits` (lib)
#
# The trait solver recurses deeply enough to blow the default 8MB. Take rustc's
# own advice rather than treating it as a flaky build - it is deterministic for
# a given snapshot compiler. Applies to rust-native too (BBCLASSEXTEND).
export RUST_MIN_STACK = "16777216"
