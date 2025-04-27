# Use gallium for rendering 
PACKAGECONFIG:append:class-target = " gallium"
# Use gallium and llvmpipe for rendering in qemu
PACKAGECONFIG:append:class-target:qemuall = " gallium-llvm"

PACKAGECONFIG:append:class-target = " gbm"

# Enable freedreno driver
PACKAGECONFIG:append = " freedreno"
GALLIUMDRIVERS:append = ",freedreno"
