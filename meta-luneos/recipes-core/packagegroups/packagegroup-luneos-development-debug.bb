DESCRIPTION = "Debugging tools and test programs for LuneOS developers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Not in luneos-dev-image by default: valgrind's tools are non-PIE and
# gdbserver opens a debug port, and the Qt examples and libdrm tests are 137
# and more test executables nobody uses day to day. Add them to an image with
#   EXTRA_IMAGE_FEATURES += "luneos-development-debug"
# in local.conf, or install the packagegroup with opkg.

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

VALGRIND = "valgrind"
# only armv7a is supported
VALGRIND:arm = ""
VALGRIND:armv7a = "valgrind"

RDEPENDS:${PN} = " \
  gdb \
  gdbserver \
  ${VALGRIND} \
  qtbase-examples \
  libdrm-tests \
"
