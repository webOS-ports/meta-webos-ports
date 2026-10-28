SUMMARY = "Long-press actions for hardware keys: the camera key opens Camera"
DESCRIPTION = "A small daemon that watches the input devices advertising a \
configured key and runs an action once that key has been held for a while \
(600 ms by default). A short press is left alone - the device is not grabbed - \
so the key keeps whatever meaning it had. The default maps KEY_CAMERA (212) \
and Sony's full-press camera key (0x2fe) to launching org.webosports.app.camera. \
The compositor's keymap has no entry for either, which is why this does not \
live in the shell's HardwareKeys. Mappings and the threshold can be changed in \
/etc/luneos/key-actions.json; with no such key on the device it exits."
HOMEPAGE = "https://github.com/webOS-ports/meta-webos-ports"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SECTION = "base"

inherit systemd allarch

SRC_URI = " \
    file://luneos-key-actions \
    file://luneos-key-actions.service \
"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/luneos-key-actions ${D}${sbindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/luneos-key-actions.service ${D}${systemd_system_unitdir}/
}

SYSTEMD_SERVICE:${PN} = "luneos-key-actions.service"

# luna-send for the action itself.
RDEPENDS:${PN} = "python3-core python3-json luna-service2"
