# Use gallium and llvmpipe for rendering
PACKAGECONFIG_append = " gallium gallium-llvm"

# This breaks llvm from meta-oe as reported in:
# http://lists.openembedded.org/pipermail/openembedded-core/2017-March/134503.html
EXTRA_OECONF_remove = "--with-llvm-prefix=${STAGING_BINDIR_NATIVE}"

PACKAGECONFIG_append = " gbm"

# This was added in sumo
PROVIDES += "${@bb.utils.contains('PACKAGECONFIG', 'gbm', 'virtual/libgbm', '', d)}"

# We want the fbdev platform as well
# Temporary disable until I find out what to do with mesa-10.6
# http://lists.openembedded.org/pipermail/openembedded-core/2015-August/108949.html
#EGL_PLATFORMS_append = ",fbdev"

