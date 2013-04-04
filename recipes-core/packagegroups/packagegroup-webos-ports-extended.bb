DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = " \
  android-system-compat \
  \
  distro-feed-configs \
  pulseaudio-server \
  pulseaudio-misc \
  wireless-tools \
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
  org.webosinternals.service.update \
  \
  org.webosports.app.firstuse \
  org.webosports.app.memos \
  org.webosports.app.settings \
  org.webosinternals.preware \
  org.webosports.app.calendar \
  \
  ca-certificates \
"

ANDROID_RDEPENDS = "android-audiosystem"
LIBHYBRIS_RDEPENDS = "${VIRTUAL-RUNTIME_android-system-image}"

RDEPENDS_${PN}_append_tuna = " token-generator ${LIBHYBRIS_RDEPENDS} ${ANDROID_RDEPENDS}"
RDEPENDS_${PN}_append_grouper = " token-generator ${LIBHYBRIS_RDEPENDS} ${ANDROID_RDEPENDS}"

RDEPENDS_${PN}_append_arm = " \
  crash-handler \
"
