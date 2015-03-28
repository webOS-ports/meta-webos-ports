DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS_${PN} = " \
  distro-feed-configs \
  \
  pulseaudio-module-switch-on-connect \
  pulseaudio-server \
  pulseaudio-misc \
  pulseaudio-distro-conf \
  \
  wireless-tools \
  udev-extraconf \
  alsa-conf \
  tar \
  \
  \
  webos-connman-adapter \
  ${@base_contains('MACHINE_FEATURES', 'phone', 'packagegroup-webos-telephony', '', d)} \
  ${@base_contains('MACHINE_FEATURES', 'cellular', 'packagegroup-webos-telephony', '', d)} \
  ${@base_contains('MACHINE_FEATURES', 'bluetooth', 'bluez4', '',d)} \
  \
  org.webosports.service.licenses \
  org.webosinternals.ipkgservice \
  org.webosports.service.update \
  org.webosports.cdav \
  keymanager \
  org.webosports.service.messaging \
  location-service \
  imaccountvalidator \
  imlibpurpleservice \
  \
  org.webosports.app.firstuse \
  org.webosports.app.memos \
  org.webosports.app.settings \
  org.webosinternals.preware \
  org.webosports.app.calendar \
  org.webosinternals.tweaks \
  org.webosports.service.devmode \
  org.webosports.app.browser \
  org.webosports.app.calculator \
  org.webosports.app.pdf \
  org.webosports.app.filemanager \
  org.webosports.app.contacts \
  org.webosports.app.messaging \
  fingerterm \
  \
  ca-certificates \
  pmcertificatemgr \
  certmgrd \
  \
  qtwayland \
  qtwayland-plugins \
  qtbase-plugins \
  qtbase-fonts \
  qtbase-fonts-ttf-dejavu \
  qtbase-fonts-ttf-vera \
  qtmultimedia-plugins \
  qtmultimedia-qmlplugins \
  qtsensors \
  qtsensors-qmlplugins \
  \
  luna-next-cardshell \
  luna-sysmgr \
  luna-sysmgr-conf \
  luna-appmanager \
  luna-qml-launcher \
  luneos-components \
  \
  webos-system-update \
  \
  webos-systemd-services \
  \
  audio-service \
  webos-systemsounds \
  mediaindexer \
  media-permission-service \
  \
  gstreamer1.0-plugins-base-meta \
  gstreamer1.0-plugins-good-meta \
  gstreamer1.0-plugins-bad-meta \
  gstreamer1.0-plugins-ugly-meta \
  gstreamer1.0-libav \
  \
  node-sqlite3 \
  node-dbus \
  node-taglib \
  \
  luneos-default-wallpapers \
"

LIBHYBRIS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_android-system-image} \
    android-system \
    android-system-compat \
    android-tools \
    nyx-modules-hybris \
    mtp-server \
    android-property-service \
    qt5-qpa-hwcomposer-plugin \
    qtscenegraph-adaptation \
    pulseaudio-modules-droid \
    sensorfw \
    qtsensors-sensorfw-plugin \
"

MEMNOTIFY_RDEPENDS = " \
    memnotify-module \
"

RDEPENDS_${PN}_append_tuna = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_grouper = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_mako = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
# NOTE: we can't add memnotify support for tenderloin yet here as we build it's
# kernel outside of OE and therefor have to ship the module with the kernel
# package directly.
RDEPENDS_${PN}_append_tenderloin = " ${LIBHYBRIS_RDEPENDS}"

QEMU_RDEPENDS = " \
    libegl-gallium \
    mesa-driver-swrast \
    qt5-plugin-generic-vboxtouch \
    alsa-utils-systemd \
    phonesim \
"

RDEPENDS_${PN}_append_qemux86 = " ${QEMU_RDEPENDS}"
RDEPENDS_${PN}_append_qemux86-64 = " ${QEMU_RDEPENDS}"

RDEPENDS_${PN}_append_arm = " \
  crash-handler \
"
