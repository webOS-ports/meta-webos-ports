# Copyright (c) 2013 LG Electronics, Inc.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

CERT_SOURCE_DIR = "${datadir}/ca-certificates"
CERT_TARGET_DIR = "${sysconfdir}/ssl/certs"

inherit webos_certificates

do_install_append() {
    cd ${D}${sysconfdir}/ssl/certs
    for a in *.pem
    do
        if [ -e $a ] ; then
            cat $a >>${D}${sysconfdir}/ssl/certs/ca-certificates.crt
        fi
    done
}

