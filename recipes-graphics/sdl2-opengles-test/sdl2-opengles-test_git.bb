SUMMARY = "SDL2 OpenGL ES Test Applications"
HOMEPAGE = "https://github.com/thp/sdl2-opengles-test"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://main_glesv2.cpp;beginline=1;endline=26;md5=0d08cb5b4a92c955950cc46dbaa3b6f0"

DEPENDS += "libsdl2"

inherit webos_filesystem_paths

PV = "1.0.6+gitr${SRCPV}"
SRC_URI = "git://github.com/thp/sdl2-opengles-test \
    file://sdl2_opengles1_test-appinfo.json \
    file://sdl2_opengles2_test-appinfo.json"
S = "${WORKDIR}/git"

SRCREV = "40041a4e3425aab6d2a7170148a3dc36975f2d09"

do_compile() {
    oe_runmake TARGETS="sdl2_opengles1_test sdl2_opengles2_test"
}

do_install() {
    for f in sdl2_opengles1_test sdl2_opengles2_test ; do
        install -d ${D}${webos_applicationsdir}/$f
        install -m 0755 ${S}/$f ${D}${webos_applicationsdir}/$f/
        install -m 0644 ${WORKDIR}/$f-appinfo.json ${D}${webos_applicationsdir}/$f/appinfo.json
    done
}

FILES_${PN} += "${webos_applicationsdir}"
FILES_${PN}-dbg += "${webos_applicationsdir}/*/.debug"
