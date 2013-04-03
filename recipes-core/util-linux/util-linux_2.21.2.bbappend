# Copyright (c) 2013 LG Electronics, Inc.

PRINC := "${@int(PRINC) + 1}"

ALTERNATIVE_${PN} += "getopt"

ALTERNATIVE_LINK_NAME[getopt] = "${base_bindir}/getopt"
ALTERNATIVE_TARGET[getopt] = "${bindir}/getopt"
