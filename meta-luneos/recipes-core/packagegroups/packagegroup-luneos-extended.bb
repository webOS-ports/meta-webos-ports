DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

NOT_COMPATIBLE_WITH_CURRENT_NODEJS = " \
  node-sqlite3 \
"


RDEPENDS:${PN} = " \
  ${DISTRO_EXTRA_RDEPENDS} \
  distro-feed-configs \
  \
  pulseaudio-distro-conf \
  pulseaudio-misc \
  pulseaudio-module-loopback \
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
  org.webosports.app.preware \
  org.webosports.service.ipkg \
  \
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
  org.webosports.tweaks \
  \
  org.webosports.service.devmode \
  org.webosports.service.licenses \
  org.webosports.service.lumberjack \
  org.webosports.service.messaging \
  org.webosports.service.update \
  \
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
  qtsensors-sensorfw-plugin \
  \
  sensorfw \
  \
  luna-appmanager \
  luna-next-cardshell \
  luna-sysmgr \
  luna-sysmgr-conf \
  luneos-components \
  qtlocation-luneos-plugin \
  webos-system-update \
  \
  webos-systemd-services \
  \
  audio-service \
  com.palm.keymanager \
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
  messwerk \
  fingerterm \
"

#Needs work for Qt6
#  org.webosports.app.terminal
#  org.webosports.app.camera

LIBHYBRIS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_android-system-image} \
    android-property-service \
    android-system \
    android-system-compat \
    android-tools \
    android-tools-adbd \
    lxc \
    mtp-server \
    pulseaudio-modules-droid \
    pulseaudio-modules-droid-hidl \
    qt6-qpa-hwcomposer-plugin \
    \
    exiv2 \
    libpulse-simple0 \
    nyx-modules-hybris \
    \
    ofono-ril-binder-plugin \
"

# (Optional?) work for Qt6:
#     qtscenegraph-adaptation 

MEMNOTIFY_RDEPENDS = " \
    memnotify-module \
"

#Needs update for Qt6
#    qtubuntu-camera 
#    libqtubuntu-media-signals2 
#    qtvideo-node 


# NOTE: For kernels build outside of OE we can't add memnotify support and therefor have to ship the module with the kernel package directly.
# NOTE: For kernels build with OE, the kernel tree would need to have a patch applied in order to make si_swapinfo exportable as per http://lkml.iu.edu/hypermail//linux/kernel/1201.2/00719.html

RDEPENDS:${PN}:append:tuna = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS:${PN}:append:grouper = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS:${PN}:append:mako = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS} bluebinder"
RDEPENDS:${PN}:append:hammerhead = " alsa-utils-systemd mesa-driver-swrast rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:hammerhead-halium = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS:${PN}:append:tenderloin = " alsa-utils-systemd mesa-driver-swrast rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:tenderloin-halium = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS:${PN}:append:mido = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS} bluebinder"
RDEPENDS:${PN}:append:athene = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS:${PN}:append:onyx = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"
RDEPENDS:${PN}:append:oxygen = " ${LIBHYBRIS_RDEPENDS}"
RDEPENDS:${PN}:append:tissot = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS} bluebinder"
RDEPENDS:${PN}:append:sargo = " ${LIBHYBRIS_RDEPENDS} bluebinder"
RDEPENDS:${PN}:append:rosy = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS} bluebinder"
RDEPENDS:${PN}:append:s2 = " ${LIBHYBRIS_RDEPENDS}"
RDEPENDS:${PN}:append:sagit = " ${LIBHYBRIS_RDEPENDS}"
RDEPENDS:${PN}:append:yggdrasil = " ${LIBHYBRIS_RDEPENDS} ${MEMNOTIFY_RDEPENDS}"

#RDEPENDS:${PN}:append:pinephone = "waydroid"
#RDEPENDS:${PN}:append:pinephonepro = "waydroid"

QEMU_RDEPENDS = " \
    alsa-utils-systemd \
    mesa-driver-swrast \
    kernel-module-snd-intel8x0 \
    libpci \
    phonesim \
    qt-plugin-generic-vboxtouch \
    rng-tools \
    vmwgfx-layout \
"

RDEPENDS:${PN}:append:qemux86 = " ${QEMU_RDEPENDS}"
RDEPENDS:${PN}:append:qemux86-64 = " ${QEMU_RDEPENDS}"

RDEPENDS:${PN}:append:arm = " \
    crash-handler \
"
