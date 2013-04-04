DESCRIPTION = "M's JSON parser is a small library completely written in plain ISO C which \
handles documents described by the JavaScript Object Notation (JSON) data interchange \
format."
SECTION = "libs"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.0;md5=9427b8ccf5cf3df47c29110424c9641a"

SRC_URI = " \
    http://downloads.help.palm.com/opensource/3.0.5/mjson-1.0.1.tgz;name=source \
    http://downloads.help.palm.com/opensource/3.0.5/mjson-1.0.1-patch.gz;downloadfilename=mjson-1.0.1.patch.gz;name=patch;striplevel=0"
SRC_URI[source.md5sum] = "ae205b23a210d1a3972c8356bc602f23"
SRC_URI[source.sha256sum] = "d3982d90ea3865fab20d7ddb31ce4ebba12a241d6f84eda04868ee2d44f9ca78"
SRC_URI[patch.md5sum] = "29baa32645f0e7a203f37819cb7a1223"
SRC_URI[patch.sha256sum] = "86f7ed92189aa3674446bb38e5940e347e344126c58c553b91ce7abf177c18c7"

S = "${WORKDIR}/${PN}-${PV}"

CFLAGS = "-Wno-error=unused-but-set-variable"

inherit autotools
