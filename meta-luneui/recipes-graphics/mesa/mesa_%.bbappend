# Use gallium and llvmpipe for rendering
PACKAGECONFIG_append = " gallium gallium-llvm"

# We want the fbdev platform as well
# Temporary disable until I find out what to do with mesa-10.6
# http://lists.openembedded.org/pipermail/openembedded-core/2015-August/108949.html
#EGL_PLATFORMS_append = ",fbdev"

# ------------------------------------------------------------------------------
# Move the /usr/lib/libGLESv2.so symlink into the runtime package as workaround
# for issues seem with Qt 5.4.1, which seems to try to dlopen libGLESv2.so using
# a hardcoded filename (instead of relying on the dynamic linker and therefore
# loading libGLESv2.so via it's soname).
# ------------------------------------------------------------------------------
FILES_libgles2-mesa = "${libdir}/libGLESv2.so.* ${libdir}/libGLESv2.so"
FILES_SOLIBSDEV_libgles2-mesa = ""
INSANE_SKIP_libgles2-mesa += "dev-so"
