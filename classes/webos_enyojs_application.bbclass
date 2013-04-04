inherit webos_application

DEPENDS += " nodejs-native "

do_compile() {
    # We need the build directory so create it when it not exists
    if [ ! -d ${S}/build ] ; then
        mkdir -p ${S}/build
    fi

    # FIXME: use native node from sysroot
    node enyo/tools/deploy.js -o ${S}/deploy/${WEBOS_APPLICATION_NAME}
}
