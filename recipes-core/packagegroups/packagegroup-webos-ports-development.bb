DESCRIPTION = "Development components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit packagegroup

VALGRIND = "valgrind"
# only armv7a is supported
VALGRIND_arm = ""
VALGRIND_armv7a = "valgrind"

RDEPENDS_${PN} = " \
  mingetty \
  serial-forward \
  mdbus2 \
  gdb \
  gdbserver \
  powerstat \
  screen \
  evtest \
  strace \
  opkg-utils \
  bc \
  ${VALGRIND} \
  \
  alsa-utils-alsamixer \
  alsa-utils-alsactl \
  alsa-utils-alsaucm \
  alsa-utils-aplay \
  alsa-utils-amixer \
  \
  org.webosports.app.testr \
  \
  systemd-analyze \
  qt5-opengles2-test \
  qtwayland-examples \
  \
  connman-client \
"
