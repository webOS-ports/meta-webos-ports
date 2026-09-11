# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "QML widgets and runtime framework for webOS apps"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=9e100013a76448cbe7c14134b0974453 \
"

DEPENDS = "qt-features-webos qtdeclarative qtwayland-webos pmloglib luna-service2 qttools-native"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION')[0] == '6' else '' }"
RDEPENDS:${PN} = "qt5compat-qmlplugins"

RPROVIDES:${PN}-examples = " \
    eos.bare \
    eos.widgetgallery \
"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0011 patch series in this directory now lives as commits there, so
# nothing is applied on top any more. Pinned with a plain SRCREV - submission
# tags are a webosose convention and this branch carries none. The branch
# itself is set below via WEBOS_GIT_PARAM_BRANCH.
SRCREV = "64c86e76b0d7332e5d955ace286a7d7b0f8fb907"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 1.0.0-171, so anything lower
# would look like a downgrade to opkg on an update.
PV = "1.0.0-172"

PR = "r40"

inherit webos_qmake6
inherit pkgconfig
inherit webos_app_generate_security_files
inherit webos_filesystem_paths
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Perform extra QML validation
#WEBOS_QMLLINT_EXTRA_VALIDATION = "1"

FILES:${PN} += "${OE_QMAKE_PATH_QML}/Eos/*"

PACKAGES += "${PN}-examples"
FILES:${PN}-examples += " \
    ${webos_applicationsdir}/* \
    ${datadir}/qml/locales/${BPN}/ \
"

# unit-tests
PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests += "${datadir}/booster/tests/*"

# SDK tools
PACKAGES += "${PN}-tools"
FILES:${PN}-tools += "${webos_sdkdir}/*"

# we don't provide cmake tests
EXTRA_QMAKEVARS_POST += "CONFIG-=create_cmake"

# ERROR: qml-webos-framework-1.0.0-169-r38 do_package_qa: QA Issue: File /usr/src/debug/qml-webos-framework/1.0.0-169/src/Eos/Items/qrc_beziergon.cpp in package qml-webos-framework-src contains reference to TMPDIR
# File /usr/src/debug/qml-webos-framework/1.0.0-169/examples/com.webos.exampleapp.canvastablet/.rcc/qrc_com.webos.exampleapp.canvastablet.cpp in package qml-webos-framework-src contains reference to TMPDIR
# File /usr/src/debug/qml-webos-framework/1.0.0-169/examples/com.webos.exampleapp.tabletevent/.rcc/qrc_com.webos.exampleapp.tabletevent.cpp in package qml-webos-framework-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
