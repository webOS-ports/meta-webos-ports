webos_tweaksdir = "${webos_servicesdir}/org.webosinternals.tweaks.prefs/preferences"

# install tweak files from component repository
do_install_append() {
    if [ -d ${S}/files/tweaks ] ; then
        install -d ${D}${webos_tweaksdir}
        for f in `find ${S}/files/tweaks -type f` ; do
            install -m 0644 $f ${D}${webos_tweaksdir}/
        done
    fi
}

FILES_${PN} += "${webos_tweaksdir}"
