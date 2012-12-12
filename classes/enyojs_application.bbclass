DEPENDS += " nodejs-native "

do_compile() {
    # We need the build directory so create it when it not exists
    if [ ! -d ${S}/build ] ; then
        mkdir -p ${S}/build
    fi

    # FIXME: use native node from sysroot
    node enyo/tools/deploy.js -o ${S}/deploy/${PN}
}

do_install() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${PN} ${D}${webos_applicationsdir}

    if [ ! -e ${S}/appinfo.json ] ; then
        bberror "Can't find appinfo.json file for application ${PN}"
    fi

    cp ${S}/appinfo.json ${D}${webos_applicationsdir}/${PN}
}

PACKAGES = "${PN}"
FILES_${PN} = "${webos_applicationsdir}/${PN}"
