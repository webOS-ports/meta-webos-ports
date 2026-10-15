SUMMARY = "v4l2loopback - virtual V4L2 loopback video device kernel module"
DESCRIPTION = "Out-of-tree kernel module that creates virtual V4L2 devices a \
userspace producer can write frames into and any V4L2 consumer can read. On \
Halium it is the kernel half of droidcam2v4l2, which pumps the Android camera \
(droidmedia) into a /dev/video node so ordinary V4L2 apps (browsers, video \
calls) see the camera. Like any out-of-tree module it builds against whatever \
kernel MACHINE resolves to, so it is named per-machine in MACHINE_EXTRA_RDEPENDS \
(as wireguard-module is), only on devices that build their own kernel."
HOMEPAGE = "https://github.com/umlaeute/v4l2loopback"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/umlaeute/v4l2loopback.git;branch=main;protocol=https \
           file://0001-build-on-forward-ported-pre-4.0-kernels.patch \
"
# v0.15.4, the single v4l2loopback recipe for every device. The patch only
# relaxes the module's hard pre-4.0 #error and, below 4.0.0, maps the modern
# 4-arg v4l2_event_subscribe() onto whichever arity (2/3/4-arg) the tree's core
# keeps; it is a no-op on 4.0+ kernels, so the many Halium devices on 4.x/5.x
# kernels are unaffected. The pre-4.0 devices (tenderloin, hammerhead, mako,
# sm-t520, bullhead, a002, goyavewifi, a3-2015, flo-halium) each carry the V4L2-core
# forward-port the patch header describes in their shr-distribution/linux branch.
SRCREV = "0f9ee86760b7f2bea174b7e3e7a1d38845da0ab4"
PV = "0.15.4"

DEPENDS = "virtual/kernel"
inherit module

# v4l2loopback's Makefile drives the kernel build through KERNEL_DIR (not
# module.bbclass's KERNEL_PATH), its module target is "v4l2loopback.ko", and its
# "install" target installs only the module (install-all would also build the
# ctl utility, man page and headers, which need extra host tooling and are not
# wanted here).
EXTRA_OEMAKE += "KERNEL_DIR=${STAGING_KERNEL_DIR}"
MAKE_TARGETS = "v4l2loopback.ko"
MODULES_INSTALL_TARGET = "install"

# GCC 15 defaults to -fPIE and emits GOT-relative code; the 32-bit ARM module
# loader on the old (3.4) kernels can't resolve the resulting
# _GLOBAL_OFFSET_TABLE_ reference ("Unknown symbol" on insmod). Build the module
# non-PIC on 32-bit ARM. Scoped to arm so the arm64/x86 devices that share this
# recipe (and build fine as-is) are not disturbed.
EXTRA_OEMAKE:append:arm = " KCFLAGS=-fno-pic"

KERNEL_MODULE_AUTOLOAD += "v4l2loopback"
