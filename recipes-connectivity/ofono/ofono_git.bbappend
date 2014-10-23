FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "8d7f5b834a2465bb51a791e9950e722be948c823"
PV = "1.12+git${SRCPV}"

RDEPENDS_${PN} += "mobile-broadband-provider-info"

SRC_URI  = " \
  git://github.com/rilmodem/ofono.git;protocol=git;branch=master \
  file://ofono \
  file://0001-Disable-backtrace-cause-linking-to-libdl-fails.patch \
  file://ofono.service \
  file://wait-for-rild.sh \
"
S = "${WORKDIR}/git"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service

    install -d ${D}${bindir}
    install -m 0744 ${WORKDIR}/wait-for-rild.sh ${D}${bindir}
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE = "enable"
