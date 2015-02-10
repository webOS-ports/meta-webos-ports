SUMMARY = "Android header files"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo

WEBOS_REPO_NAME = "phablet-headers"
WEBOS_GIT_PARAM_BRANCH = "cm-11.0"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRCREV = "890bef235d53ab09e140f02dcc27d1993df6e9df"
PV = "4.4.3+gitr${SRCPV}"

PROVIDES += "virtual/android-headers"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${includedir}/android
    cp -rv ${S}/* ${D}${includedir}/android/
    rm ${D}${includedir}/android/android-headers.pc

    if [ -e ${S}/android-config_${MACHINE}.h ] ; then
        rm ${D}${includedir}/android/android-config.h
        install ${S}/android-config_${MACHINE}.h ${D}${includedir}/android-config.h
    fi

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${S}/android-headers.pc ${D}${libdir}/pkgconfig
}

FILES_${PN}-dev += "${libdir}/pkg-config"
