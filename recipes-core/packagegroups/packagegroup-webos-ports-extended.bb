DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r16"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = " \
  distro-feed-configs \
  pulseaudio-server \
  wireless-tools \
  bluez4 \
  \
  webos-connman-adapter \
  ${@base_contains('MACHINE_FEATURES', 'phone', 'packagegroup-webos-telephony', '',d)} \
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

RDEPENDS_${PN}_append_tuna = " token-generator"
RDEPENDS_${PN}_append_grouper = " token-generator"

RDEPENDS_${PN}_append_arm = " \
  crash-handler \
"
