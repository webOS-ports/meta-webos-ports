# needed to use gles instead of default gl
PACKAGECONFIG_GL_class-target = "gles2"
# leave default gl for nativesdk, because the default is:
# PACKAGECONFIG_class-nativesdk = "${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)} ${PACKAGECONFIG_GL}"
# but mesa doesn't provide it:
# ERROR: Nothing PROVIDES 'virtual/nativesdk-libgles2' (but virtual:nativesdk:/OE/build/luneos-honister/webos-ports/openembedded-core/meta/recipes-graphics/libsdl2/libsdl2_2.0.14.bb DEPENDS on or otherwise requires it).
