# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "Open webOS System Manager - machine specific configuration files"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.1"

inherit webos_filesystem_paths

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://luna-platform.conf \
           file://lunaAnimations-platform.conf \
           file://defaultPreferences-platform.txt \
           "

do_install() {
    install -d ${D}${webos_sysconfdir}
    install -d ${D}${webos_sysmgrdir}

    # Install all the platform specific conf files

    # Install the platform luna.conf file
    install -v -m 0644 ${WORKDIR}/luna-platform.conf ${D}${webos_sysconfdir}/

    # Install the platform lunaAnimations.conf file
    install -v -m 0644 ${WORKDIR}/lunaAnimations-platform.conf ${D}${webos_sysconfdir}/

    # Install the platform defaultPreferences.txt file
    install -v -m 644 ${WORKDIR}/defaultPreferences-platform.txt ${D}${webos_sysconfdir}/

    # Copy over platform specific images, if any
    if [ -d ${WORKDIR}/images ]
    then
        pushd ${WORKDIR}
        tar --exclude-vcs --exclude-backups -cf - images | tar xf - -C ${D}${webos_sysmgrdir}
        popd
    fi
}

FILES_${PN} += "${webos_sysmgrdir}\
                ${webos_sysconfdir}"
