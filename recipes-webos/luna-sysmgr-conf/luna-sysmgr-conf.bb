# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "Open webOS System Manager - machine specific configuration files"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "3.0.0-3_1bcdb5bd8b97d148a3e46ae002fcf091b6d202f6"

# Don't uncomment until all of the do_*() tasks have been moved out of the recipe
#inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_machine_dep

WEBOS_REPO_NAME = "luna-sysmgr"
WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "fca0f3fba57e3120a24f6814a5c32c9e541915e9"

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
