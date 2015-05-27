FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

RDEPENDS_${PN} += "sed zip fbprogress"

SRC_URI += " \
    file://fixups.sh \
    file://system-updater.sh \
"

do_install_append() {
    install -m 0755 ${WORKDIR}/fixups.sh ${D}/
    install -m 0755 ${WORKDIR}/system-updater.sh ${D}
}

FILES_${PN} += "/fixups.sh /system-updater.sh"
