# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "Open webOS System Manager - machine specific configuration files"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.0.0-3+git${SRCPV}"
SRCREV = "ec9de626bc966b20dc77e2dba436e9b8a01d8faa"

# Don't uncomment until all of the do_*() tasks have been moved out of the recipe
inherit webos_ports_fork_repo

PACKAGE_ARCH = "${MACHINE_ARCH}"

WEBOS_REPO_NAME = "luna-sysmgr"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_sysconfdir}
    install -d ${D}${webos_sysmgrdir}

    # install all the platform specific conf files
    # (no good way to do this with find/xargs, given the <base>-<machine>.conf -> <base>-platform.conf
    # name change needed on the install copy => do them all individually)

    # install the platform luna.conf file
    if [ -f conf/luna-${MACHINE}.conf ]
    then
        install -v -m 644 conf/luna-${MACHINE}.conf ${D}${webos_sysconfdir}/luna-platform.conf
    fi

    # install the platform lunaAnimations.conf file
    if [ -f conf/lunaAnimations-${MACHINE}.conf ]
    then
        install -v -m 644 conf/lunaAnimations-${MACHINE}.conf ${D}${webos_sysconfdir}/lunaAnimations-platform.conf
    fi

    # install the platform defaultPreferences.txt file
    if [ -f conf/defaultPreferences-${MACHINE}.txt ]
    then
        install -v -m 644 conf/defaultPreferences-${MACHINE}.txt ${D}${webos_sysconfdir}/defaultPreferences-platform.txt
    fi

    if [ -d platform/${MACHINE} ]
    then
        # copy over platform specific images
        if [ -d platform/${MACHINE}/images ]
        then
            cd ${S}/platform/${MACHINE} && tar --exclude-vcs --exclude-backups -cf - images | tar xf - -C ${D}${webos_sysmgrdir}
            cd ${S}
        fi
    fi
}

FILES_${PN} += "${webos_sysmgrdir}"
