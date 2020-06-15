# Use gallium for rendering 
PACKAGECONFIG_append_class-target = " gallium"
# Use gallium and llvmpipe for rendering in qemu
PACKAGECONFIG_append_class-target_qemuall = " gallium-llvm"

PACKAGECONFIG_append_class-target = " gbm"
