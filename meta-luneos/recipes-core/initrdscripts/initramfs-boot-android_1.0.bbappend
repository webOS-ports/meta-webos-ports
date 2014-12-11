FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://fixups.sh"

do_install_append() {
    install -m 0755 ${WORKDIR}/fixups.sh ${D}/
}

FILES_${PN} += "/fixups.sh"
