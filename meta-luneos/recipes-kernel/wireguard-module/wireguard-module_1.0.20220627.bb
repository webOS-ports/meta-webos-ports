SUMMARY = "WireGuard kernel module, backported for pre-5.6 kernels"
DESCRIPTION = "wireguard-linux-compat: the upstream WireGuard backport for \
kernels 3.10-5.5, built as an out-of-tree module. Every device kernel built \
here so far predates 5.6 (WireGuard only became a native in-tree driver \
there), and connman's own wireguard plugin talks the kernel's WireGuard \
netlink family directly (via libmnl) rather than shelling out to wg-quick, \
so it needs a real kernel-side WireGuard, not a userspace implementation. \
This recipe is deliberately machine-agnostic: it builds against whatever \
kernel MACHINE resolves to, so the same recipe covers every pre-5.6 device \
(sargo, tissot, ...) without per-device kernel changes or a reflash - the \
module signs with that build's own kernel module signing key like any other \
out-of-tree module here (see memnotify-module for the existing precedent)."
HOMEPAGE = "https://www.wireguard.com/"
SECTION = "kernel/network"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/WireGuard/wireguard-linux-compat.git;branch=master;protocol=https;destsuffix=${BP} \
           file://0001-compat-let-the-build-say-the-kernel-already-has-skb_m.patch \
"
SRCREV = "18fbcd68a35a892527345dc5679d0b2d860ee004"

S = "${UNPACKDIR}/${BP}/src"

DEPENDS = "virtual/kernel"

inherit module

# src/Makefile is a standalone convenience wrapper for manual/DKMS use (its
# default target chains into "$(MAKE) -C $(KERNELDIR) M=$(PWD) ... modules"),
# not the plain Kbuild-only layout module.bbclass's module_do_compile expects.
# It has its own "KERNELDIR" variable rather than the KERNEL_PATH/KERNEL_SRC
# module.bbclass actually sets, so without this it silently falls back to
# KERNELDIR's own default (/lib/modules/$(uname -r)/build - the *host's*
# kernel) instead of building against ${STAGING_KERNEL_DIR}, and the module
# compile runs against the wrong kernel entirely.
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

# Same story as above: module.bbclass's do_install runs the bare
# "modules_install" target by default, which this Makefile does not define
# either (its own name for the same thing is "module-install" - it calls
# through to the real "modules_install" kbuild target itself, then runs
# depmod, which module_do_install() has already neutered via DEPMOD=echo on
# the command line, so that half is a harmless no-op here).
MODULES_INSTALL_TARGET = "module-install"

# compat.h decides from LINUX_VERSION_CODE alone whether the kernel already has
# skb_mark_not_on_list, which upstream stable picked up in 4.14.217. An Android
# vendor tree does not follow that arithmetic: mindphone's kernel calls itself
# 4.14.186 and has the backport, so the compat definition collides with the real
# one and every object fails with "redefinition of 'skb_mark_not_on_list'".
#
# The preprocessor cannot see a static inline function, so look at the kernel's
# own header here and tell compat.h what we found. Machine-agnostic by
# construction, which is the point of this recipe - it is the kernel that is
# checked, not the machine, so any other vendor tree carrying the same backport
# is covered without being named. Appended to Kbuild rather than passed through
# EXTRA_OEMAKE because src/Makefile is a wrapper that does not forward flags.
do_configure:append() {
    if grep -q 'skb_mark_not_on_list' ${STAGING_KERNEL_DIR}/include/linux/skbuff.h; then
        bbnote "kernel already has skb_mark_not_on_list; disabling the compat definition"
        echo 'ccflags-y += -DCOMPAT_HAVE_SKB_MARK_NOT_ON_LIST' >> ${S}/Kbuild
    fi
}

KERNEL_MODULE_AUTOLOAD += "wireguard"

# src/Kbuild forces obj-m (not obj-$(CONFIG_WIREGUARD)) whenever KBUILD_EXTMOD
# is set, i.e. whenever it is built out-of-tree the way this recipe builds
# it - so it always produces wireguard.ko here regardless of the target
# kernel's own defconfig, no CONFIG_WIREGUARD=y/m needed there at all.
