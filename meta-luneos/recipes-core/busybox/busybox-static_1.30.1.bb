FILESEXTRAPATHS_prepend := "${COREBASE}/meta/recipes-core/busybox/busybox:${COREBASE}/meta/recipes-core/busybox/files:"

require recipes-core/busybox/busybox_${PV}.bb

FILESPATHPKG =. "busybox-${PV}:"
S = "${WORKDIR}/busybox-${PV}"

do_configure_append() {
       sed -i -e '/CONFIG_STATIC/d' .config
       echo "CONFIG_STATIC=y" >>.config
}


do_deploy() {
    cp ${WORKDIR}/image/bin/busybox.nosuid ${DEPLOY_DIR_IMAGE}/busybox-static
    ${STRIP} ${DEPLOY_DIR_IMAGE}/busybox-static
}

addtask deploy after do_install before do_package
