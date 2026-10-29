inherit webos_application

DEPENDS += "nodejs-native nodejs-enyo-dev-native"

ENYO_DEV_PATH ??= "${STAGING_DIR_NATIVE}/opt/enyo-dev"

do_compile() {
    if [ -d ${S}/deploy ] ; then
        rm -rf ${S}/deploy
    fi

    # enyo-dev-dist bundles Node only for i686 and x86_64. Elsewhere (aarch64
    # build hosts) use nodejs-native, already in DEPENDS; nodejs-enyo-dev-native
    # swaps in a graceful-fs that loads on a current Node for exactly that case.
    node=${ENYO_DEV_PATH}/node_binaries/${BUILD_ARCH}/node
    [ -x $node ] || node=${STAGING_BINDIR_NATIVE}/node

    $node ${ENYO_DEV_PATH}/bin/enyo.js init && \
    $node ${ENYO_DEV_PATH}/bin/enyo.js pack --production --clean -d ${S}/deploy/${WEBOS_APPLICATION_NAME}
}

do_install:append() {
    install -d ${D}${webos_applicationsdir}
    cp -rf ${S}/deploy/${WEBOS_APPLICATION_NAME} ${D}${webos_applicationsdir}
}
