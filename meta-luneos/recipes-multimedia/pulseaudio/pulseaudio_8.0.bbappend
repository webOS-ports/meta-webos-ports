
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://pulseaudio.service \
"

SRC_URI += " \
    file://0001-core-make-dependencies-compile-for-64bit.patch \
    file://0002-build-Install-pulsecore-headers.patch \
"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pulseaudio.service ${D}${systemd_unitdir}/system
    install -d ${D}${includedir}/pulsecore/filter
    mv ${D}${includedir}/pulsecore/lfe-filter.h ${D}${includedir}/pulsecore/filter/
}

inherit systemd

SYSTEMD_PACKAGES = "${PN}-server"
SYSTEMD_SERVICE_${PN}-server = "pulseaudio.service"

# Programs using pulseaudio as backend crashed with
#  Assertion 'pthread_mutex_unlock(&m->mutex) == 0' failed at pulsecore/mutex-posix.c:106, function pa_mutex_unlock()
# so we have to drop support for pthread priority inheritance to workaround this problem.
# Actual cause seems to be a problem in eglibc which isn't fixed yet. See:
# - https://github.com/Freescale/meta-fsl-arm/commit/3e6ede30f5da132fc5e2c376c11df661efea7163
# - https://bugs.launchpad.net/ubuntu/+source/pulseaudio/+bug/932096
CACHED_CONFIGUREVARS_append_arm = " ax_cv_PTHREAD_PRIO_INHERIT=no"
