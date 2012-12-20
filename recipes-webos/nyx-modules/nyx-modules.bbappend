FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 6}"

# For these devices we specify here a different machine implementation than for
# everything else to enable special modules to build.
WEBOS_TARGET_MACHINE_IMPL_tuna = "device"
WEBOS_TARGET_MACHINE_IMPL_grouper = "device"
WEBOS_TARGET_MACHINE_IMPL_a500 = "device"

inherit webos-ports-submissions

DEPENDS += "mtdev"

SRCREV = "33a9dff92127102d0073feb29b642550ab51ef0b"

#  a500.cmake specifies which modules to build, with their config
SRC_URI_append_a500 = " \
                        file://a500.cmake \
"

do_configure_prepend_a500() {
  cp "${WORKDIR}/a500.cmake" "${WORKDIR}/git/src/machine/"
}
