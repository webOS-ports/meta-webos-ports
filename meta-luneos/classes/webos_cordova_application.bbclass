RDEPENDS:${PN} += "cordova"

WEBOS_APPLICATION_NAME ?= "${PN}"

do_install:append() {
    ln -sf ${webos_frameworksdir}/cordova/cordova.js ${D}${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}/
}
