SECTION = "libs"
DESCRIPTION = "Presage is an intelligent predictive text entry system."
LICENSE = "GPL-2.0+"

DEPENDS = "sqlite3 libtinyxml"

inherit autotools gettext

SRC_URI = " \
    http://downloads.sourceforge.net/presage/presage-${PV}.tar.gz \
    file://disable-help2man.patch;striplevel=0 \
"
S = "${WORKDIR}/presage-${PV}"

EXTRA_OECONF = " \
    --disable-documentation \
    --disable-gpresagemate \
    --disable-gprompter \
    --disable-python-binding \
"
