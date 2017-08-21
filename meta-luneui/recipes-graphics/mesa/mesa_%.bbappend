# Use gallium and llvmpipe for rendering
PACKAGECONFIG_append = " gallium gallium-llvm"

PACKAGECONFIG_append = " gbm"

# This was added in sumo
PROVIDES += "${@bb.utils.contains('PACKAGECONFIG', 'gbm', 'virtual/libgbm', '', d)}"
