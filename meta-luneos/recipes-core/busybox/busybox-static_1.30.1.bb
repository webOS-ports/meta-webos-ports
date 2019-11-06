FILESEXTRAPATHS_prepend := "${COREBASE}/meta/recipes-core/busybox/busybox:${COREBASE}/meta/recipes-core/busybox/files:"

require recipes-core/busybox/busybox_${PV}.bb

FILESPATHPKG =. "busybox-${PV}:"
S = "${WORKDIR}/busybox-${PV}"

do_configure_append() {
       sed -i -e '/CONFIG_STATIC/d' .config
       sed -i -e '/CONFIG_FEATURE_TAR_LONG_OPTIONS/d' .config
       echo "CONFIG_STATIC=y" >>.config
       echo "CONFIG_FEATURE_TAR_LONG_OPTIONS=y" >> .config
}


do_deploy() {
    cp ${WORKDIR}/image/bin/busybox.nosuid ${DEPLOY_DIR_IMAGE}/busybox-static
    ${STRIP} ${DEPLOY_DIR_IMAGE}/busybox-static
}

addtask deploy after do_install before do_package
