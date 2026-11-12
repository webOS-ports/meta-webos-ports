SUMMARY = "Kaldi speech recognition toolkit, Vosk branch"
DESCRIPTION = "The parts of Kaldi that Vosk's recogniser is made of, from \
Alpha Cephei's vosk branch. Built as static libraries with -fPIC and \
linked into libvosk.so, as Vosk's own releases do, so nothing of it goes \
on the device."
HOMEPAGE = "https://kaldi-asr.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../COPYING;md5=a10e448a64dbd3723ff3fb2f397fba2e"

SRC_URI = " \
    git://github.com/alphacep/kaldi.git;protocol=https;branch=vosk \
    file://0001-matrix-call-LAPACK-through-OpenBLAS-s-LAPACK_-macros.patch;patchdir=.. \
"
SRCREV = "bc5baf14231660bd50b7d05788865b4ac6c34481"
PV = "5.5+git"

S = "${UNPACKDIR}/${BP}/src"
B = "${S}"

DEPENDS = "openblas vosk-openfst"

# The libraries Vosk links (its src/Makefile), and what they need.
KALDI_DIRS = "base matrix util feat tree gmm transform fstext hmm lm decoder \
              lat cudamatrix nnet3 rnnlm chain ivector online2"

do_configure() {
    # Kaldi's configure runs a probe program when the target looks like the
    # build machine; it never is here.
    sed -i -e 's/if \[\[ "$TARGET_ARCH" == "`uname -m`" \]\] ; then/if false ; then/' configure
    CXX="${CXX}" AR="${AR}" AS="${AS}" RANLIB="${RANLIB}" ./configure \
        --static \
        --use-cuda=no \
        --mathlib=OPENBLAS \
        --openblas-root=${STAGING_DIR_TARGET}${prefix} \
        --fst-root=${STAGING_DIR_TARGET}${prefix} \
        --fst-version=1.8.0 \
        --host=${HOST_SYS}
    # OpenFst is in the sysroot's /usr/include, and as -isystem that comes
    # before libstdc++'s own, so its #include_next <stdlib.h> finds nothing.
    # As -I, GCC drops it as a copy of a system directory.
    sed -i -e 's/-isystem $(OPENFSTINC)/-I$(OPENFSTINC)/' kaldi.mk
    grep -q -- '-I$(OPENFSTINC)' kaldi.mk
}

do_compile() {
    oe_runmake ${KALDI_DIRS} EXTRA_CXXFLAGS="-fPIC ${CXXFLAGS}"
}

do_install() {
    install -d ${D}${libdir} ${D}${includedir}/kaldi/itf
    install -m 0644 itf/*.h ${D}${includedir}/kaldi/itf/
    for dir in ${KALDI_DIRS}; do
        install -d ${D}${includedir}/kaldi/$dir
        install -m 0644 $dir/*.h ${D}${includedir}/kaldi/$dir/
        install -m 0644 $dir/kaldi-$dir.a ${D}${libdir}/libkaldi-$dir.a
    done
}

ALLOW_EMPTY:${PN} = "1"
