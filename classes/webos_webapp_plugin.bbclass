DEPENDS += "webapp-launcher"

FILES_${PN}-dbg += "${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}/plugins/.debug"
FILES_${PN} += "${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}/plugins/"
