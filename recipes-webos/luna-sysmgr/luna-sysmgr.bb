# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "Open webOS System Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "cjson luna-service2 sqlite3 luna-sysmgr-ipc luna-sysmgr-ipc-messages pmloglib librolegen nyx-lib openssl luna-webkit-api luna-prefs libpbnjson freetype luna-sysmgr-common"
DEPENDS += "qtbase qtquick1"
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

WEBOS_VERSION = "3.0.0-3_b7c8f26142bcf5941f3f2ebdd33647709f1bb74c"

# Don't uncomment until all of the do_*() tasks have been moved out of the recipe
#inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
# Uncomment once installing into /usr/sbin instead of /usr/bin
#inherit webos_daemon

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "b76d11f083d268e01d69966e2521c96e6f30bb15"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

#install_loc() {
#    # generate all the localized files in the resources directory
#    COMPONENT_NAME=luna-sysmgr
#    ${STAGING_DIR}/xliffs/tool/buildloc ${COMPONENT_NAME} ${STAGING_DIR}/xliffs $(echo ${LOCALES} | tr " " ,)
#    if [ -f ${COMPONENT_NAME}_newstrings.xliff ]
#    then
#        mv -f *.xliff ${STAGING_DIR}/xliffs/new
#    fi

#    tar --exclude-vcs --exclude-backups -cf - localization | tar xf - -C ${D}${webos_sysmgrdir}
#    #ln -s en_gb ${D}${webos_sysmgrdir}/localization/en_ie
#    if [ -d ${D}${webos_sysmgrdir}/localization/es_mx ]
#    then
#        rm -rf ${D}${webos_sysmgrdir}/localization/es_mx
#    fi
#    ln -sf es_us ${D}${webos_sysmgrdir}/localization/es_mx
#}

do_install_append() {
    # install images & low-memory files
#    bbnote "install images and low-memory files"
    install -d ${D}${webos_sysmgrdir}
    cd ${S} && tar --exclude-vcs --exclude-backups -cf - images | tar xf - -C ${D}${webos_sysmgrdir}
    cd ${S} && tar --exclude-vcs --exclude-backups -cf - uiComponents | tar xf - -C ${D}${webos_sysmgrdir}
    install -d ${D}${webos_sysmgrdir}/low-memory
    install -v -m 644 low-memory/* ${D}${webos_sysmgrdir}/low-memory

    if [ -d bin ]
    then
#        bbnote "install ime bin files"
        install -d ${D}${webos_sysmgrdir}/bin
        install -v -m 644 bin/* ${D}${webos_sysmgrdir}/bin
    fi
    
    # install sysmgr builtins apps
#    bbnote "install sysmgr builtins apps"
    if [ -d sysapps ]
    then
        
    #    for sysappDir in $( ls ${S}/sysapps )
    #    do    
            #Now install the localization json files
            # generate all the localized files in the resources directory
    #        cd ${S}/sysapps/${sysappDir}
    #        ${STAGING_DIR}/xliffs/tool/buildloc -e luna-sysmgr ${STAGING_DIR}/xliffs $(echo ${LOCALES} | tr " " ,)
            
            #Workaround the luna-sysmgr 'workaround', basically, revert to normal behavior
    #        if [ -d localization ]
    #        then
    #            mv -f localization resources
    #        fi
    #        find ./ -name strings.json | xargs rm -rf
    #    done

        
        install -d ${D}${webos_applicationsdir}
        cd ${S}/sysapps && tar --exclude-vcs --exclude-backups -cf - * | tar xf - -C ${D}${webos_applicationsdir}
        cd ${S}
        
    fi
    
    # Install launcher things
#    bbnote "Install launcher things"
    
    # install sysmgr builtins apps
#    bbnote "install sysmgr builtins apps"
    cd ${S}
    if [ -d sysapps ]
    then
        install -d ${D}${webos_applicationsdir}
        cd ${S}/sysapps && tar --exclude-vcs --exclude-backups -cf - * | tar xf - -C ${D}${webos_applicationsdir}
        cd ${S}
    fi

    # install localization
    #install_loc

    # install the schema files
    install -d ${D}${webos_sysconfdir}/schemas/
    #install -v -m 644 conf/localization.schema ${D}${webos_sysconfdir}/schemas/
    install -v -m 644 conf/launcher-conf.schema ${D}${webos_sysconfdir}/schemas/

    # install temporary sounds
    install -d ${D}${webos_soundsdir}
    install -v -m 644 sounds/* ${D}${webos_soundsdir}

    # install into event.d so we run.
    install -d ${D}${webos_upstartconfdir}
    install -v -m 644 LunaSysMgr.upstart ${D}${webos_upstartconfdir}/LunaSysMgr
    install -v -m 644 LunaReady.upstart ${D}${webos_upstartconfdir}/LunaReady

    # install the luna.conf file if it exists in the source
    if [ -f conf/luna.conf ]
    then
        install -d ${D}${webos_sysconfdir}
        install -v -m 644 conf/luna.conf ${D}${webos_sysconfdir}
        install -v -m 644 conf/lunaAnimations.conf ${D}${webos_sysconfdir}
        install -v -m 644 conf/timezone.txt ${D}${webos_sysconfdir}
        install -v -m 644 conf/notificationPolicy.conf ${D}${webos_sysconfdir}
        install -v -m 644 conf/persistentWindows.conf ${D}${webos_sysconfdir}
        install -v -m 644 conf/default-launcher-page-layout.json ${D}${webos_sysconfdir}
    fi

    # install all the platform specific conf files
    # (no good way to do this with find/xargs, given the <base>-<machine>.conf -> <base>-platform.conf
        # name change needed on the install copy => do them all individually)

    # install the mojodb file to register schema for different security policies
    install -d ${D}${webos_sysconfdir}/db_kinds
    if [ -f mojodb/com.palm.securitypolicy ]
    then
        install -v -m 644 mojodb/com.palm.securitypolicy ${D}${webos_sysconfdir}/db_kinds/com.palm.securitypolicy
    fi

    # install the mojodb file for the device security policy
    if [ -f mojodb/com.palm.securitypolicy.device ]
    then
        install -v -m 644 mojodb/com.palm.securitypolicy.device ${D}${webos_sysconfdir}/db_kinds/com.palm.securitypolicy.device
    fi

    # install the mojodb file to set permissions on security policies
    install -d ${D}${webos_sysconfdir}/db/permissions
    if [ -f mojodb/com.palm.securitypolicy.permissions ]
    then
        install -v -m 644 mojodb/com.palm.securitypolicy.permissions ${D}${webos_sysconfdir}/db/permissions/com.palm.securitypolicy
    fi

    # install the mojodb file to register for backup
    install -d ${D}${webos_sysconfdir}/backup
    if [ -f mojodb/com.palm.appDataBackup ]
    then
        install -v -m 644 mojodb/com.palm.appDataBackup ${D}${webos_sysconfdir}/backup/com.palm.appDataBackup
    fi

    if [ -f conf/default-exhibition-apps.json ]
    then
        install -v -m 644 conf/default-exhibition-apps.json ${D}${webos_sysconfdir}
    fi

    # install the pubsub definition file for revokations
    if [ -f service/com.palm.appinstaller.pubsub ]
    then
        install -d ${D}${webos_sysconfdir}/pubsub_handlers
        install -v -m 0644 service/com.palm.appinstaller.pubsub ${D}${webos_sysconfdir}/pubsub_handlers/com.palm.appinstaller
    fi
}

do_clean_prepend() {
    os.system('cd ' + bb.data.expand('${S}', d) + ' && [ -f Makefile ] && make distclean')
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
