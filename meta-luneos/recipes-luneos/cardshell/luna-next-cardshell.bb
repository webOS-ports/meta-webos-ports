SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=bc807597ba062cd149d362d22d3061e7"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    qtmultimedia-qmlplugins \
    luneos-components \
    libconnman-qt5 \
    nemo-qml-plugin-dbus \
    libqofono \
    luna-next \
"

PV = "0.5.0-3+git${SRCPV}"
SRCREV = "987c4b392ad2442948cf6d575c345538300ba38f"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_tweaks
inherit webos_filesystem_paths

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# inheriting webos_application requires the appinfo.json file, which we don't have here.
# so just install manually db8 permissions.
do_install_append() {
    if [ -d ${S}/configuration/db/kinds ] ; then
        install -d ${D}${webos_sysconfdir}/db/kinds
        install -m 0644 ${S}/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds
    fi

    if [ -d ${S}/configuration/db/permissions ] ; then
        install -d ${D}${webos_sysconfdir}/db/permissions
        install -v -m 644 ${S}/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
    fi

    if [ -d ${S}/configuration/db/activities ] ; then
        install -d ${D}${webos_sysconfdir}/activities
        cp -vrf ${S}/configuration/activities/* ${D}${webos_sysconfdir}/activities/
    fi
}

FILES_${PN} += "${webos_prefix}/luna-next/shells/card \
                ${webos_sysconfdir}"
