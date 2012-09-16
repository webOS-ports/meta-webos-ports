require recipes-multimedia/pulseaudio/pulseaudio.inc

PR = "r2"
PV = "2.1+gitr${SRCPV}"
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

# overwrite from pulseaudio.inc
# module-cork-music-on-phone was renamed to module-role-cork
# http://cgit.freedesktop.org/pulseaudio/pulseaudio/commit/?id=3c5cc345472302b9511c19244b3eceb4a3674d8c
# remove this when we have fixed oe-core version in layers.txt
RDEPENDS_pulseaudio-server = " \
    pulseaudio-module-filter-apply \
    pulseaudio-module-filter-heuristics \
    pulseaudio-module-udev-detect \
    pulseaudio-module-null-sink \
    pulseaudio-module-device-restore \
    pulseaudio-module-stream-restore \
    pulseaudio-module-card-restore \
    pulseaudio-module-augment-properties \
    pulseaudio-module-detect \
    pulseaudio-module-alsa-sink \
    pulseaudio-module-alsa-source \
    pulseaudio-module-alsa-card \
    pulseaudio-module-native-protocol-unix \
    pulseaudio-module-default-device-restore \
    pulseaudio-module-intended-roles \
    pulseaudio-module-rescue-streams \
    pulseaudio-module-always-sink \
    pulseaudio-module-suspend-on-idle \
    pulseaudio-module-console-kit \
    pulseaudio-module-position-event-sounds \
    pulseaudio-module-role-cork "

# Config files from ${sysconfdir} should go into ${PN}-conf package and not
# into ${PN}-server so we fix this here until oe-core version we're using
# contains the relevant patch for this.
FILES_${PN}-server = "${bindir}/pulseaudio ${bindir}/start-* ${bindir}/pactl ${base_libdir}/udev/rules.d/*.rules"
CONFFILES_pulseaudio-server = ""
CONFFILES_pulseaudio-conf = " \
  ${sysconfdir}/pulse/default.pa \
  ${sysconfdir}/pulse/daemon.conf \
  ${sysconfdir}/pulse/client.conf"

