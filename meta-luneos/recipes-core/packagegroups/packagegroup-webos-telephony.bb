DESCRIPTION = "Telephony components use by the Open webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PHONE_RDEPENDS = " \
    voicecall \
    org.webosports.app.phone \
"

RDEPENDS_${PN} = " \
    ofono \
    webos-telephonyd \
    ${@base_contains('MACHINE_FEATURES', 'phone', '${PHONE_RDEPENDS}', '', d)} \
"
