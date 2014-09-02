FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "7c8db19341b426fa0a7b96bfc044f6a8d29d507f"
PV = "1.14+git${SRCPV}"

SRC_URI  = " \
  git://github.com/nemomobile-packages/ofono.git;protocol=git;branch=master \
  file://ofono \
  file://0001-Disable-backtrace-cause-linking-to-libdl-fails.patch \
  file://ofono.service \
  file://wait-for-rild.sh \
"
S = "${WORKDIR}/git/ofono"

EXTRA_OECONF_append = " --disable-pushforwarder"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service

    install -d ${D}${bindir}
    install -m 0744 ${WORKDIR}/wait-for-rild.sh ${D}${bindir}
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE = "enable"
