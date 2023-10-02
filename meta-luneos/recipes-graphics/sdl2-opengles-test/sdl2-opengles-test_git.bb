SUMMARY = "SDL2 OpenGL ES Test Applications"
HOMEPAGE = "https://github.com/mer-qa/sdl2-opengles-test"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://main_opengles2.cpp;beginline=1;endline=26;md5=0d08cb5b4a92c955950cc46dbaa3b6f0"

DEPENDS += "libsdl2"

inherit webos_filesystem_paths pkgconfig

TARGETS = "sdl2_opengles1_test sdl2_opengles2_test"
TARGETS:rpi = "sdl2_opengles2_test"

PV = "1.0.6+git"
SRC_URI = "git://github.com/mer-qa/sdl2-opengles-test.git;branch=master;protocol=https \
    file://sdl2_opengles1_test-appinfo.json \
    file://sdl2_opengles2_test-appinfo.json"
S = "${WORKDIR}/git"

SRCREV = "d0a3c8806cb29e3fe9dccc162c66d42dc4ebc40e"

do_compile() {
    oe_runmake TARGETS="${TARGETS}" CXXFLAGS="${CXXFLAGS} ${LDFLAGS}"
}

do_install() {
    for f in ${TARGETS}; do
        install -d ${D}${webos_applicationsdir}/$f
        install -m 0755 ${S}/$f ${D}${webos_applicationsdir}/$f/
        install -m 0644 ${WORKDIR}/$f-appinfo.json ${D}${webos_applicationsdir}/$f/appinfo.json
    done
}

FILES:${PN} += "${webos_applicationsdir}"
