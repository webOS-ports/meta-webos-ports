# Copyright (c) 2012-2014 LG Electronics, Inc.

require conf/distro/include/luneos-preferred-providers.inc
require conf/distro/include/luneos-recipe-blacklist.inc
require conf/distro/include/luneos-preferred-versions.inc
require conf/distro/include/luneos-features.inc
require conf/distro/include/luneos-toolchain.inc
require conf/distro/include/luneos-sdk.inc

require conf/distro/include/no-static-libs.inc

#
# Form the codename of a release (WEBOS_DISTRO_RELEASE_CODENAME) from a codename
# that represents the major set of features of the platform
# (WEBOS_DISTRO_PLATFORM_CODENAME) and a codename that represents the features
# of the particular DISTRO_VERSION being released (WEBOS_DISTRO_VERSION_CODENAME).
# The two more or less correspond the the major and minor versions of
# DISTRO_VERSION, but allow there to be a name for them ahead of the assignment
# of actual version numbers. It is expected that there will be several version
# codenames with the same platform codename under active development at the same
# time.
#
# It is expected that other distros will override these definitions based on
# their codename schemes.
#

# It is expected that other distros will override these definitions.
WEBOS_PORTS_CODENAME = "ports"
# WEBOS_DISTRO_PLATFORM_CODENAME ?= "${DISTRO}-${WEBOS_PORTS_CODENAME}"
WEBOS_DISTRO_PLATFORM_CODENAME ?= "luneos"
WEBOS_DISTRO_VERSION_CODENAME ?= "Espressino"
WEBOS_DISTRO_RELEASE_CODENAME ?= "${WEBOS_DISTRO_PLATFORM_CODENAME}${@oe.utils.conditional( 'WEBOS_DISTRO_VERSION_CODENAME', '', '', '-${WEBOS_DISTRO_VERSION_CODENAME}', d) }"

# This is the codename for the series of builds that are made from a branch.
# It appears in the image filenames. It is expected that other distros will
# override this based on their codename schemes.
WEBOS_DISTRO_BUILD_CODENAME ?= "${WEBOS_DISTRO_TOPDIR_BRANCH}"

WEBOS_DISTRO_PRERELEASE ?= "devel"


#
# All of the types of official build jobs should override WEBOS_DISTRO_BUILD_ID
# in the following format: "[[<prefix>.]<site>-]<build-number>", where the
# optional <site> identifies the build server site, and the optional <prefix>
# distinguishes the different types of build jobs, for example, verification
# builds. <prefix> is left off for the "official" build and <site> is dropped
# for the authoritative "official" build. <build-number> is expected to be an
# integer. Note that WEBOS_DISTRO_BUILD_CODENAME is never included in
# WEBOS_DISTRO_BUILD_ID.
#
WEBOS_DISTRO_BUILD_ID ?= "unofficial"

# use WEBOS_DISTRO_BUILD_ID.
IMAGE_VERSION_SUFFIX = "${@oe.utils.conditional( 'WEBOS_DISTRO_BUILD_CODENAME', '', '', '-${WEBOS_DISTRO_BUILD_CODENAME}', d) }"
IMAGE_VERSION_SUFFIX .= "${@oe.utils.conditional( 'WEBOS_DISTRO_BUILD_ID', 'unofficial', '-${DATETIME}', '-${WEBOS_DISTRO_BUILD_ID}', d) }"

# Add WEBOS_DISTRO_RELEASE_CODENAME and WEBOS_DISTRO_BUILD_ID to what's displayed
# as the "build configuration".
BUILDCFG_VARS += "WEBOS_DISTRO_RELEASE_CODENAME WEBOS_DISTRO_BUILD_ID"

# Branch, revision and description of TOPDIR (webos-ports-setup repository)
SETUP_COMMON_DIR = "${TOPDIR}/../common/"
WEBOS_DISTRO_TOPDIR_BRANCH ?= "${@oe.buildcfg.get_metadata_git_branch('${SETUP_COMMON_DIR}').strip()}"
WEBOS_DISTRO_TOPDIR_REVISION ?= "${@oe.buildcfg.get_metadata_git_revision('${SETUP_COMMON_DIR}').strip()}"
# Depends on https://lists.openembedded.org/g/openembedded-core/message/190292 or webos_base.bbclass removed in https://github.com/webOS-ports/meta-webos-ports/commit/20884dd8b7e78cbfa3b5e485bdfca24584c644b3
# WEBOS_DISTRO_TOPDIR_DESCRIBE ?= "${@oe.buildcfg.get_metadata_git_describe('${SETUP_COMMON_DIR}').strip()}"
BUILDCFG_VARS += "WEBOS_DISTRO_TOPDIR_BRANCH WEBOS_DISTRO_TOPDIR_REVISION"

