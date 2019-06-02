# imported from oe-core as-is in:
# 116330fb1a mesa: Update 19.0.3 -> 19.0.5

require mesa_${PV}.bb

SUMMARY += " (OpenGL only, no EGL/GLES)"

PROVIDES = "virtual/libgl virtual/mesa"

S = "${WORKDIR}/mesa-${PV}"

PACKAGECONFIG ??= "opengl dri ${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"
PACKAGECONFIG_class-target = "opengl dri ${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"
