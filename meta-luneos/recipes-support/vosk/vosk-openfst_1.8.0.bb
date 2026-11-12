SUMMARY = "OpenFst, as patched for Kaldi and Vosk"
DESCRIPTION = "OpenFst builds and searches weighted finite-state \
transducers. This is Alpha Cephei's copy with the fixes Kaldi and Vosk \
need. Only its static libraries and headers are built: Vosk links them \
into libvosk.so, so nothing of it goes on the device."
HOMEPAGE = "https://github.com/alphacep/openfst"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=973381090441f4eb420224655e05e064"

SRC_URI = "git://github.com/alphacep/openfst.git;protocol=https;branch=master"
SRCREV = "18e94e63870ebcf79ebb42b7035cd3cb626ec090"

inherit autotools

EXTRA_OECONF = " \
    --enable-static --disable-shared --with-pic \
    --enable-far --enable-ngram-fsts --enable-lookahead-fsts \
    --disable-bin \
"

# Static only: there is nothing to package for the device.
ALLOW_EMPTY:${PN} = "1"

# OpenFst's loadable FST types, as archives: Vosk builds with
# FST_NO_DYNAMIC_LINKING and links libfstngram.a instead.
do_install:append() {
    rm -r ${D}${libdir}/fst
}
