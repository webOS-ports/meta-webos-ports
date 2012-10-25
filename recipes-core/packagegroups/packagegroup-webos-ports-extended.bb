DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r3"

inherit packagegroup

RDEPENDS_${PN} = " \
  opkg \
  distro-feed-configs \
  pulseaudio-server \
  wireless-tools \
  connman \
  connman-adapter \
  bluez4 \
"
RRECOMMENDS_${PN} = " \
  token-generator \
"
