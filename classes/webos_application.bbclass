WEBOS_APPLICATION_NAME ?= "${PN}"

do_install_append() {
    if [ ! -e ${S}/appinfo.json ] ; then
        bberror "Can't find appinfo.json file for application ${WEBOS_APPLICATION_NAME}"
    fi

    install -d ${D}${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}

    cp ${S}/appinfo.json ${D}${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}

    if [ -d ${S}/configuration/db/kinds ] ; then
        install -d ${D}${webos_sysconfdir}/db/kinds
        install -m 0644 ${S}/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds
    fi

    if [ -d ${S}/configuration/db/permissions ] ; then
        install -d ${D}${webos_sysconfdir}/db/permissions
        install -v -m 644 $${S}/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
    fi

    if [ -d ${S}/configuration/db/activities ] ; then
        install -d ${D}${webos_sysconfdir}/activities
        cp -vrf ${S}/configuration/activities/* ${D}${webos_sysconfdir}/activities/
    fi
}

FILES_${PN} += " \
    ${webos_applicationsdir}/${WEBOS_APPLICATION_NAME} \
    ${webos_sysconfdir}"
