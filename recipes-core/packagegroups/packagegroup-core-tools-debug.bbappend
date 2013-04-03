# Copyright (c) 2013 LG Electronics, Inc.

PRINC := "${@int(PRINC) + 1}"

RDEPENDS_${PN} += "\
    ltrace \
    "
