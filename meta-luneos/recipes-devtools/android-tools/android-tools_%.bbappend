# android-tools 5.1.1 is 2015 AOSP source and does not compile with a C23 host
# compiler. Ubuntu 26.04's GCC 15.2 defaults to -std=gnu23, where an empty
# parameter list means "takes no arguments" rather than "unspecified":
#
#   ext4_utils/allocate.h:61: u32 allocate_inode();
#   ext4_utils/contents.c:115: error: too many arguments to function
#                              'allocate_inode'; expected 0, have 1
#
# -native and -nativesdk builds use the host compiler, so they are the ones that
# break; the target build uses OE's own toolchain and is unaffected. The recipe
# already appends to CC for these classes, so do the same rather than fighting
# CFLAGS, which its makefiles handle inconsistently.
#
# Not a LuneOS-specific problem - this belongs upstream in meta-oe.
CC:append:class-native = " -std=gnu17"
CC:append:class-nativesdk = " -std=gnu17"
