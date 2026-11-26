# luneos.inc sets INSANE_SKIP:append:armv7a = " 32bit-time" for every recipe.
# This recipe only unpacks and patches the source that clang-native,
# llvm-native, compiler-rt and the rest build from, and packages nothing, so
# the skip has no effect here - but do_recipe_qa reads INSANE_SKIP, and with
# it in the hash every one of those -native recipes got armv7a-only hashes:
# a 32-bit ARM build rebuilt the whole native LLVM/Clang stack, and
# everything built with it (mesa-native, gtk+3-native, rust-native, ...),
# instead of taking it from sstate built for any other machine.
do_recipe_qa[vardepsexclude] += "INSANE_SKIP"
