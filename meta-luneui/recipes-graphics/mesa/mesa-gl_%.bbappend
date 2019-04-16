PACKAGECONFIG_append = " gbm"

# This was added in sumo
PROVIDES += "${@bb.utils.contains('PACKAGECONFIG', 'gbm', 'virtual/libgbm', '', d)}"

# We want to use libhybris for non qemu* MACHINEs
NOT_PROVIDED = "virtual/mesa"
NOT_PROVIDED_qemuall = ""
NOT_PROVIDED_pinephone = ""
PROVIDES_remove = "${NOT_PROVIDED}"

do_install_append() {
    if ${@oe.utils.conditional('PREFERRED_PROVIDER_virtual/egl', 'libhybris', 'true', 'false', d)} ; then
        rm -rf ${D}${includedir}/KHR/khrplatform.h
    fi
}
