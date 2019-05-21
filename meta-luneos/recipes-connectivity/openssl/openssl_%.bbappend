# Copyright (c) 2013-2014 LG Electronics, Inc.

inherit update-alternatives
ALTERNATIVE_${PN}-conf = "openssl-cnf2"
ALTERNATIVE_LINK_NAME[openssl-cnf2] = "${sysconfdir}/ssl/openssl.cnf"
ALTERNATIVE_PRIORITY[openssl-cnf2] ?= "2"

do_install_append_class-target() {
    # u-a renames it too late in do_package,
    # we don't want sstate reporting conflict when populating sysroot
    mv ${D}${sysconfdir}/ssl/openssl.cnf ${D}${sysconfdir}/ssl/openssl.cnf.${BPN}
}

FILES_openssl-conf = "${sysconfdir}/ssl/openssl.cnf*"
