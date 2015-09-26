FILES_${PN}-fonts += " \
                       ${D}/${datadir}/fonts"

do_install_append() {
    ### Fix up the fonts to work correctly with fontconfig
    ln -sf ${OE_QMAKE_PATH_LIBS}/fonts ${D}/${datadir}/fonts
}

