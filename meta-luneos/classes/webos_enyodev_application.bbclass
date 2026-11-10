inherit webos_application

DEPENDS += "nodejs-native nodejs-enyo-dev-native"

ENYO_DEV_PATH ??= "${STAGING_DIR_NATIVE}/opt/enyo-dev"
# nodejs-native's node, which runs on every build host; enyo-dev no longer
# ships its own x86-only one.
ENYO_DEV_NODE ??= "${STAGING_BINDIR_NATIVE}/node"

do_compile() {
    if [ -d ${S}/deploy ] ; then
        rm -rf ${S}/deploy
    fi

    ${ENYO_DEV_NODE} ${ENYO_DEV_PATH}/bin/enyo.js init && \
    ${ENYO_DEV_NODE} ${ENYO_DEV_PATH}/bin/enyo.js pack --production --clean -d ${S}/deploy/${WEBOS_APPLICATION_NAME}
}

do_install:append() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${WEBOS_APPLICATION_NAME} ${D}${webos_applicationsdir}
}
