
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://pulseaudio.service \
"

SRC_URI += " \
    file://0003-daemon-Set-default-resampler-to-speex-fixed-2.patch \
    file://0004-suspend-on-idle-Ensure-we-still-time-out-if-a-stream.patch \
    file://0005-Add-dbus-policy-for-Bluez4.patch \
    file://0006-alsa-ucm-Check-UCM-verb.patch \
    file://0007-alsa-ucm-Replace-port-device-UCM-context-assertion.patch \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pulseaudio.service ${D}${systemd_unitdir}/system

    # Out-of-tree PulseAudio modules (pulseaudio-module-palm-policy) include
    # pulsecore headers, and those refuse to compile unless config.h has been
    # included first -- several of them key struct layout off it (HAVE_CREDS,
    # HAVE_ATOMIC_BUILTINS*, WORDS_BIGENDIAN). Ship the generated config.h that
    # libpulsecore itself was built with, so those modules cannot end up with a
    # different ABI than the daemon that dlopen()s them. pulsecore.pc and the
    # rest of the pulsecore headers come from
    # pulseaudio-pulsecore-private-headers; only config.h has to come from here,
    # because only here does it exist.
    install -Dm644 ${B}/config.h ${D}${includedir}/pulsecore/config.h
}

inherit systemd

# The provide goes on the real libpulse-simple package, NOT on ${PN}.
#
# libpulse-simple0 is a name no recipe declares: debian.bbclass renames libpulse-simple to it at
# do_package time, from the soname. bitbake therefore only learns it through pkgdata -- i.e. only
# once pulseaudio has been packaged for that PACKAGE_ARCH. Building a machine whose arch has never
# been built (any halium machine: LIBHYBRIS_RDEPENDS in packagegroup-luneos-extended names
# libpulse-simple0) then dies during task-queue resolution, before anything is compiled:
#
#     ERROR: Nothing RPROVIDES 'libpulse-simple0' (but .../packagegroup-luneos-extended.bb
#     RDEPENDS on or otherwise requires it)
#
# Declaring it here puts the name in recipe metadata, so it resolves with no pkgdata present. The
# package ends up providing its own post-rename name, which is a no-op for opkg.
RPROVIDES:libpulse-simple = "libpulse-simple0"

# It must NOT be RPROVIDES:${PN}. PulseAudio packages a real
# libpulse-simple0 itself (from the libpulse-simple split), so having the -- empty
# -- ${PN} package claim the same name made opkg report it as implicitly
# obsoleting the real one, and any image pulling in "pulseaudio" failed to solve.
# The provide dates from a PulseAudio that did not split libpulse-simple out on
# its own; consumers (qtubuntu-camera, packagegroup-luneos-extended's libhybris
# set) resolve against the real package now.

SYSTEMD_PACKAGES = "${PN}-server"
SYSTEMD_SERVICE:${PN}-server = "pulseaudio.service"

# Programs using pulseaudio as backend crashed with
#  Assertion 'pthread_mutex_unlock(&m->mutex) == 0' failed at pulsecore/mutex-posix.c:106, function pa_mutex_unlock()
# so we have to drop support for pthread priority inheritance to workaround this problem.
# Actual cause seems to be a problem in eglibc which isn't fixed yet. See:
# - https://github.com/Freescale/meta-fsl-arm/commit/3e6ede30f5da132fc5e2c376c11df661efea7163
# - https://bugs.launchpad.net/ubuntu/+source/pulseaudio/+bug/932096
CACHED_CONFIGUREVARS:append:arm = " ax_cv_PTHREAD_PRIO_INHERIT=no"

inherit useradd

USERADD_PACKAGES = "pulseaudio-server"
GROUPADD_PARAM:pulseaudio-server = " \
    -g 507 -f --system pulse; \
    -g 506 -f --system pulse-access; \
"

USERADD_PARAM:pulseaudio-server = " \
    --system --home /var/run/pulse --no-create-home --shell /bin/false --groups audio,pulse --gid pulse pulse; \
"

GROUPMEMS_PARAM:pulseaudio-server = " \
    -a root -g pulse-access; \
    -a root -g audio; \
    -a system -g pulse-access; \
    -a pulse -g pulse-access; \
    -a pulse -g audio; \
"
