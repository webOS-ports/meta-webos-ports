# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

SUMMARY = "Utility for testing GLES shaders on a device."
LICENSE = "Apache-2.0"
SECTION = "devtools"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

DEPENDS = "virtual/egl"

SRC_URI = "git://github.com/Tofee/simple-shader.git;protocol=https;branch=main"
S = "${WORKDIR}/git"

PV = "0.0.1+git"
SRCREV = "d373356857a36707d740b65300a74ffaf74c14ae"

do_compile() {
    cd ${S}
    oe_runmake test-shader
}

do_install() {
    install -m 0755 -d ${D}${bindir}
    oe_runmake install DESTDIR=${D}${bindir}
}
