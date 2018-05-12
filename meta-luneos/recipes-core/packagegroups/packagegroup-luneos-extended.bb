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
  pulseaudio-module-bluez5-device \
  pulseaudio-module-bluez5-discover \
  pulseaudio-server \
  \
  alsa-conf \
  ofono \
  tar \
  udev-extraconf \
  voicecall \
  webos-connman-adapter \
  webos-telephonyd \
  iw \
  \
  bluez5 \
  \
  location-service \
  imaccountvalidator \
  imlibpurpleservice \
  messaging-accounts \
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
  org.webosports.app.phone \
  org.webosports.app.photos \
  org.webosports.app.settings \
  org.webosports.app.tasks \
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
  qtconnectivity \
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
  \
  https-everywhere \
  storaged \
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
    \
    exiv2 \
    libpulse-simple0 \
    qtubuntu-camera \
    libqtubuntu-media-signals2 \
    qtvideo-node \
    org.webosports.app.camera \
"

MEMNOTIFY_RDEPENDS = " \
    memnotify-module \
"
# NOTE: For kernels build outside of OE we can't add memnotify support and therefor have to ship the module with the kernel package directly.
# NOTE: For kernels build with OE, the kernel tree would need to have a patch applied in order to make si_swapinfo exportable as per http://lkml.iu.edu/hypermail//linux/kernel/1201.2/00719.html

ANBOX_RDEPENDS = " \
    anbox \
    kernel-module-squashfs \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
"
# Note: Anbox requires kernel >= 3.18 !

RDEPENDS_${PN}_append_tuna = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_grouper = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_mako = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_hammerhead = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_tenderloin = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_mido = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_athene = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_onyx = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_tissot = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS_${PN}_append_rosy = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"

QEMU_RDEPENDS = " \
    alsa-utils-systemd \
    mesa-driver-swrast \
    phonesim \
    qt5-plugin-generic-vboxtouch \
    kernel-module-snd-intel8x0 \
    libpci \
"

RDEPENDS_${PN}_append_qemux86 = " ${QEMU_RDEPENDS}"
RDEPENDS_${PN}_append_qemux86-64 = " ${QEMU_RDEPENDS} ${ANBOX_RDEPENDS}"

RDEPENDS_${PN}_append_arm = " \
    crash-handler \
"
