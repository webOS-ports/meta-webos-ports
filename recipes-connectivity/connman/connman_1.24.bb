require connman.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://src/main.c;beginline=1;endline=20;md5=486a279a6ab0c8d152bcda3a5b5edc36"

FILESEXTRAPATHS_prepend := "${COREBASE}/meta/recipes-connectivity/${BPN}/${BPN}:"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://connman.service \
            "
SRC_URI[md5sum] = "dd6e1b4d9b9a28d127edb9f9b58bdec1"
SRC_URI[sha256sum] = "551df7a5f0c6e4d69523dd2b3aa2c54525b323457d5135f64816215bad3dc24c"

RRECOMMENDS_${PN} = "connman-conf"

do_install_append() {
    rm ${D}${systemd_unitdir}/system/connman.service
    install -m 0644 ${WORKDIR}/connman.service ${D}${systemd_unitdir}/system/
}
