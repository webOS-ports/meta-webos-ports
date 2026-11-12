SUMMARY = "OpenBLAS: an optimised BLAS and LAPACK library"
DESCRIPTION = "OpenBLAS is an optimised BLAS library based on GotoBLAS2. \
Built here with its LAPACK compiled from C (C_LAPACK), so no Fortran \
compiler is needed. Kaldi, and so Vosk speech recognition, uses it."
HOMEPAGE = "https://www.openblas.net/"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5adf4792c949a00013ce25d476a2abc0"

SRC_URI = " \
    git://github.com/OpenMathLib/OpenBLAS.git;protocol=https;branch=develop;tag=v${PV} \
    file://0001-Makefile.arm-let-NO_ARCH_FLAGS-leave-the-architecture.patch \
"
SRCREV = "e0166008be8e466242aa76b2ff75ce3f0fbf574a"

# OpenBLAS's TARGET (TargetList.txt) for each machine. 32-bit ARM's ARMV7
# needs VFPv3-D32, which every NEON core has (Cortex-A8 and later). The
# 64-bit targets pick their kernels at run time (DYNAMIC_ARCH) from the
# given baseline up.
OPENBLAS_TARGET = "${TARGET_ARCH}"
OPENBLAS_TARGET:arm = "ARMV7"
OPENBLAS_TARGET:aarch64 = "ARMV8"
OPENBLAS_TARGET:x86-64 = "NEHALEM"
OPENBLAS_DYNAMIC_ARCH = "0"
OPENBLAS_DYNAMIC_ARCH:aarch64 = "1"
OPENBLAS_DYNAMIC_ARCH:x86-64 = "1"
OPENBLAS_BINARY = "32"
OPENBLAS_BINARY:aarch64 = "64"
OPENBLAS_BINARY:x86-64 = "64"

# NO_ARCH_FLAGS: the toolchain's -mcpu/-mfpu choose the architecture.
# Single-threaded with locking, as Vosk's own builds use it: the
# recogniser's matrices are small, and the caller's threads stay safe.
EXTRA_OEMAKE = " \
    HOSTCC='${BUILD_CC}' \
    CC='${CC}' \
    CROSS=1 \
    CROSS_SUFFIX=${HOST_PREFIX} \
    PREFIX=${prefix} \
    LIBDIR=${libdir} \
    TARGET=${OPENBLAS_TARGET} \
    BINARY=${OPENBLAS_BINARY} \
    DYNAMIC_ARCH=${OPENBLAS_DYNAMIC_ARCH} \
    NOFORTRAN=1 \
    C_LAPACK=1 \
    USE_THREAD=0 \
    USE_OPENMP=0 \
    USE_LOCKING=1 \
    NO_STATIC=1 \
    NO_ARCH_FLAGS=1 \
    COMMON_OPT='${CFLAGS}' \
"

# The default goal builds the library, then LAPACK into it, then the shared
# object, in turn (as separate goals under -j they write the archive at
# once). Tests are not built when cross-compiling.
do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake DESTDIR=${D} install
    # Left empty: no programs are built.
    rmdir ${D}${bindir}
    # The library is installed as libopenblas_<core>-r<version>.so, which
    # packaging takes for a development link; give it its soname instead.
    real=$(readlink ${D}${libdir}/libopenblas.so.0)
    rm ${D}${libdir}/libopenblas.so.0 ${D}${libdir}/libopenblas.so
    mv ${D}${libdir}/$real ${D}${libdir}/libopenblas.so.0
    ln -s libopenblas.so.0 ${D}${libdir}/libopenblas.so
}

FILES:${PN}-dev += "${libdir}/cmake"
