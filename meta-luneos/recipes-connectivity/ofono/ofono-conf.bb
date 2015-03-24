DESCRIPTION = "Machine specific configuration for ofono"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PV = "1.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://environment.conf"

do_install() {
    install -d ${D}${localstatedir}/lib/ofono
    install -m 0644 ${WORKDIR}/environment.conf ${D}${localstatedir}/lib/ofono/
}
