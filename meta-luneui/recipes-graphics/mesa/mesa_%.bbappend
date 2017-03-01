# Use gallium and llvmpipe for rendering
PACKAGECONFIG_append = " gallium gallium-llvm"

# We want the fbdev platform as well
# Temporary disable until I find out what to do with mesa-10.6
# http://lists.openembedded.org/pipermail/openembedded-core/2015-August/108949.html
#EGL_PLATFORMS_append = ",fbdev"

