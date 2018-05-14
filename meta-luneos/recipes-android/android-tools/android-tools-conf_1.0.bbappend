FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0001_Enable_FunctionFS.install_patch "

do_install_prepend() {
    if [ "${MACHINE}" != "tenderloin" ]; then 
      patch -d ${WORKDIR} < ${WORKDIR}/0001_Enable_FunctionFS.install_patch
    fi
}

