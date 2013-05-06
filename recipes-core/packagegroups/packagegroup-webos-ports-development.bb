DESCRIPTION = "Development components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r5"

inherit packagegroup

RDEPENDS_${PN} = " \
  mingetty \
  serial-forward \
  mdbus2 \
  gdb \
  powerstat \
  screen \
  evtest \
  strace \
  opkg-utils \
  \
  alsa-utils-alsamixer \
  alsa-utils-alsactl \
  alsa-utils-alsaucm \
  alsa-utils-aplay \
  alsa-utils-amixer \
  \
  gst-meta-debug \
"
