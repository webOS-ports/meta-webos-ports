SUMMARY = "Skype protocol plug-in for libpurple"
SECTION = "webos/services"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://skypeweb/gpl3.txt;md5=d90260d32cef39f3c8d6c0f05d3adb8e"

DEPENDS = "pidgin json-glib glib-2.0 zlib"

inherit pkgconfig

SRC_URI = "git://github.com/EionRobb/skype4pidgin;branch=master;protocol=git"
SRCREV = "0a276c20d4440ea6a2e0de4b82b4dd1c7e0e39a8"

S = "${WORKDIR}/git"
PV = "1.4+git${SRCPV}"

do_compile() {
    oe_runmake -C skypeweb;
}

do_install() {
    oe_runmake -C skypeweb DESTDIR="${D}" install;
}

FILES_${PN} += " \
    ${libdir} \
"
