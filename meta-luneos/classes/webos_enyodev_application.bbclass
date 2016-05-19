inherit webos_application

DEPENDS += "nodejs-native nodejs-enyo-dev"

do_compile() {
    if [ -d ${S}/deploy ] ; then
        rm -rf ${S}/deploy
    fi

    enyo pack --production -- clean -l debug -d ${S}/deploy/${WEBOS_APPLICATION_NAME}
}

do_install_append() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${WEBOS_APPLICATION_NAME} ${D}${webos_applicationsdir}
}
