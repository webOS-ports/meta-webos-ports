# Use gallium and llvmpipe for rendering
PACKAGECONFIG_append_class-target = " gallium gallium-llvm"

PACKAGECONFIG_append_class-target = " gbm"

# Enable freedreno driver
GALLIUMDRIVERS_hammerhead = "freedreno"
