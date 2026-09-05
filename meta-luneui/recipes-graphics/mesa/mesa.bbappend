# Use gallium for rendering 
PACKAGECONFIG:append:class-target = " gallium"
# Use gallium and llvmpipe for rendering in qemu
PACKAGECONFIG:append:class-target:qemuall = " gallium-llvm"

# The emulator runs on virtual GPUs: VMSVGA/vmwgfx under VirtualBox and VMware
# (gallium "svga"), virtio-gpu under qemu (gallium "virgl"). oe-core's mesa
# pulled both in implicitly on x86-64 once gallium-llvm was set, but
# meta-mainline's mesa 26.x requires them to be requested explicitly, so they
# have to be listed here or the emulator ends up with freedreno/lima/llvmpipe
# only and surface-manager crashes on a failed EGL init.
PACKAGECONFIG:append:class-target:qemuall = " svga virtio"

PACKAGECONFIG:append:class-target = " gbm"

# Enable freedreno driver
PACKAGECONFIG:append = " freedreno"
GALLIUMDRIVERS:append = ",freedreno"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
           file://0001-gallivm-check-ExecutionEngine-create-for-NULL-before-.patch \
           file://0002-gallivm-handle-a-failed-execution-engine-instead-of-a.patch \
"
