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

# mesa 26 moved EGL_WL_bind_wayland_display behind an opt-in build flag which
# defaults to OFF - up to mesa 24 it was always built:
#
#   option('legacy-wayland', type : 'array', value : [],
#          choices : ['bind-wayland-display'])
#
#   with_wayland_bind_display = with_platform_wayland and
#       get_option('legacy-wayland').contains('bind-wayland-display')
#
# Without it libEGL is linked with no wl_drm at all, and surface-manager's Qt
# wayland-egl hardware integration refuses to start:
#
#   Failed to initialize EGL display. There is no EGL_WL_bind_wayland_display
#   extension.
#   libEGL warning: failed to get driver name for fd -1
#   libEGL warning: MESA-LOADER: failed to retrieve device information
#   libEGL warning: egl: failed to create dri2 screen
#
# Clients are then handed fd -1, cannot reach the GPU, and fall back to
# software rasterisation. Because we build with -Dllvm=disabled that fallback
# is softpipe rather than llvmpipe - the slowest path there is - which is why
# the UI crawls on both the PinePhone Pro and the PineTab2 even though
# panfrost itself is up and the compositor holds a GPU context.
#
# The alternative would be moving the compositor to the linux-dmabuf-v1
# hardware integration (the plugin ships in the image), but that changes the
# LuneOS graphics path on every device; re-enabling the flag restores exactly
# the behaviour we had on mesa 24.
EXTRA_OEMESON:append:class-target = " -Dlegacy-wayland=bind-wayland-display"
