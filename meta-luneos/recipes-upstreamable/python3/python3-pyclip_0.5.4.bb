DESCRIPTION = "Cross-platform clipboard utilities supporting both binary and text data."
HOMEPAGE = "https://github.com/spyoungtech/pyclip"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2b42edef8fa55315f34f2370b4715ca9"

SRC_URI[sha256sum] = "bd7cf7ebfc6e6080d48c21007a7f09661afdec00de54a3fd48903717e67b53ea"

inherit pypi setuptools3

do_configure:append () {
    install -d ${S}/docs
    cp ${S}/README.md ${S}/docs/README.md
}

BBCLASSEXTEND = "native nativesdk"
