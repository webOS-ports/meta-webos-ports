# Copyright (c) 2012-2013 LG Electronics, Inc.

DESCRIPTION = "meta-webos components used in Open webOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# You don't need to change this value when you're changing just RDEPENDS_${PN} variable.

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

#VIRTUAL-RUNTIME_webappmanager ?= "luna-webappmanager"
VIRTUAL-RUNTIME_webappmanager ?= "wam"
VIRTUAL-RUNTIME_initscripts ?= "initscripts"
VIRTUAL-RUNTIME_librdx ?= "rdxd"
VIRTUAL-RUNTIME_webos-compositor ?= "luna-next"
VIRTUAL-RUNTIME_webos-ime ?= "webos-keyboard"
VIRTUAL-RUNTIME_novacomd ?= "novacomd"

# We're not using VIRTUAL-RUNTIME because VIRTUAL-RUNTIME is usually used for only
# one item and changing that in <distro>-preferred-providers.inc would require
# .bbappend in meta-<distro> to do PR/PRINC/PR_append bump anyway so it's easier
# to change this variable in .bbappend together with bump.
#

WEBOS_PACKAGESET_TESTAPPS = " \
    bareapp \
    com.webos.app.test.enact \
    com.webos.app.test.webosose \
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
    luna-applauncher \
    luna-systemui \
    luna-universalsearchmgr \
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
    ${VIRTUAL-RUNTIME_webos-compositor} \
    ${VIRTUAL-RUNTIME_webos-ime} \
"

# nyx-lib needs nyx-modules at runtime, but a runtime dependency is not defined
# in its recipe because nyx-modules is MACHINE_ARCH (e.g. qemux86), while nyx-lib is
# TUNE_PKGARCH  (e.g. i586). Instead, it is pulled into the image by adding it here.
# (There are more details as to why this was done in nyx-lib.bb.)

RDEPENDS_${PN} = " \
    activitymanager \
    bootd \
    configurator \
    ${WEBOS_PACKAGESET_ENYO_1} \
    event-monitor \
    event-monitor-network \
    filecache \
    ${VIRTUAL-RUNTIME_librdx} \
    ${VIRTUAL-RUNTIME_novacomd} \
    luna-downloadmgr \
    luna-init \
    luna-sysservice \
    mojoservicelauncher \
    nodejs-module-webos-service \
    nyx-modules \
    pmklogd \
    pmlogctl \
    pmlogdaemon \
    settingsservice \
    sleepd \
    webos-connman-adapter \
    com.webos.service.pdm \
    ${VIRTUAL-RUNTIME_initscripts} \
    ${WEBOS_PACKAGESET_TZDATA} \
    ${WEBOS_FOSS_MISSING_FROM_RDEPENDS} \
    ${WEBOS_PACKAGESET_SYSTEMAPPS} \
    ${WEBOS_PACKAGESET_UI} \
    ${WEBOS_PACKAGESET_TESTAPPS} \
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

# These packages that are installed in the qemux86 image only.
RDEPENDS_${PN}_append_qemuall = " \
    dhcp-client \
"

# Unused meta-webos components:
# - glibcurl
# - libtinyxml
