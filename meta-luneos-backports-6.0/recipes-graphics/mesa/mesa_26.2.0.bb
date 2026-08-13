require mesa.inc

# Pinned to the released mesa-26.2.0 tag rather than a main-branch snapshot, so the
# version in PV corresponds to what actually gets built.
#
# branch=26.2, not main: mesa cuts a release branch and tags the release on it, so
# the mesa-26.2.0 commit is not an ancestor of main and bitbake's "revision in
# branch" check rejects it.
#
# SRCREV is the commit the mesa-26.2.0 tag points at, not the annotated tag object
# (86119401...), which bitbake cannot check out.
#
# Do not pin back to 26.1.x on buildhistory evidence alone. A black screen on the
# qemux86-64 emulator appliance was blamed on this bump once because a buildhistory
# diff showed mesa as the only graphics change; that was a misattribution. The boot
# logo rendered here too, so scanout was never broken. The real fault was
# libqofono's QML plugin being installed off the import path by the qmake->CMake
# migration, and fixing that brought the UI back with mesa unchanged.
#
# NOTE: meta-smartphone/meta-mainline carries mesa_<version>.bbappend with the
# freedreno A22X patch series for tenderloin (HP TouchPad). It is version-pinned,
# so it has to be renamed in lockstep with this file or those 15 patches silently
# stop applying.
SRCREV = "9f0a761020bca92f2b07156a0621e5360cb8eca5"

SRC_URI = "git://gitlab.freedesktop.org/mesa/mesa.git;protocol=https;branch=26.2"

PV = "26.2.0+git"

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
