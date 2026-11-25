# What nodejs24 in this layer already does: the native build has no use for
# the V8 QEMU wrapper - 0001-Using-native-binaries.patch, the only thing that
# calls it, is applied for class-target only - but do_create_v8_qemu_wrapper
# still ran for nodejs-native, and qemu_wrapper_cmdline put the target's tune
# (DEFAULTTUNE, TUNE_PKGARCH, QEMU_EXTRAOPTIONS) into its signature. That made
# nodejs-native, and node-gyp-native, enact-dev-native and the rest built on
# it, specific to the target architecture: every architecture built its own.
python __anonymous () {
    if bb.data.inherits_class('native', d):
        bb.build.deltask('do_create_v8_qemu_wrapper', d)
}

# The recipe's do_compile installs the wrapper unconditionally.
do_compile:class-native () {
    install -D ${RECIPE_SYSROOT_NATIVE}/etc/ssl/openssl.cnf ${B}/deps/openssl/nodejs-openssl.cnf
    oe_runmake BUILDTYPE=Release
}
