inherit webos_application

DEPENDS += "nodejs-native nodejs-enyo-dev"

ENYO_DEV_PATH ??= "${STAGING_DIR_NATIVE}/opt/enyo-dev"

do_compile() {
    if [ -d ${S}/deploy ] ; then
        rm -rf ${S}/deploy
    fi

    ${ENYO_DEV_PATH}/node_binaries/${BUILD_ARCH}/node ${ENYO_DEV_PATH}/bin/enyo.js init

    ${ENYO_DEV_PATH}/node_binaries/${BUILD_ARCH}/node ${ENYO_DEV_PATH}/bin/enyo.js pack --production --clean -l debug -d ${S}/deploy/${WEBOS_APPLICATION_NAME}
}

do_install_append() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${WEBOS_APPLICATION_NAME} ${D}${webos_applicationsdir}
}
