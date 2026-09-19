SUMMARY = "Keep the device awake while a remote shell is attached"
DESCRIPTION = "Holds a kernel wakelock (/sys/power/wake_lock) for as long as \
an ssh session (per-connection sshd@ unit, hooked through a drop-in) or an \
adb shell/transfer (5 s poll on adbd) is attached, so a device that suspends on \
battery does not suspend under a developer. Plain USB charging holds nothing. \
No-op on kernels without CONFIG_PM_WAKELOCKS."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "base"

inherit systemd

SRC_URI = " \
    file://luneos-remote-wakelock \
    file://luneos-remote-wakelock.service \
    file://sshd-remote-wakelock.conf \
"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/luneos-remote-wakelock ${D}${sbindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/luneos-remote-wakelock.service ${D}${systemd_system_unitdir}/

    # openssh's sshd.socket is Accept=yes, so every connection is an
    # sshd@<instance>.service and one template drop-in covers them all. The
    # drop-in is inert if the unit is absent (sshd.service mode, dropbear);
    # the watcher's TCP check covers those.
    install -d ${D}${systemd_system_unitdir}/sshd@.service.d
    install -m 0644 ${UNPACKDIR}/sshd-remote-wakelock.conf \
        ${D}${systemd_system_unitdir}/sshd@.service.d/luneos-remote-wakelock.conf
}

SYSTEMD_SERVICE:${PN} = "luneos-remote-wakelock.service"

FILES:${PN} += "${systemd_system_unitdir}/sshd@.service.d"

# pgrep from procps (busybox's lacks nothing we use, but the image ships
# procps' anyway), flock and logger from util-linux; awk and sleep are busybox.
RDEPENDS:${PN} = "procps util-linux-flock util-linux-logger"
