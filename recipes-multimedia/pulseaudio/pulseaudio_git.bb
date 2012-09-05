require recipes-multimedia/pulseaudio/pulseaudio.inc

PR = "r0"
PV	 = "2.1+gitr${SRCPV}"
DEPENDS += "libjson gdbm speex libxml-parser-perl-native"

inherit gettext perlnative

# NOTE: We're using a git version of pulseaudio here as we need ALSA UCM support which
# isn't yet available in a release version. When a newer version is released which
# includes ALSA UCM support we have to switch to it.
SRC_URI = " \
  git://anongit.freedesktop.org/pulseaudio/pulseaudio;protocol=git;branch=master \
  file://volatiles.04_pulse"
SRCREV = "34ab73b9ac51ba865d299fe2c3c2916e867e94f4"
S = "${WORKDIR}/git"

EXTRA_OECONF += " --with-module-dir=${libdir}/pulse-${PV}/modules"

do_configure_prepend() {
    touch config.rpath
    echo "${PV}" >.tarball-version
    echo "${PV}" >.version
}

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}
