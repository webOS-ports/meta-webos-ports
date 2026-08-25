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
# NOTE: this file must stay named mesa.bb, not mesa_<version>.bb. oe-core dropped
# the version from its own recipe in wrynose (mesa_24.0.7.bb -> mesa.bb) and every
# layer renamed its append to mesa.bbappend to match. A versioned recipe here is
# still built -- this layer outranks oe-core -- but mesa.bbappend then matches
# oe-core's mesa.bb instead of it, so meta-luneui's PACKAGECONFIG (gallium-llvm,
# svga, virtio, freedreno) and meta-rockchip's and meta-rpi-luneos's appends all
# silently stop applying. The emulator then builds with softpipe only and
# surface-manager dies in initializeHardwareIntegration() with
# "vmwgfx: driver missing", because the svga gallium driver was never compiled in.
#
# meta-smartphone/meta-mainline's mesa.bbappend, which carries the freedreno A22X
# patch series for tenderloin (HP TouchPad), has to keep the same name as this
# file or those patches silently stop applying too.
SRCREV = "9f0a761020bca92f2b07156a0621e5360cb8eca5"

SRC_URI = "git://gitlab.freedesktop.org/mesa/mesa.git;protocol=https;branch=26.2 \
           file://0001-gallivm-check-ExecutionEngine-create-for-NULL-before-.patch \
           file://0002-gallivm-handle-a-failed-execution-engine-instead-of-a.patch \
"

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
