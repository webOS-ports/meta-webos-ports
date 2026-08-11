SUMMARY = "SDL2 OpenGL ES Test Applications"
HOMEPAGE = "https://github.com/mer-qa/sdl2-opengles-test"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://main_opengles2.cpp;beginline=1;endline=26;md5=5fdb109268909001db6a33949924f902"

DEPENDS += "libsdl2"

inherit webos_filesystem_paths pkgconfig

TARGETS = "sdl2_opengles1_test sdl2_opengles2_test"
TARGETS:rpi = "sdl2_opengles2_test"

PV = "1.0.11+git"
SRC_URI = "git://github.com/mer-qa/sdl2-opengles-test.git;branch=master;protocol=https \
    file://org.mer.app.sdl2_opengles1_test-appinfo.json \
    file://org.mer.app.sdl2_opengles2_test-appinfo.json"
S = "${WORKDIR}/git"

SRCREV = "a016b9e9708bf8e6499d02c4a47af1fa76dcef34"

do_compile() {
    oe_runmake TARGETS="${TARGETS}" CXXFLAGS="${CXXFLAGS} ${LDFLAGS}"
}

do_install() {
    for f in ${TARGETS}; do
        install -d ${D}${webos_applicationsdir}/org.mer.app.$f
        install -m 0755 ${S}/$f ${D}${webos_applicationsdir}/org.mer.app.$f/
        install -m 0644 ${WORKDIR}/org.mer.app.$f-appinfo.json ${D}${webos_applicationsdir}/org.mer.app.$f/appinfo.json
    done
}

FILES:${PN} += "${webos_applicationsdir}"
