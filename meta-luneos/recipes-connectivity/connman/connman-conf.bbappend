
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://environment.conf \
    file://main.conf \
"

do_install:append() {
    install -d ${D}${sysconfdir}/connman
    install -m 0644 ${WORKDIR}/environment.conf ${D}${sysconfdir}/connman/
    install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/
}

FILES:${PN} += "${sysconfdir}/connman"