# Add build timestamp to what's displayed in build-id.txt for use in pruning script
BUILDCFG_VARS += "DATETIME"

# Add the same variables from BUILDCFG_VARS to /etc/buildinfo as well
IMAGE_BUILDINFO_VARS = "${BUILDCFG_VARS}"

# Override these in webOS based distros to modify DISTRO_EXTRA_R*
WEBOS_DEFAULT_EXTRA_RDEPENDS = ""
WEBOS_DEFAULT_EXTRA_RRECOMMENDS = ""

DISTRO_EXTRA_RDEPENDS += " ${WEBOS_DEFAULT_EXTRA_RDEPENDS}"
DISTRO_EXTRA_RRECOMMENDS += " ${WEBOS_DEFAULT_EXTRA_RRECOMMENDS}"

DISTRO_EXTRA_RDEPENDS:append:qemuall = " kernel-module-vboxvideo kernel-module-vboxguest kernel-module-uvesafb kernel-module-virtio-gpu kernel-module-vmwgfx"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS:remove:qemuall = "v86d"

KERNEL_MODULE_AUTOLOAD:append:qemux86 = " vmwgfx vboxguest vboxsf"
KERNEL_MODULE_PROBECONF:append:qemux86 = " vmwgfx"
KERNEL_MODULE_AUTOLOAD:append:qemux86-64 = " vmwgfx vboxguest vboxsf"
KERNEL_MODULE_PROBECONF:append:qemux86-64 = " vmwgfx"

# Prevent vboxvideo from loading, it conflicts with vmwgfx
KERNEL_MODULE_PROBECONF:append:qemux86 = " vboxvideo"
KERNEL_MODULE_PROBECONF:append:qemux86-64 = " vboxvideo"
module_conf_vboxvideo:qemux86 = "blacklist vboxvideo"
module_conf_vboxvideo:qemux86-64 = "blacklist vboxvideo"

USER_CLASSES ?= ""

# Configure Bluetooth service classes we want to support
WEBOS_BLUETOOTH_ENABLED_SERVICE_CLASSES ?= "FTP"

# WEBOS_EXTRA_MACHINEOVERRIDES will appear in OVERRIDES before what's added by
# the machine architecture .inc file, which appears before MACHINE.
# WEBOS_EXTRA_MACHINEOVERRIDES is set in webos_machine_impl_dep.bbclass . Do it
# this way instead of just in the bbclass so that the position in OVERRIDES is
# fixed.
WEBOS_EXTRA_MACHINEOVERRIDES ?= ""
MACHINEOVERRIDES =. "${WEBOS_EXTRA_MACHINEOVERRIDES}"
MACHINEOVERRIDES[vardepsexclude] += "WEBOS_EXTRA_MACHINEOVERRIDES"
OVERRIDES[vardepsexclude] += "WEBOS_EXTRA_MACHINEOVERRIDES"

INHERIT_DISTRO ?= "debian devshell sstate license remove-libtool buildstats buildstats-summary image-buildinfo"

SOURCE_MIRROR_URL = "http://sources.webos-ports.org/"

WARN_QA:append = " version-going-backwards"
WEBOS_ERROR_QA_AUTOREV ?= "webos-enh-sub-autorev-error"
ERROR_QA:append = " webos-enh-sub-warning webos-enh-sub-error ${WEBOS_ERROR_QA_AUTOREV}"
ERROR_QA:remove = " version-going-backwards"

# Move all default WARN_QA from insane.bbclass to ERROR_QA to prevent silently re-introducing these issues
DEFAULT_WARN_QA = "\
    libdir xorg-driver-abi \
    textrel incompatible-license files-invalid \
    infodir build-deps src-uri-bad symlink-to-sysroot multilib \
    invalid-packageconfig host-user-contaminated uppercase-pn patch-fuzz \
    mime mime-xdg unlisted-pkg-lics unhandled-features-check \
    missing-update-alternatives native-last missing-ptest \
    license-exists license-no-generic license-syntax license-format \
    license-incompatible license-file-missing obsolete-license \
"
# patch-status is by default enabled only for oe-core layer
ERROR_QA:append = " ${DEFAULT_WARN_QA} patch-status"
WARN_QA:remove = "${DEFAULT_WARN_QA}"

