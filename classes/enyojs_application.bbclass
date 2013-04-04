DEPENDS += " nodejs-native "

WEBOS_APPLICATION_NAME ?= "${PN}"

do_compile() {
    # We need the build directory so create it when it not exists
    if [ ! -d ${S}/build ] ; then
        mkdir -p ${S}/build
    fi

    # FIXME: use native node from sysroot
    node enyo/tools/deploy.js -o ${S}/deploy/${WEBOS_APPLICATION_NAME}
}

do_install() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${WEBOS_APPLICATION_NAME} ${D}${webos_applicationsdir}

    if [ ! -e ${S}/appinfo.json ] ; then
        bberror "Can't find appinfo.json file for application ${WEBOS_APPLICATION_NAME}"
    fi

    cp ${S}/appinfo.json ${D}${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}
}

PACKAGES = "${PN}"
FILES_${PN} = "${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}"
