# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.
# Copyright (c) 2013 LG Electronics, Inc.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://no_bash_required-webos.patch \
            file://dhclient.upstart"

# Add dhclient upstart script
# (script should be configured to only run dhclient when needed)

do_install_append () {
        install -d ${D}${webos_upstartconfdir}
        install -m 0644 ${WORKDIR}/dhclient.upstart ${D}${webos_upstartconfdir}/dhclient
}

FILES_dhcp-client += "${webos_upstartconfdir}/dhclient"

