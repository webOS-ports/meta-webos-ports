SUMMARY = "Scrolling and cursor movement from a capacitive keyboard or trackpad"
DESCRIPTION = "Reads a touch surface that is not the touchscreen - the \
capacitive keyboard of a BlackBerry KEYone/KEY2/Passport, a trackpad beside the \
keys, a navigation pad - and replays a one-finger slide as a finger drag on the \
touchscreen, so every toolkit (Mojo, Enyo, QML, Chromium) scrolls and flings as \
it would under a real finger, with no toolkit or compositor support. With the \
cursor modifier held the same slide sends arrow keys instead, which moves the \
text cursor. The input devices are found by capability and by name and every \
number is a tunable, so a new device usually needs no configuration at all: run \
kbdscroll --list to see what was found and what was chosen."
HOMEPAGE = "https://github.com/webOS-ports/meta-webos-ports"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SECTION = "base"

inherit systemd

SRC_URI = " \
    file://kbdscroll.c \
    file://kbdscroll.conf \
    file://kbdscroll.service \
    file://profiles/athena.conf \
    file://profiles/q25.conf \
"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

# Only because of the machine profile below: the binary itself is portable and
# the same on every device.
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile() {
    # One file, libc only - uinput and evdev are kernel headers. Compiled from
    # the source's own directory so no build path reaches the binary.
    ${CC} ${CFLAGS} ${LDFLAGS} -Wall -o kbdscroll kbdscroll.c
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 kbdscroll ${D}${sbindir}/

    install -d ${D}${sysconfdir}/kbdscroll.conf.d
    install -m 0644 ${S}/kbdscroll.conf ${D}${sysconfdir}/kbdscroll.conf

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/kbdscroll.service ${D}${systemd_system_unitdir}/

    # A device with numbers of its own ships them as a profile, so the defaults
    # stay the defaults and the tuning stays readable next to the hardware it
    # describes. Adding a device is a file under profiles/ plus its SRC_URI
    # line; nothing in this recipe has to change.
    #
    # Installed by codename and selected at RUNTIME, not by ${MACHINE} at build
    # time. Most machines - q25, mp01, every Halium target - build no rootfs of
    # their own and run the generic halium-arm64 one, so a build-time
    # ${MACHINE}.conf would simply never be installed for them. Shipping all of
    # them costs a few hundred bytes each and matches how luneos-device-config
    # selects its Tier 1 adaptations; kbdscroll reads
    # by-codename/<LUNEOS_DEVICE_CODENAME>.conf (see read_conf_codename()).
    # A machine that DOES build its own rootfs keeps the build-time drop-in as
    # well, so its tuning does not become conditional on codename derivation
    # working - athena had this before by-codename existed and must not regress.
    # Loading both is harmless: same file, same values.
    if [ -f ${S}/profiles/${MACHINE}.conf ]; then
        install -m 0644 ${S}/profiles/${MACHINE}.conf \
            ${D}${sysconfdir}/kbdscroll.conf.d/10-${MACHINE}.conf
    fi

    install -d ${D}${sysconfdir}/kbdscroll.conf.d/by-codename
    for prof in ${S}/profiles/*.conf; do
        [ -f "$prof" ] || continue
        install -m 0644 "$prof" \
            ${D}${sysconfdir}/kbdscroll.conf.d/by-codename/$(basename "$prof")
    done
}

SYSTEMD_SERVICE:${PN} = "kbdscroll.service"

# Hand edits survive an upgrade. The machine profile deliberately is not a
# conffile: it is ours, it is expected to improve, and a local change belongs in
# a drop-in that sorts after it (90-local.conf) rather than in a file the
# package owns.
CONFFILES:${PN} = "${sysconfdir}/kbdscroll.conf"
