# Use gallium for rendering 
PACKAGECONFIG:append:class-target = " gallium"
# Use gallium and llvmpipe for rendering in qemu
PACKAGECONFIG:append:class-target:qemuall = " gallium-llvm"

PACKAGECONFIG:append:class-target = " gbm"

# Enable freedreno driver
PACKAGECONFIG:append:hammerhead = " kmsro freedreno"
GALLIUMDRIVERS:append:hammerhead = ",freedreno"

PACKAGECONFIG:append:rosy = " kmsro freedreno"
GALLIUMDRIVERS:append:rosy = ",freedreno"

PACKAGECONFIG:append:tenderloin = " kmsro freedreno"
GALLIUMDRIVERS:append:tenderloin = ",freedreno"
