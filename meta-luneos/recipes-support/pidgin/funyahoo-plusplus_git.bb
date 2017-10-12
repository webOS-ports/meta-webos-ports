SUMMARY = "Protocol plugin for New Yahoo (2016) for Adium, Pidgin, Miranda and Telepathy IM Framework"
SECTION = "webos/services"
LICENSE = "GPLv3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d"

#DEPENDS = "pidgin intltool-native"
DEPENDS = "pidgin json-glib glib-2.0"

inherit pkgconfig

SRC_URI = "git://github.com/EionRobb/funyahoo-plusplus;branch=master;protocol=git"
SRCREV = "cbceef10ef4388ab0bf0d654493636180802274d"

S = "${WORKDIR}/git"
#PV = "1.2.0+git${SRCPV}"

do_compile() {
    oe_runmake CC="${CC}" CXX="${CXX}" EXTRA_INCLUDES="${TARGET_CFLAGS}" AR="${AR}";
}

do_install() {
    oe_runmake DESTDIR="${D}" install;
}

FILES_${PN} += " \
    ${libdir} \
"
