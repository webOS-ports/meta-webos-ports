FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-core/busybox/busybox:${COREBASE}/meta/recipes-core/busybox/files:"

require recipes-core/busybox/busybox_${PV}.bb

S = "${WORKDIR}/busybox-${PV}"
SRC_URI += " \
            file://static-build.cfg \
            file://enable-devmem.cfg \
"

SYSTEMD_SERVICE:${PN}-syslog = ""

do_install() {
    if ! grep -q "CONFIG_FEATURE_INDIVIDUAL=y" ${B}/.config; then
        install -d ${D}${base_bindir}
        if [ "${BUSYBOX_SPLIT_SUID}" = "1" ]; then
            install -m 0755 ${B}/busybox.nosuid ${D}${base_bindir}/busybox-static
        else
            install -m 0755 ${B}/busybox ${D}${base_bindir}/busybox-static
        fi
    else
        bberror "busybox-static expects CONFIG_FEATURE_INDIVIDUAL to be enabled in busybox config"
    fi

    # just to keep do_package:prepend from busybox.inc happy:
    install -d ${D}${sysconfdir}
    touch ${D}${sysconfdir}/busybox.links
}

inherit deploy
addtask deploy after do_install before do_package

do_deploy() {
    install -m 0755 ${D}${base_bindir}/busybox-static ${DEPLOYDIR}/busybox-static
    ${STRIP} ${DEPLOYDIR}/busybox-static
}
