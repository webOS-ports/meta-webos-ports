PACKAGECONFIG_append = " gbm"

# This was added in sumo
PROVIDES += "${@bb.utils.contains('PACKAGECONFIG', 'gbm', 'virtual/libgbm', '', d)}"

# We want to use libhybris for non qemu* MACHINEs
NOT_PROVIDED = "virtual/mesa"
NOT_PROVIDED_qemuall = ""
PROVIDES_remove = "${NOT_PROVIDED}"
