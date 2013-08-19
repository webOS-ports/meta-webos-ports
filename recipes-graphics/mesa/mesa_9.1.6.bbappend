# Use gallium and llvmpipe for rendering
PACKAGECONFIG_append = " gallium gallium-egl gallium-gbm gallium-llvm"

# We want the fbdev platform as well
EGL_PLATFORMS_append = ",fbdev"
