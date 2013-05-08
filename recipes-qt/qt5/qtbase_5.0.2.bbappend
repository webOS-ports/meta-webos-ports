GL_DEPENDS = "virtual/libgles2 virtual/egl"
QT_GLFLAGS = "-opengl es2 -eglfs"

# Make our webos QPA plugin the default one
QT_CONFIG_FLAGS += "-qpa webos"

# Runtime dependency will be pulled by packagegroup because we don't want checksums to depend
# on qtbase.do_package -> qt5-webos-plugin -> qtwebkit -> qtbase
# RDEPENDS_${PN} += "qt5-webos-plugin"
