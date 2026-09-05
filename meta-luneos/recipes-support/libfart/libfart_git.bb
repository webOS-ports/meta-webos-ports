SUMMARY = "Face analysis and recognition toolkit"
DESCRIPTION = "libfart runs the three stages of a face-unlock pipeline over \
caller-supplied BGR frames: face detection, optional passive presentation-attack \
detection (MiniFASNet V2) and MobileFaceNet embedding + match. It ships its own \
TensorFlow Lite models and does no capture of its own. From FuriLabs, who use it \
behind their biomd biometrics daemon; LuneOS drives it from luneos-faced instead."
HOMEPAGE = "https://github.com/FuriLabs/face-analysis-recognition-toolkit"
SECTION = "webos/support"

# Note: GPL-2.0-only. Anything linking libfart - luneos-faced included - has to
# be GPL-2 compatible.
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=d991426cc497df74365f4de93ce6b1d0"

# gtk+3/gstreamer are only needed by the "test" target, which we do not build.
DEPENDS = "opencv libtensorflow-lite flatbuffers libgbinder glib-2.0 nlohmann-json"

PV = "0.1+git"
SRCREV = "c9a664c71927d6d1862fee40f04ea47b8fc6af61"
SRC_URI = " \
    git://github.com/FuriLabs/face-analysis-recognition-toolkit.git;protocol=https;branch=forky \
    file://0001-Makefile-make-flags-and-install-paths-cross-build-fr.patch \
    file://0002-detector-make-the-NNAPI-delegate-optional.patch \
"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"

# libgbinder is MACHINE_ARCH, and opencv/tensorflow-lite tuning differs per
# machine, so do not let this be shared as an allarch/tune-only package.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig

EXTRA_OEMAKE = " \
    PREFIX=${prefix} \
    LIBDIR=${libdir} \
    INCLUDEDIR=${includedir} \
    DATADIR=${datadir} \
"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install DESTDIR=${D}
}

# Upstream ships an unversioned libfart.so and no SONAME, so the runtime
# library would otherwise land in -dev.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} = "dev-so"

# detect-class1 / mini_fas_net_v2 / mobile_face_net .tflite models
FILES:${PN} += "${datadir}/fart"
