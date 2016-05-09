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

# luna-sysmgr's upstart conf expects to be able to LD_PRELOAD ptmalloc3
RDEPENDS_${PN} = "ptmalloc3"
# luna-sysmgr's upstart conf expects to have ionice available. Under OE-core, this is supplied by util-linux.
RDEPENDS_${PN} += "util-linux"
#RDEPENDS_${PN} += "jail" #TODO

#  luna-sysmgr's upstart conf expects setcpushares-task and setcpushares-pdk to be available
VIRTUAL-RUNTIME_cpushareholder ?= "cpushareholder-stub"
RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_cpushareholder}"

PV = "3.0.0-3+git${SRCPV}"
SRCREV = "e416ecc354a59e89bf836ffd15a2d0e6a7f376fc"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_filesystem_paths
inherit webos_ports_fork_repo
inherit webos_system_bus
inherit webos_cmake_qt5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
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
        install -v -m 644 ${S}/conf/lunaAnimations.conf ${D}${webos_sysconfdir}
        install -v -m 644 ${S}/conf/timezone.txt ${D}${webos_sysconfdir}
        install -v -m 644 ${S}/conf/notificationPolicy.conf ${D}${webos_sysconfdir}
        install -v -m 644 ${S}/conf/persistentWindows.conf ${D}${webos_sysconfdir}
        install -v -m 644 ${S}/conf/default-launcher-page-layout.json ${D}${webos_sysconfdir}
    fi

    # install the mojodb file to register schema for different security policies
    install -d ${D}${webos_sysconfdir}/db/kinds
    if [ -f ${S}/mojodb/com.palm.securitypolicy ]
    then
        install -v -m 644 ${S}/mojodb/com.palm.securitypolicy ${D}${webos_sysconfdir}/db/kinds/com.palm.securitypolicy
    fi

    # install the mojodb file for the device security policy
    if [ -f ${S}/mojodb/com.palm.securitypolicy.device ]
    then
        install -v -m 644 ${S}/mojodb/com.palm.securitypolicy.device ${D}${webos_sysconfdir}/db/kinds/com.palm.securitypolicy.device
    fi

    # install the mojodb file to set permissions on security policies
    install -d ${D}${webos_sysconfdir}/db/permissions
    if [ -f ${S}/mojodb/com.palm.securitypolicy.permissions ]
    then
        install -v -m 644 ${S}/mojodb/com.palm.securitypolicy.permissions ${D}${webos_sysconfdir}/db/permissions/com.palm.securitypolicy
    fi

    # install the mojodb file to register for backup
    install -d ${D}${webos_sysconfdir}/backup
    if [ -f ${S}/mojodb/com.palm.appDataBackup ]
    then
        install -v -m 644 ${S}/mojodb/com.palm.appDataBackup ${D}${webos_sysconfdir}/backup/com.palm.appDataBackup
    fi

    if [ -f ${S}/conf/default-exhibition-apps.json ]
    then
        install -v -m 644 ${S}/conf/default-exhibition-apps.json ${D}${webos_sysconfdir}
    fi

    # install the pubsub definition file for revokations
    if [ -f ${S}/service/com.palm.appinstaller.pubsub ]
    then
        install -d ${D}${webos_sysconfdir}/pubsub_handlers
        install -v -m 0644 ${S}/service/com.palm.appinstaller.pubsub ${D}${webos_sysconfdir}/pubsub_handlers/com.palm.appinstaller
    fi
}

pkg_postinst_${PN}() {
    #!/bin/sh -e

    # We need the lock directory for the application installer which will fail if this
    # directory does not exist
    mkdir -p ${webos_cryptofsdir}/apps/var/lock
}

FILES_${PN} += "${webos_sysmgrdir} ${webos_sysconfdir} ${webos_applicationsdir} ${webos_soundsdir}"

# /usr/bin/LunaSysMgr contains RPATH pointing to sysroot
INSANE_SKIP_${PN} = "rpaths"
