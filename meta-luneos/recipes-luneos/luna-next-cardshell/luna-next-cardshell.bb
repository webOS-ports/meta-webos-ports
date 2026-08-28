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

# herrie/audiod: the shell's volume paths still called
# org.webosports.service.audio, the audio-service LuneOS no longer ships.
# audiod claims that bus name, so the calls were answered with "Couldn't find
# method: volumeUp" and the keys, the on-screen indicator and the system-menu
# Points at the fingerprint PR branch (off master) for the lockscreen
# fingerprint unlock. NOTE: this branch is off master and does NOT carry the
# herrie/audiod volume fix; reconcile the two (or repoint to master) once the
# luna-next-cardshell fingerprint PR merges.
WEBOS_GIT_PARAM_BRANCH = "fingerprint"
SRCREV = "f02e9e0fb8c0fbbab631d628f522d4346d0c0943"

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
