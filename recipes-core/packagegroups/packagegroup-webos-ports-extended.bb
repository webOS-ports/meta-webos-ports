DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = " \
  distro-feed-configs \
  pulseaudio-server \
  pulseaudio-misc \
  wireless-tools \
  udev-extraconf \
  alsa-conf \
  \
  gst-meta-base \
  gst-meta-audio \
  gst-meta-video \
  gst-plugins-good-pulse \
  gst-plugins-ugly-mad \
  gst-plugins-ugly-lame \
  \
  webos-connman-adapter \
  ${@base_contains('MACHINE_FEATURES', 'phone', 'packagegroup-webos-telephony', '',d)} \
  ${@base_contains('MACHINE_FEATURES', 'bluetooth', 'bluez4', '',d)} \
  \
  org.webosports.service.licenses \
  org.webosinternals.service.upstartmgr \
  org.webosinternals.ipkgservice \
  org.webosports.service.update \
  \
  org.webosports.app.firstuse \
  org.webosports.app.memos \
  org.webosports.app.settings \
  org.webosinternals.preware \
  org.webosports.app.calendar \
  org.webosinternals.tweaks \
  org.webosports.app.update \
  snowshoe \
  web-apps \
  \
  ca-certificates \
  \
  qtwayland \
  qtwayland-plugins \
  qtbase-plugins \
  qtbase-fonts \
  qtbase-fonts-ttf-dejavu \
  qtbase-fonts-ttf-vera \
  \
  luna-next-cardshell \
  luna-sysmgr \
  luna-sysmgr-conf \
  \
  webos-systemd-services \
"

LIBHYBRIS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_android-system-image} \
    android-system \
    android-system-compat \
    android-tools \
"

RDEPENDS_${PN}_append_tuna = " token-generator ${LIBHYBRIS_RDEPENDS}"
RDEPENDS_${PN}_append_grouper = " token-generator ${LIBHYBRIS_RDEPENDS}"

MESA_RDEPENDS = "libegl-gallium mesa-driver-swrast"

RDEPENDS_${PN}_append_qemux86 = " ${MESA_RDEPENDS} qt5-plugin-generic-vboxtouch"
RDEPENDS_${PN}_append_qemux86-64 = " ${MESA_RDEPENDS} qt5-plugin-generic-vboxtouch"

RDEPENDS_${PN}_append_arm = " \
  crash-handler \
"
