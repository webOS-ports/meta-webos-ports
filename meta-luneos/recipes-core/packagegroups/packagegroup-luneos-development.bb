DESCRIPTION = "Development components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# gdb, gdbserver and valgrind are in packagegroup-luneos-development-debug.

RDEPENDS:${PN} = " \
  mingetty \
  serial-forward \
  powerstat \
  screen \
  evtest \
  strace \
  opkg-utils \
  bc \
  smemstat \
  binutils \
  \
  alsa-utils-alsamixer \
  alsa-utils-alsactl \
  alsa-utils-alsaucm \
  alsa-utils-aplay \
  alsa-utils-amixer \
  \
  org.webosports.app.testr \
  nyx-utils \
  \
  systemd-analyze \
  sdl2-opengles-test \
  org.webosports.app.settings-qml \
  \
  connman-client \
  qtdeclarative-tools \
  ${VIRTUAL-RUNTIME_ofono-tests} \
"

