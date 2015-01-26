inherit webos_application

DEPENDS += " nodejs-native "

do_compile() {
    if [ -d ${S}/deploy ] ; then
        rm -rf ${S}/deploy
    fi

    node ${S}/enyo/tools/deploy.js -v -o ${S}/deploy/${WEBOS_APPLICATION_NAME}
}

do_install_append() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${WEBOS_APPLICATION_NAME} ${D}${webos_applicationsdir}
}
