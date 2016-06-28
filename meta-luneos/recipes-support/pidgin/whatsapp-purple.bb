SUMMARY = "WhatsApp LibPurple Plugin"
SECTION = "webos/services"
LICENSE = "GPLv2.0+"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=6b066e36c765e83968e71478f54618f3"

DEPENDS = "pidgin protobuf-native protobuf freeimage"

SRCREV = "07ed931a3dd04c98a5f36e52129aa2cfe743a024"

inherit pkgconfig

SRC_URI = "git://github.com/davidgfnet/whatsapp-purple"
PV = "0.9.0+git${SRCPV}"
S = "${WORKDIR}/git"

do_compile() {
    oe_runmake CC="${CC}" CXX="${CXX}" EXTRA_INCLUDES="${TARGET_CFLAGS}" AR="${AR}";
}

do_install() {
    oe_runmake DESTDIR="${D}" install;
}

FILES_${PN} += " \
    ${libdir} \
    ${datadir} \
"
