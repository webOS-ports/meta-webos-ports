# webruntime 151 runs gn-native in place of the x86-64 gn that Chromium ships
# in src/buildtools/linux64, so build the revision Chromium 151 pins in DEPS
# (gn_version) rather than oe-core's, which is 107 commits older. Bump this
# together with the webruntime SRCREV when Chromium's gn_version changes.
SRCREV = "1d86777e7f2562a86ecea77d1809ac4f82bb5bfe"

# Build only gn itself, which is all do_install takes. At this revision the
# default target also runs simple_integration_test, which compares generated
# .ninja files against goldens that assume the build directory is two levels
# below the source tree ("../../integration_tests/..."); with oe-core's
# B = ${WORKDIR}/build and S under ${WORKDIR}/sources the paths differ and the
# test fails, although gn is fine.
do_compile() {
    ninja -C ${B} --verbose gn
}
