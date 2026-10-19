SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0-only & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=bc807597ba062cd149d362d22d3061e7"

RDEPENDS:${PN} += " \
    qtdeclarative-qmlplugins \
    qt5compat-qmlplugins \
    qtmultimedia-qmlplugins \
    luneos-components \
    libqofono \
    libconnman-qt \
    nemo-qml-plugin-dbus \
    luna-surfacemanager-conf \
    luna-surfacemanager-base \
"

PV = "0.6-0+git"

# The card size a new xdg_shell window starts at, until
# webOS-ports/luna-next-cardshell#440 is merged
SRCREV = "8ce3315e581ba5a9441de0cec49262e9d08c4902"
WEBOS_GIT_PARAM_BRANCH = "herrie/xdg-default-window-size"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_qmake6_paths
inherit webos_tweaks
inherit webos_filesystem_paths

EXTRA_OECMAKE += "-DLUNA_NEXT_SHELL_DIR=${OE_QMAKE_PATH_QML}/WebOSCompositor"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# inheriting webos_application requires the appinfo.json file, which we don't have here.
# so just install manually db8 permissions.
do_install:append() {
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

FILES:${PN} += "${OE_QMAKE_PATH_QML}/WebOSCompositor \
                ${webos_sysconfdir}"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
