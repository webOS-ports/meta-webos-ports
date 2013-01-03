DEPENDS += "cordova"

do_install_append() {
    ln -sf ${webos_frameworksdir}/cordova/cordova.js ${D}${webos_applicationsdir}/${PN}/
}
