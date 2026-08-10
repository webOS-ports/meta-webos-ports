require mesa.inc

# Use git for development version (26.1.0-devel)
SRCREV = "e57fca6de22ccbc758384f4bb8c1be998a5dd825"

SRC_URI = "git://gitlab.freedesktop.org/mesa/mesa.git;protocol=https;branch=main"

S = "${WORKDIR}/git"

PV = "26.1.0+git"

# Default packageconfig for this version
PACKAGECONFIG = " \
    expat \
    gallium \
    video-codecs \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'opengl egl gles gbm', '', d)} \
    xmlconfig \
    zlib \
"
