FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "8be724836e3e3c9f3331a4866317f61023f709e0"
PV = "1.14+git${SRCPV}"

RDEPENDS_${PN} += "mobile-broadband-provider-info"

SRC_URI  = " \
  git://github.com/nemomobile-packages/ofono.git;protocol=git;branch=master \
  file://ofono \
  file://0001-Disable-backtrace-cause-linking-to-libdl-fails.patch \
  file://ofono.service \
  file://wait-for-rild.sh \
"
S = "${WORKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree and
# override ${SEPB} rather than ${B} as seperatebuilddir.inc has higher
# priority than we have and ${B} getsoverriden again after we set it to
# the right value here.
SEPB = "${S}"

EXTRA_OECONF_append = " --disable-pushforwarder"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service

    install -d ${D}${bindir}
    install -m 0744 ${WORKDIR}/wait-for-rild.sh ${D}${bindir}
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE = "enable"
