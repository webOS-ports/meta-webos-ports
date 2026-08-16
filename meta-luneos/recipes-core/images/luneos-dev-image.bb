require luneos-image.inc

DESCRIPTION = "Developers LuneOS image"

IMAGE_FEATURES += "luneos-development"

webos_enable_devmode() {
    install -d ${IMAGE_ROOTFS}/var/luna
    install -d ${IMAGE_ROOTFS}/var/luna/preferences
    touch ${IMAGE_ROOTFS}/var/luna/dev-mode-enabled
    touch ${IMAGE_ROOTFS}/var/luna/preferences/devmode_enabled
    touch ${IMAGE_ROOTFS}/var/luna/preferences/debug_system_apps
    touch ${IMAGE_ROOTFS}/etc/usb-debugging-enabled
    touch ${IMAGE_ROOTFS}/.writable_image
}

# journald ships Storage=auto, which keeps the journal in RAM unless
# /var/log/journal already exists - so on a device that never reaches a usable
# state, every reboot destroys the record of why. Recovering a boot failure then
# means booting a recovery, mounting the rootfs, creating this directory by hand,
# rebooting back into the failure and only then reading the log - which needs
# working USB on a device whose USB may itself be part of what is broken.
#
# Create it here so a dev image records its own failures the first time round.
# Dev image only: journald bounds itself with SystemMaxUse (10% of the
# filesystem by default), but the writes are still real and a release image
# should not pay for them.
webos_enable_persistent_journal() {
    # /var/log is a symlink to volatile (tmpfs) storage, so creating the journal
    # directory naively lands it in /var/volatile/log, which is wiped on every
    # boot - the precise thing this is meant to prevent. The symlink comes from
    # a systemd-tmpfiles rule in 00-create-volatile.conf:
    #
    #   L  /var/log  -  -  -  -  /var/volatile/log
    #
    # Replace it with a real directory. An "L" line only creates its symlink when
    # nothing exists at the path - forcing it would need "L+" - so a real
    # directory placed here survives that rule at boot.
    #
    # VOLATILE_LOG_DIR used to be the supported knob for this and would have been
    # the obvious thing to set; it is obsolete in this OE ("is obsolete and no
    # longer supported" in bitbake.conf) with no replacement, hence doing it here.
    if [ -L ${IMAGE_ROOTFS}/var/log ]; then
        rm -f ${IMAGE_ROOTFS}/var/log
    fi
    install -d ${IMAGE_ROOTFS}/var/log/journal
}

ROOTFS_POSTPROCESS_COMMAND += "webos_enable_devmode;"
ROOTFS_POSTPROCESS_COMMAND += "webos_enable_persistent_journal;"

MESA_PKGS = " \
    libegl-mesa \
    libgles2-mesa \
    libgbm \
    mesa-megadriver \
    libgles1-mesa \
"

IMAGE_INSTALL:append = " \
    libdrm-tests \
    qtbase-examples \
    qtbase-plugins \
    glmark2 \
    kernel-modules \
"

IMAGE_INSTALL:append:tenderloin = " \
    ${MESA_PKGS} \
    luneos-mainline-debug \
"

IMAGE_INSTALL:append:qemuall = " \
    ${MESA_PKGS} \
"
