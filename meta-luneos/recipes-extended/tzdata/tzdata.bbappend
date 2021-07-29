inherit webos_filesystem_paths

do_install:append() {
    rm -fv ${D}${sysconfdir}/localtime
    ln -sf ${webos_sysmgr_localstatedir}/preferences/localtime ${D}${sysconfdir}/localtime
}
