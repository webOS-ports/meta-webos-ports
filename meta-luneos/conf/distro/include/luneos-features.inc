# Copyright (c) 2012-2013 LG Electronics, Inc.

# Keep in sync with setting in oe-core/meta/conf/distro/include/default-distrovars.inc
# TODO: Maybe change this to use "_remove" when it's introduced in bitbake

# Split some features to separate variables
# Remove argp (not used in OE anymore) and x11 (not needed for Open webOS)
WEBOS_DISTRO_FEATURES_DEFAULT = "\
  alsa ext2 irda largefile xattr \
"
WEBOS_DISTRO_FEATURES_OPENGL = "opengl"
# Remove x11
WEBOS_DISTRO_FEATURES_WINDOW_SYSTEM = "wayland"
WEBOS_DISTRO_FEATURES_BUSES = "pci"
WEBOS_DISTRO_FEATURES_USB = "usbhost usbgadget"
WEBOS_DISTRO_FEATURES_CONNECTIVITY = "bluetooth 3g wifi bluez5 nfc ipv6"
WEBOS_DISTRO_FEATURES_NETWORKING = "nfs zeroconf"
WEBOS_DISTRO_FEATURES_SECURITY = "seccomp"
WEBOS_DISTRO_FEATURES_EXTENDED = "webos-touch"

WEBOS_DISTRO_FEATURES_LD = "ld-is-lld"

WEBOS_DISTRO_FEATURES_INIT = "systemd usrmerge"

# wrynose replaced the backfill mechanism: DISTRO_FEATURES_BACKFILL and
# DISTRO_FEATURES_BACKFILL_CONSIDERED are gone, and DISTRO_FEATURES_DEFAULTS is
# now added on top of an explicit DISTRO_FEATURES assignment like the one below.
# Opting out is done with DISTRO_FEATURES_OPTED_OUT.
#
# scarthgap backfilled "pulseaudio sysvinit gobject-introspection-data ldconfig"
# and LuneOS opted out of sysvinit only, so pulseaudio, gobject-introspection-data
# and ldconfig were already in effect and are deliberately NOT opted out here.
#
# wrynose's DISTRO_FEATURES_DEFAULTS additionally brings in acl, debuginfod,
# ipv4, x11, vfat, ptest, multiarch and vulkan, none of which LuneOS had. Of
# those we refuse:
#   x11        - Open webOS is wayland-only; this file has removed x11 since 2012
#   ptest      - builds the test suites of every recipe that has them
#   multiarch  - unused, and it perturbs the sysroot layout
#   vulkan     - no LuneOS target has a Vulkan driver (a2xx/a3xx freedreno,
#                Mali Utgard); qtbase filters on this feature, so leaving it on
#                would silently enable a Qt backend nothing can run
#   debuginfod - pulls a debuginfod client into the image
# acl, ipv4 and vfat are left enabled: they are newly gained relative to
# scarthgap but harmless, and vfat matches what the machine configs already ask
# for in MACHINE_FEATURES.
#
# sysvinit and bluez4 are no longer listed here because neither exists in
# wrynose's defaults any more - systemd dropped SysV compatibility entirely.
DISTRO_FEATURES_OPTED_OUT += "x11 ptest multiarch vulkan debuginfod"

DISTRO_FEATURES = "\
  ${WEBOS_DISTRO_FEATURES_DEFAULT} \
  ${WEBOS_DISTRO_FEATURES_OPENGL} \
  ${WEBOS_DISTRO_FEATURES_WINDOW_SYSTEM} \
  ${WEBOS_DISTRO_FEATURES_BUSES} \
  ${WEBOS_DISTRO_FEATURES_USB} \
  ${WEBOS_DISTRO_FEATURES_CONNECTIVITY} \
  ${WEBOS_DISTRO_FEATURES_NETWORKING} \
  ${WEBOS_DISTRO_FEATURES_SECURITY} \
  ${WEBOS_DISTRO_FEATURES_LIBC} \
  ${WEBOS_DISTRO_FEATURES_LD} \
  ${WEBOS_DISTRO_FEATURES_INIT} \
  ${WEBOS_DISTRO_FEATURES_EXTENDED} \
"

# Enable adb support
DISTRO_FEATURES += "adb"

# Use vc4graphics for all raspberrypi MACHINEs
MACHINE_FEATURES:append:rpi = " vc4graphics"