# https://bugreports.qt.io/browse/QTBUG-105913
ERROR_QA:remove:layer-qt6-layer = "buildpaths"
WARN_QA:append:layer-qt6-layer = " buildpaths"

# Default WEBOS_TARGET_* values (see webos_cmake.bbclass)
WEBOS_TARGET_CORE_OS ?= "rockhopper"

# The Open webOS distro is packaged into these image types:
IMAGE_FSTYPES ?= "tar.gz"

# Only create a vmdk image when we're building for x86 emulator,
# i.e. when MACHINE is qemux86 or qemux86-64
# If IMAGE_FSTYPES:append:qemux86 = " vmdk" is used, the inherit of
# image-vmdk.bbclass in image.bbclass doesn't happen, because IMAGE_FSTYPES
# overrides are expanded too late.
IMAGE_FSTYPES := "${@ '${IMAGE_FSTYPES} wic.vmdk' if '${MACHINE}'.startswith('qemux86') else '${IMAGE_FSTYPES}' }"

# Give our emulator images some more space so developers have some more room
# for various things they are doing
IMAGE_ROOTFS_EXTRA_SPACE := "${@ '2097152' if '${MACHINE}'.startswith('qemux86') else '${IMAGE_ROOTFS_EXTRA_SPACE}' }"

# "tar.bz2 ext3" is added by qemu.inc, replace tar.bz2 with tar.gz in it
IMAGE_FSTYPES := "${@ ' ${IMAGE_FSTYPES} '.replace(' tar.bz2 ',' tar.gz ') }"

# Redirect console output to keep it from spewing all over the VirtualBox window
# (This is added to qemux86 kernel cmdline by openembedded-core/meta/classes/syslinux.bbclass)
APPEND:append:qemuall = " console=ttyS0"

# The CONNECTIVITY_CHECK_URI's are used to test whether we can succesfully
# fetch from the network (and warn you if not). To disable the test set
# the variable to be empty.
CONNECTIVITY_CHECK_URIS ?= "https://github.com"

# TODO can check on all these host machines.
SANITY_TESTED_DISTROS ?= " \
    ubuntu-22.04 \n \
    ubuntu-23.04 \n \
    ubuntu-23.10 \n \
"

require conf/distro/include/yocto-uninative.inc
INHERIT += "uninative"

# no interactive resolving, better to fail and report later
PATCHRESOLVE = "noop"

# save statistical info on images
INHERIT += "buildhistory"

# enable Hash Equivalence Server
# read https://lwn.net/Articles/804640/ for details
BB_SIGNATURE_HANDLER ?= "OEEquivHash"
BB_HASHSERVE ??= "auto"

# to build stuff with commercial licenses
# LICENSE_FLAGS_ACCEPTED is now used instead of COMMERCIAL_LICENSE, see:
# http://git.openembedded.org/?p=openembedded-core.git&a=commit;h=a66fdbda548fab367cada035c49a32d9bf8ea528
# we are not commercial project, we can whitelist them all
# RPi firmware has some very specific license (synaptics-killswitch) that needs whitelisting as well
LICENSE_FLAGS_ACCEPTED = "commercial synaptics-killswitch"

LICENSE_CREATE_PACKAGE = "1"

require conf/distro/include/defaulttunes.inc

#############################################################################
# Ensure MACHINE_CLASS is in OVERRIDES and is not empty as :: in OVERRIDES
# breaks order of OVERRIDES ie in FILESPATH
#############################################################################
MACHINE_CLASS ?= "${MACHINE}"
MACHINEOVERRIDES .= ":${MACHINE_CLASS}"
MACHINEOVERRIDES[vardepsexclude] = "MACHINE MACHINE_CLASS"
OVERRIDES[vardepsexclude] = "MACHINE MACHINE_CLASS"

inherit android_image_types

# To build lapack(blas) for snowboy
FORTRAN:forcevariable = ",fortran"

# Override 64bit time_t on armv7a, as there are still many cases where things
# like printf("[%5ld.%09ld]", time.tv_sec, time.tv_nsec) is done and provokes crashes
GLIBC_64BIT_TIME_FLAGS:remove:armv7a = "-D_TIME_BITS=64"
INSANE_SKIP:append:armv7a = " 32bit-time"
