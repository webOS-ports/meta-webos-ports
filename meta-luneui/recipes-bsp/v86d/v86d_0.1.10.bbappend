# this is only temporary until the fbsetup and uvesafb.conf is removed in oe-core
do_install () {
    install -d ${D}${base_sbindir}
    install v86d ${D}${base_sbindir}/
}
