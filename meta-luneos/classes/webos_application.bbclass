WEBOS_APPLICATION_NAME ?= "${PN}"
WEBOS_APPLICATION_TARGET_DIR ?= "${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}"
WEBOS_APPLICATION_DB8_KIND_LOCATION ?= "${S}/configuration/db/kinds"
WEBOS_APPLICATION_DB8_PERMISSION_LOCATION ?= "${S}/configuration/db/permissions"
WEBOS_APPLICATION_ACTIVITY_LOCATION ?= "${S}/configuration/activities"

do_install_append() {
    if [ ! -e ${S}/appinfo.json ] ; then
        bberror "Can't find appinfo.json file for application ${WEBOS_APPLICATION_NAME}"
    fi

    install -d ${D}${WEBOS_APPLICATION_TARGET_DIR}
    cp ${S}/appinfo.json ${D}${WEBOS_APPLICATION_TARGET_DIR}

    if [ -d ${WEBOS_APPLICATION_DB8_KIND_LOCATION} ] ; then
        install -d ${D}${webos_sysconfdir}/db/kinds
        install -m 0644 ${WEBOS_APPLICATION_DB8_KIND_LOCATION}/* ${D}${webos_sysconfdir}/db/kinds
    fi

    if [ -d ${WEBOS_APPLICATION_DB8_PERMISSION_LOCATION} ] ; then
        install -d ${D}${webos_sysconfdir}/db/permissions
        install -v -m 644 ${WEBOS_APPLICATION_DB8_PERMISSION_LOCATION}/* ${D}${webos_sysconfdir}/db/permissions
    fi

    if [ -d ${WEBOS_APPLICATION_ACTIVITY_LOCATION} ] ; then
        install -d ${D}${webos_sysconfdir}/activities
        cp -vrf ${WEBOS_APPLICATION_ACTIVITY_LOCATION}/* ${D}${webos_sysconfdir}/activities/
    fi
}

FILES_${PN} += " \
    ${WEBOS_APPLICATION_TARGET_DIR} \
    ${webos_sysconfdir}"
