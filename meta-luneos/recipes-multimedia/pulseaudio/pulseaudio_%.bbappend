
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://pulseaudio.service \
"

SRC_URI += " \
    file://0003-daemon-Set-default-resampler-to-speex-fixed-2.patch \
    file://0004-suspend-on-idle-Ensure-we-still-time-out-if-a-stream.patch \
    file://0005-Add-dbus-policy-for-Bluez4.patch \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pulseaudio.service ${D}${systemd_unitdir}/system
}

inherit systemd

RPROVIDES:${PN} = "libpulse-simple0"

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
    -a system -g pulse-access; \
    -a pulse -g pulse-access; \
    -a pulse -g audio; \
"
