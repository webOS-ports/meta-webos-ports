SUMMARY = "Vosk: offline speech recognition"
DESCRIPTION = "Vosk's recogniser library, libvosk, with Kaldi and OpenFst \
linked in as Vosk's own releases have them. It turns speech into text on \
the device, as it is spoken. A model (vosk-model-*) per language is needed \
to use it."
HOMEPAGE = "https://alphacephei.com/vosk/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../COPYING;md5=d09bbd7a3746b6052fbd78b26a87396b"

SRC_URI = "git://github.com/alphacep/vosk-api.git;protocol=https;branch=master;tag=v${PV}"
SRCREV = "a7bf6a51e299152a8fb496b928a21eb79a1d7bea"

S = "${UNPACKDIR}/${BP}/src"
B = "${S}"

DEPENDS = "vosk-kaldi vosk-openfst openblas"

# Vosk's src/Makefile, with the Kaldi and OpenFst archives from the
# sysroot and OpenBLAS shared.
VOSK_KALDI_LIBS = "online2 decoder ivector gmm tree feat lat lm rnnlm hmm nnet3 \
                   transform cudamatrix matrix fstext util base chain"

do_configure() {
    # The link strips libvosk.so (-s); leave that to packaging.
    sed -i -e 's/--shared -s -o/--shared -o/' Makefile
}

do_compile() {
    oe_runmake \
        CXX="${CXX}" \
        KALDI_ROOT=${STAGING_INCDIR}/kaldi \
        OPENFST_ROOT=${STAGING_DIR_TARGET}${prefix} \
        HAVE_OPENBLAS_CLAPACK=0 \
        USE_SHARED=0 \
        LIBS="" \
        EXTRA_CFLAGS="-I${STAGING_INCDIR}/kaldi -DHAVE_OPENBLAS ${CXXFLAGS}" \
        EXTRA_LDFLAGS="${LDFLAGS} -Wl,-soname,libvosk.so.0 \
            -Wl,--start-group ${@' '.join('-lkaldi-' + l for l in d.getVar('VOSK_KALDI_LIBS').split())} \
            -lfst -lfstngram -Wl,--end-group -lopenblas -lpthread"
}

do_install() {
    install -d ${D}${libdir} ${D}${includedir}
    install -m 0755 libvosk.so ${D}${libdir}/libvosk.so.${PV}
    ln -sf libvosk.so.${PV} ${D}${libdir}/libvosk.so.0
    ln -sf libvosk.so.${PV} ${D}${libdir}/libvosk.so
    install -m 0644 vosk_api.h ${D}${includedir}/
}
