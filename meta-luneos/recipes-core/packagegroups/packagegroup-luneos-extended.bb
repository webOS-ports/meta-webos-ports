DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

NOT_COMPATIBLE_WITH_CURRENT_NODEJS = " \
  node-sqlite3 \
"


RDEPENDS_${PN} = " \
  distro-feed-configs \
  \
  pulseaudio-distro-conf \
  pulseaudio-misc \
  pulseaudio-module-switch-on-connect \
  pulseaudio-module-bluetooth-discover \ 
  pulseaudio-module-bluetooth-policy \
  pulseaudio-module-bluez4-device \
  pulseaudio-module-bluez4-discover \
  pulseaudio-server \
  \
  alsa-conf \
  tar \
  udev-extraconf \
  webos-connman-adapter \
  wireless-tools \
  \
  ${@base_contains('MACHINE_FEATURES', 'phone', 'packagegroup-webos-telephony', '', d)} \
  ${@base_contains('MACHINE_FEATURES', 'cellular', 'packagegroup-webos-telephony', '', d)} \
  ${@base_contains('MACHINE_FEATURES', 'bluetooth', 'bluez4', '',d)} \
  \
  location-service \
  imaccountvalidator \
  imlibpurpleservice \
  \
  org.webosinternals.preware \
  org.webosinternals.tweaks \
  org.webosinternals.ipkgservice \
  \
  org.webosports.app.browser \
  org.webosports.app.calculator \
  org.webosports.app.contacts \
  org.webosports.app.filemanager \
  org.webosports.app.firstuse \
  org.webosports.app.maps \
  org.webosports.app.memos \
  org.webosports.app.messaging \
  org.webosports.app.pdf \
  org.webosports.app.photos \
  org.webosports.app.settings \
  \
  org.webosports.cdav \
  \
  org.webosports.service.devmode \
  org.webosports.service.licenses \
  org.webosports.service.lumberjack \
  org.webosports.service.messaging \
  org.webosports.service.update \
  \
  fingerterm \
  \
  ca-certificates \
  certmgrd \
  pmcertificatemgr \
  \
  qtbase-plugins \
  qtmultimedia-plugins \
  qtmultimedia-qmlplugins \
  qtsensors \
  qtsensors-qmlplugins \
  qtwayland \
  qtwayland-plugins \
  \
  luna-appmanager \
  luna-next-cardshell \
  luna-qml-launcher \
  luna-sysmgr \
  luna-sysmgr-conf \
  luneos-components \
  \
  webos-system-update \
  \
  webos-systemd-services \
  \
  audio-service \
  keymanager \
  downloadmanager \
  mediaindexer \
  media-permission-service \
  webos-systemsounds \
  \
  gstreamer1.0-libav \
  gstreamer1.0-plugins-bad-meta \
  gstreamer1.0-plugins-base-meta \
  gstreamer1.0-plugins-good-meta \
  gstreamer1.0-plugins-ugly-meta \
  \
  luneos-default-wallpapers \
"

LIBHYBRIS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_android-system-image} \
    android-property-service \
    android-system \
    android-system-compat \
    android-tools \
    mtp-server \
    nyx-modules-hybris \
    pulseaudio-modules-droid \
    qt5-qpa-hwcomposer-plugin \
    qtscenegraph-adaptation \
    qtsensors-sensorfw-plugin \
    sensorfw \
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
    alsa-utils-systemd \
    libegl-gallium \
    mesa-driver-swrast \
    phonesim \
    qt5-plugin-generic-vboxtouch \
"

RDEPENDS_${PN}_append_qemux86 = " ${QEMU_RDEPENDS}"
RDEPENDS_${PN}_append_qemux86-64 = " ${QEMU_RDEPENDS}"

RDEPENDS_${PN}_append_arm = " \
  crash-handler \
"
