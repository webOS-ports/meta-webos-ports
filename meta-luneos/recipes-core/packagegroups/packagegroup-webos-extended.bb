# Copyright (c) 2012-2023 LG Electronics, Inc.

DESCRIPTION = "meta-webos components used in LuneOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# You don't need to change this value when you're changing just RDEPENDS:${PN} variable.

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

VIRTUAL-RUNTIME_webappmanager ?= "wam"
VIRTUAL-RUNTIME_initscripts ?= "initscripts"
VIRTUAL-RUNTIME_webos-compositor ?= "luna-surfacemanager"
VIRTUAL-RUNTIME_appinstalld ?= "appinstalld2"
VIRTUAL-RUNTIME_event-monitor-network ?= "event-monitor-network"
VIRTUAL-RUNTIME_surface-manager ?= "luna-surfacemanager-base"
VIRTUAL-RUNTIME_surface-manager-conf ?= "luna-surfacemanager-conf"
VIRTUAL-RUNTIME_surface-manager-extension ?= ""
VIRTUAL-RUNTIME_webos-ime ?= ""
VIRTUAL-RUNTIME_novacomd ?= "novacomd"
VIRTUAL-RUNTIME_com.webos.app.browser ?= "com.webos.app.enactbrowser"
VIRTUAL-RUNTIME_com.webos.app.camera ?= "com.webos.app.camera"
VIRTUAL-RUNTIME_com.webos.app.mediagallery ?= "com.webos.app.mediagallery"
VIRTUAL-RUNTIME_com.webos.app.notification ?= "com.webos.app.notification"
VIRTUAL-RUNTIME_com.webos.app.statusbar ?= "com.webos.app.statusbar"
VIRTUAL-RUNTIME_com.webos.app.volume ?= "com.webos.app.volume"
#LuneOS doesn't have a homeapp
VIRTUAL-RUNTIME_com.webos.app.home ?= ""
VIRTUAL-RUNTIME_pdm ?= "com.webos.service.pdm"
VIRTUAL-RUNTIME_ai ?= "com.webos.service.ai"
VIRTUAL-RUNTIME_memorymanager ?= "com.webos.service.memorymanager"
VIRTUAL-RUNTIME_g-media-pipeline ?= "g-media-pipeline"
VIRTUAL-RUNTIME_g-camera-pipeline ?= "g-camera-pipeline"
VIRTUAL-RUNTIME_nodejs-module-node-red ?= "node-red"
VIRTUAL-RUNTIME_contextintentmgr ?= "com.webos.service.contextintentmgr"
VIRTUAL-RUNTIME_mojoservicelauncher ?= "mojoservicelauncher"
VIRTUAL-RUNTIME_com.webos.service.flowmanager ?= "com.webos.service.flowmanager"
VIRTUAL-RUNTIME_com.webos.service.intent ?= "com.webos.service.intent"
VIRTUAL-RUNTIME_tts ?= "com.webos.service.tts"
VIRTUAL-RUNTIME_com.webos.service.mediacontroller ?= "com.webos.service.mediacontroller"
VIRTUAL-RUNTIME_umediaserver ?= "umediaserver"
VIRTUAL-RUNTIME_mediarecorder ?= "com.webos.service.mediarecorder"
VIRTUAL-RUNTIME_com.webos.service.cec ?= "com.webos.service.cec"
VIRTUAL-RUNTIME_com.webos.service.location ?= "com.webos.service.location"
#LuneOS uses Just Type/Universal Search from legacy webOS
#VIRTUAL-RUNTIME_unifiedsearch ?= "com.webos.service.unifiedsearch com.webos.service.unifiedsearch-plugins"
VIRTUAL-RUNTIME_unifiedsearch ?= "luna-applauncher luna-universalsearchmgr"

# We're not using VIRTUAL-RUNTIME because VIRTUAL-RUNTIME is usually used for only
# one item and changing that in <distro>-preferred-providers.inc would require
# .bbappend in meta-<distro> to do PR/PRINC/PR_append bump anyway so it's easier
# to change this variable in .bbappend together with bump.
#

WEBOS_PACKAGESET_TESTAPPS = " \
    bareapp \
    com.webos.app.test.enact \
    com.webos.app.test.v8snapshot \
    com.webos.app.test.webosose \
    com.webos.app.test.webrtc \
    com.webos.app.test.youtube \
"

# Enyo 1 and related framework packages
WEBOS_PACKAGESET_ENYO_1 = " \
    enyo-1.0 \
    foundation-frameworks \
    loadable-frameworks \
    mojoservice-frameworks \
    underscore \
    luna-init-fonts \
"

WEBOS_PACKAGESET_SYSTEMAPPS = " \
    ${VIRTUAL-RUNTIME_unifiedsearch} \
    luna-systemui \
    app-services \
    core-apps \
    mojomail-imap \
    mojomail-pop \
    mojomail-smtp \
"

# This packageset controls which time zone packages should be included in webOS.
# Since any application that uses localtime will indirectly depend on presence of
# time zone data, we pull in those packages as a top-level dependency. By
# assigning the list to its own variable, we have the option to only include a
# subset should there be a device that will only be used within some region.
WEBOS_PACKAGESET_TZDATA ?= " \
    tzdata \
    tzdata-core \
    tzdata-africa \
    tzdata-americas \
    tzdata-antarctica \
    tzdata-arctic \
    tzdata-asia \
    tzdata-atlantic \
    tzdata-australia \
    tzdata-europe \
    tzdata-misc \
    tzdata-pacific \
    tzdata-posix \
    tzdata-right \
"

