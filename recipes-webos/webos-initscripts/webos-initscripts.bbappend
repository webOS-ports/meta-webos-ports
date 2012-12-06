FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://cpufreq-setup.upstart"

do_install_append() {
    install -d ${D}${sysconfdir}/event.d
    install -m 0644 ${WORKDIR}/cpufreq-setup.upstart ${D}${sysconfdir}/event.d/cpufreq-setup
}
