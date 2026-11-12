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
HOMEPAGE = "https://github.com/webOS-ports/kbdscroll"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eaf38ed2270290babdd4d6b1f65effa9"
SECTION = "base"

PE = "1"
PV = "1.0+git"

# Pinned, never AUTOREV: see org.webosports.service.torch.bb.
SRCREV = "6bd79d32ad65904619a4f56ae031f89020855157"
WEBOS_GIT_PARAM_BRANCH = "main"

inherit webos_ports_repo
inherit cmake
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# The host tests are for the developer's machine (ctest, or
# tests/run-host-tests.sh with the sanitizers); nothing runs them here.
EXTRA_OECMAKE = "-DSYSTEMD_UNITDIR=${systemd_system_unitdir} -DBUILD_TESTING=OFF"

# Only because of the machine profile below: the binary itself is portable and
# the same on every device.
PACKAGE_ARCH = "${MACHINE_ARCH}"

# The CMake install puts every profile under kbdscroll.conf.d/by-codename,
# selected at RUNTIME by codename, because most machines (q25, mp01, every
# Halium target) build no rootfs of their own and run the generic one, so a
# build-time ${MACHINE}.conf would never be installed for them. A machine that
# does build its own keeps the build-time drop-in too; loading both is
# harmless, same values.
do_install:append() {
    if [ -f ${S}/profiles/${MACHINE}.conf ]; then
        install -m 0644 ${S}/profiles/${MACHINE}.conf \
            ${D}${sysconfdir}/kbdscroll.conf.d/10-${MACHINE}.conf
    fi
}

SYSTEMD_SERVICE:${PN} = "kbdscroll.service"

# Hand edits survive an upgrade. The machine profile deliberately is not a
# conffile: it is ours, it is expected to improve, and a local change belongs in
# a drop-in that sorts after it (90-local.conf) rather than in a file the
# package owns.
CONFFILES:${PN} = "${sysconfdir}/kbdscroll.conf"
