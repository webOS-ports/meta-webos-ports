FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI_append = " file://wpa-supplicant.init"

INITSCRIPT_NAME = "wpa-supplicant.sh"
INITSCRIPT_PARAMS = "start 04 5 2 3 . stop 24 0 1 6 ."

inherit update-rc.d

do_install_append() {
    mkdir -p ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/wpa-supplicant.init ${D}${sysconfdir}/init.d/wpa-supplicant.sh
}
