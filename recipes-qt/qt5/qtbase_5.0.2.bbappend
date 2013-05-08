GL_DEPENDS = "virtual/libgles2 virtual/egl"
QT_GLFLAGS = "-opengl es2 -eglfs"

# Make our webos QPA plugin the default one
QT_CONFIG_FLAGS += "-qpa webos"

RDEPENDS_${PN} += "qt5-webos-plugin"
