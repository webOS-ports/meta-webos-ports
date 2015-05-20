FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

do_install_append() {
    # As ${sysconfdir} might be read-only we setup the resolv.conf for those case to
    # point to connman as DNS proxy which will handle all DNS requests. For cases
    # where the file is writable it will get overriden as before.
    install -d ${D}${sysconfdir}
    echo "nameserver 127.0.0.1" > ${D}${sysconfdir}/resolv.conf
}