WEBOS_PACKAGESET_UI = " \
    ${VIRTUAL-RUNTIME_webappmanager} \
    ${VIRTUAL-RUNTIME_surface-manager} \
    ${VIRTUAL-RUNTIME_surface-manager-conf} \
    ${VIRTUAL-RUNTIME_surface-manager-extension} \
    ${VIRTUAL-RUNTIME_webos-ime} \
    ${VIRTUAL-RUNTIME_com.webos.app.browser} \
    ${VIRTUAL-RUNTIME_com.webos.app.notification} \
    ${VIRTUAL-RUNTIME_com.webos.app.volume} \
    ${VIRTUAL-RUNTIME_com.webos.app.statusbar} \
"

WEBOS_PACKAGESET_ENACTAPPS = " \
    com.webos.app.imageviewer \
    ${VIRTUAL-RUNTIME_com.webos.app.mediagallery} \
    com.webos.app.videoplayer \
    com.webos.app.videocall \
"

WEBOS_PACKAGESET_MEDIA = " \
    gstreamer1.0 \
    gstreamer1.0-libav \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-ugly \
    ${VIRTUAL-RUNTIME_umediaserver} \
    com.webos.app.videoplayer \
    ${VIRTUAL-RUNTIME_com.webos.service.mediacontroller} \
    ${VIRTUAL-RUNTIME_mediarecorder} \
    com.webos.service.mediaindexer \
"


# nyx-lib needs nyx-modules at runtime, but a runtime dependency is not defined
# in its recipe because nyx-modules is MACHINE_ARCH (e.g. qemux86), while nyx-lib is
# TUNE_PKGARCH  (e.g. i586). Instead, it is pulled into the image by adding it here.
# (There are more details as to why this was done in nyx-lib.bb.)

RDEPENDS:${PN} = " \
    activitymanager \
    ${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'attr smack com.webos.app.test.smack.native', '', d)} \
    ${VIRTUAL-RUNTIME_com.webos.app.camera} \
    ${VIRTUAL-RUNTIME_com.webos.app.home} \
    ${VIRTUAL-RUNTIME_g-camera-pipeline} \
    ${VIRTUAL-RUNTIME_g-media-pipeline} \
    bootd \
    configd \
    configurator \
    ${WEBOS_PACKAGESET_ENYO_1} \
    event-monitor \
    filecache \
    gssdp \
    gupnp \
    ${VIRTUAL-RUNTIME_novacomd} \
    ilib-qml-plugin \
    ilib-webapp \
    luna-downloadmgr \
    luna-init \
    luna-sysservice \
    mojoservicelauncher \
    nodejs-module-webos-service \
    notificationmgr \
    nyx-modules \
    pmklogd \
    pmlogctl \
    pmlogdaemon \
    sam \
    settingsservice \
    sleepd \
    webos-nettools \
    webos-connman-adapter \
    webos-fontconfig-files \
    com.webos.service.audiofocusmanager \
    com.webos.service.audiooutput \
    ${VIRTUAL-RUNTIME_bluetooth_service} \
    ${VIRTUAL-RUNTIME_com.webos.service.cec} \
    com.webos.service.hfp \
    com.webos.service.mediaindexer \
    ${VIRTUAL-RUNTIME_initscripts} \
    ${VIRTUAL-RUNTIME_pdm} \
    com.webos.service.peripheralmanager \
    com.webos.service.power2 \
    ${VIRTUAL-RUNTIME_appinstalld} \
    ${VIRTUAL-RUNTIME_event-monitor-network} \
    ${VIRTUAL-RUNTIME_com.webos.app.browser} \
    ${VIRTUAL-RUNTIME_com.webos.app.mediagallery} \
    ${VIRTUAL-RUNTIME_com.webos.app.notification} \
    ${VIRTUAL-RUNTIME_com.webos.app.volume} \
    ${VIRTUAL-RUNTIME_com.webos.service.intent} \
    ${VIRTUAL-RUNTIME_memorymanager} \
    ${VIRTUAL-RUNTIME_mojoservicelauncher} \
    ${VIRTUAL-RUNTIME_tts} \
    ${VIRTUAL-RUNTIME_com.webos.service.location} \
    ${VIRTUAL-RUNTIME_nodejs-module-node-red} \
    ${WEBOS_PACKAGESET_TZDATA} \
    ${WEBOS_FOSS_MISSING_FROM_RDEPENDS} \
    ${WEBOS_PACKAGESET_SYSTEMAPPS} \
    ${WEBOS_PACKAGESET_UI} \
    ${WEBOS_PACKAGESET_TESTAPPS} \
    ${WEBOS_PACKAGESET_ENACTAPPS} \
    ${WEBOS_PACKAGESET_MEDIA} \
"


# XXX These FOSS components must be explicitly added because they are missing
# from the RDEPENDS lists of the components that expect them to be present at
# runtime (or perhaps some are in fact top-level components and some others
# aren't actually needed).
WEBOS_FOSS_MISSING_FROM_RDEPENDS = " \
    bash \
    bzip2 \
    gzip \
    curl \
    e2fsprogs \
    hunspell \
    icu \
    iproute2 \
    lsb-release \
    makedevs \
    ncurses \
    openssl \
    procps \
    psmisc \
    sqlite3 \
"

RDEPENDS:${PN} += "${MACHINE_EXTRA_RDEPENDS}"
RRECOMMENDS:${PN} += "${MACHINE_EXTRA_RRECOMMENDS}"

# Unused meta-webos components:
# - glibcurl
# - libtinyxml
