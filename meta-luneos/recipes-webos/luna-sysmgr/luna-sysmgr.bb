# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "Open webOS System Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "json-c luna-service2 sqlite3 luna-sysmgr-ipc luna-sysmgr-ipc-messages pmloglib librolegen nyx-lib openssl luna-webkit-api luna-prefs libpbnjson freetype luna-sysmgr-common"
DEPENDS += "qtbase"
DEPENDS += "qtsensors"
DEPENDS += "serviceinstaller"
#DEPENDS += "localization" #TODO
#RDEPENDS_${PN} += "jail" #TODO

RDEPENDS_${PN} += "powerd"

PV = "3.0.0-3+git${SRCPV}"
SRCREV = "d7c49dc32bc6223d6a7d7b67d353a02323fadff0"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_filesystem_paths
inherit webos_ports_fork_repo
inherit webos_system_bus
inherit webos_cmake_qt5
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};"
WEBOS_GIT_PARAM_BRANCH = "herrie/ls2-acg-fixes"
S = "${WORKDIR}/git"

do_install_append() {

    install -d ${D}${webos_cryptofsdir}/apps/var/lock

    # install images & low-memory files
    install -d ${D}${webos_sysmgrdir}/images
    install -v -m 644 ${S}/images/* ${D}${webos_sysmgrdir}/images
    install -d ${D}${webos_sysmgrdir}/low-memory
    install -v -m 644 ${S}/low-memory/* ${D}${webos_sysmgrdir}/low-memory

    # install the schema files
    install -d ${D}${webos_sysconfdir}/schemas/
    install -v -m 644 ${S}/conf/launcher-conf.schema ${D}${webos_sysconfdir}/schemas/

    # install temporary sounds
    install -d ${D}${webos_soundsdir}
    install -v -m 644 ${S}/sounds/* ${D}${webos_soundsdir}

    # install the luna.conf file if it exists in the source
    if [ -f ${S}/conf/luna.conf ]
    then
        install -d ${D}${webos_sysconfdir}
        install -v -m 644 ${S}/conf/luna.conf ${D}${webos_sysconfdir}
    fi

    # install the db kind to register schema for context upload (collecting error logs)
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -v -m 644 ${S}/files/db/kinds/com.palm.contextupload ${D}${webos_sysconfdir}/db/kinds/com.palm.contextupload

    # install the permissions on context upload
    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/files/db/permissions/com.palm.contextupload ${D}${webos_sysconfdir}/db/permissions/com.palm.contextupload

    # install the db kind to register schema for different security policies
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -v -m 644 ${S}/files/db/kinds/com.palm.securitypolicy ${D}${webos_sysconfdir}/db/kinds/com.palm.securitypolicy

    # install the db kind for the device security policy
    install -v -m 644 ${S}/files/db/kinds/com.palm.securitypolicy.device ${D}${webos_sysconfdir}/db/kinds/com.palm.securitypolicy.device

    # install the permissions on security policies
    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/files/db/permissions/com.palm.securitypolicy ${D}${webos_sysconfdir}/db/permissions/com.palm.securitypolicy

    # install the db kind to register for backup
    install -d ${D}${webos_sysconfdir}/backup
    install -v -m 644 ${S}/files/db/kinds/com.palm.sysMgrDataBackup ${D}${webos_sysconfdir}/backup/com.palm.sysMgrDataBackup

    if [ -f ${S}/conf/default-exhibition-apps.json ]
    then
        install -v -m 644 ${S}/conf/default-exhibition-apps.json ${D}${webos_sysconfdir}
    fi

    # install the pubsub definition file for revokations
    if [ -f ${S}/files/sysbus/com.palm.appinstaller.pubsub ]
    then
        install -d ${D}${webos_sysconfdir}/pubsub_handlers
        install -v -m 0644 ${S}/files/sysbus/com.palm.appinstaller.pubsub ${D}${webos_sysconfdir}/pubsub_handlers/com.palm.appinstaller
    fi
}

FILES_${PN} += "${webos_sysmgrdir} ${webos_sysconfdir} ${webos_applicationsdir} ${webos_soundsdir} ${webos_cryptofsdir}"
