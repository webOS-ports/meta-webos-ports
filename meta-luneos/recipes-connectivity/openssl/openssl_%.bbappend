# Copyright (c) 2013-2014 LG Electronics, Inc.

EXTENDPRAUTO_append = "webos8"

inherit update-alternatives
ALTERNATIVE_${PN} = "openssl-cnf openssl-cnf2"
ALTERNATIVE_LINK_NAME[openssl-cnf] = "${libdir}/ssl/openssl.cnf"
ALTERNATIVE_PRIORITY[openssl-cnf] ?= "1"
ALTERNATIVE_LINK_NAME[openssl-cnf2] = "${sysconfdir}/ssl/openssl.cnf"
ALTERNATIVE_PRIORITY[openssl-cnf2] ?= "1"

do_install_append_class-target() {
    # u-a renames it too late in do_package,
    # we don't want sstate reporting conflict when populating sysroot
    mv ${D}${libdir}/ssl/openssl.cnf ${D}${libdir}/ssl/openssl.cnf.${BPN}
    mv ${D}${sysconfdir}/ssl/openssl.cnf ${D}${sysconfdir}/ssl/openssl.cnf.${BPN}
}
