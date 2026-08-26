# Refuse to build a LuneOS rootfs for a machine that only supplies a boot image.
#
# In the GSI model a device machine's job shrank to a kernel, an initramfs and a
# boot image; the rootfs comes from the generic halium-arm64 build and is the
# same on every Halium arm64 device. A device machine that still builds its own
# rootfs is not wrong so much as wasteful and misleading - it produces a second,
# diverging userspace nobody deploys. sargo's last one was 1377 packages against
# 25 in its initramfs.
#
# The failure has to be loud. Left as a convention, "do not build the rootfs for
# sargo" is invisible: the build succeeds, takes an hour, and produces a tarball
# that looks exactly like the one you want. SkipRecipe turns it into an
# immediate parse-time error naming the machine to use instead.
#
# A machine opts in with LUNEOS_BOOTIMAGE_ONLY = "1" in its .conf.
python __anonymous() {
    if d.getVar('LUNEOS_BOOTIMAGE_ONLY') != '1':
        return
    machine = d.getVar('MACHINE')
    generic = d.getVar('LUNEOS_BOOTIMAGE_ONLY_ROOTFS') or 'halium-arm64'
    raise bb.parse.SkipRecipe(
        "%s is a boot-image-only machine: it builds a kernel, an initramfs and a "
        "boot image, and takes its rootfs from MACHINE=%s. Build the rootfs with "
        "MACHINE=%s instead, or unset LUNEOS_BOOTIMAGE_ONLY in %s.conf if this "
        "machine really does need its own userspace."
        % (machine, generic, generic, machine))
}
