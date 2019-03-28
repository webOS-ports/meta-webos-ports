# Work around broken ld-2.29.so when gold is used
# causing qemu-arm to segfault during e.g. gobject-introspection
# or postinst at do_rootfs time, more details in:
# http://lists.openembedded.org/pipermail/openembedded-devel/2019-March/198937.html

base_do_compile_prepend_arm() {
    export LDFLAGS="-fuse-ld=bfd"
}
