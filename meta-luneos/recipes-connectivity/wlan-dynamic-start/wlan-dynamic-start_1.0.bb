SUMMARY = "Create wlan0 on a qcacld-3.0 driver built for dynamic start"
DESCRIPTION = "qcacld-3.0 can be built so that loading the module registers no netdev and \
instead exposes /dev/wlan, bringing the interface up only when \"ON\" is written to it. \
Android's WiFi HAL does that write; nothing in LuneOS did, so on athena wlan.ko loaded and \
initialised and wlan0 never appeared. This service performs the write and orders itself \
before wpa-supplicant and connman, which otherwise start against an interface that does not \
exist yet and leave the network list empty with \"No carrier\" on every scan. No-op on \
drivers that register their netdev at module load."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://wlan-dynamic-start \
    file://wlan-dynamic-start.service \
"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

inherit systemd

PR = "r0"

RDEPENDS:${PN} = "iproute2 kmod"

# Deliberately not MACHINE_ARCH. athena flashes the generic halium-arm64
# luneos-dev-image - there is no MACHINE=athena rootfs to put a device-specific
# unit in - so this has to ship to every halium machine and gate itself at
# runtime, which ConditionPathExistsGlob and the script's early exits do.
#
# "halium" is an entry in MACHINEOVERRIDES, prepended by
# conf/machine/include/meta-android-halium.inc, so it already covers every
# machine whose conf requires that include - sargo among them
# (halium:aarch64:sargo:sargo). Naming sargo as well would be redundant;
# wlan-suspend-mode.bb does, and that is worth not copying. tissot is listed
# because its conf does NOT require the include, so it has no halium override.
COMPATIBLE_MACHINE = "(tissot|halium)"

do_install() {
    install -d ${D}${sbindir} ${D}${systemd_system_unitdir}
    install -m 0755 ${UNPACKDIR}/wlan-dynamic-start ${D}${sbindir}/wlan-dynamic-start
    install -m 0644 ${UNPACKDIR}/wlan-dynamic-start.service ${D}${systemd_system_unitdir}/
}

SYSTEMD_SERVICE:${PN} = "wlan-dynamic-start.service"
SYSTEMD_AUTO_ENABLE = "enable"
FILES:${PN} += "${systemd_system_unitdir}/wlan-dynamic-start.service"
