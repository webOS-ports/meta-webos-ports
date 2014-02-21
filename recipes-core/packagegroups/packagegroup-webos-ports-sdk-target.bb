DESCRIPTION = "webOS ports Software Development Kit"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

ALLOW_EMPTY_${PN} = "1"

PR = "r1"

require packagegroup-webos-ports-sdk.inc

PACKAGES = "${PN} ${PN}-dbg"

RDEPENDS_${PN} = "\
    packagegroup-core-standalone-sdk-target \
    ${SDK-BASE} \
    ${SDK-EXTRAS}"
