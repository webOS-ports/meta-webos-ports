DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r10"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = " \
  distro-feed-configs \
  pulseaudio-server \
  wireless-tools \
  connman \
  connman-adapter \
  bluez4 \
  \
  org.webosports.service.licenses \
  org.webosinternals.service.upstartmgr \
  org.webosinternals.ipkgservice \
  \
  org.webosports.app.firstuse \
  org.webosports.app.memos \
"

RDEPENDS_${PN}_append_tuna = " \
  token-generator \
"

RDEPENDS_${PN}_append_arm = " \
  crash-handler \
"
