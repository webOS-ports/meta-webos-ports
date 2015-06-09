# Copyright (c) 2012-2014 LG Electronics, Inc.

require conf/distro/include/luneos-preferred-providers.inc
require conf/distro/include/luneos-recipe-blacklist.inc
require conf/distro/include/luneos-preferred-versions.inc
require conf/distro/include/luneos-features.inc
require conf/distro/include/luneos-toolchain.inc
require conf/distro/include/luneos-sdk.inc


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
WEBOS_DISTRO_VERSION_CODENAME ?= "crema"
# WEBOS_DISTRO_VERSION_CODENAME ?= "${@base_get_metadata_git_branch('.', None).strip().translate(None, '()/')}"
WEBOS_DISTRO_RELEASE_CODENAME ?= "${WEBOS_DISTRO_PLATFORM_CODENAME}${@ base_conditional( 'WEBOS_DISTRO_VERSION_CODENAME', '', '', '-${WEBOS_DISTRO_VERSION_CODENAME}', d) }"

# This is the codename for the series of builds that are made from a branch.
# It appears in the image filenames. It is expected that other distros will
# override this based on their codename schemes.
WEBOS_DISTRO_BUILD_CODENAME ?= ""
# WEBOS_DISTRO_BUILD_CODENAME ?= "${@base_get_metadata_git_branch('.', None).strip().translate(None, '()/')}"

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
WEBOS_IMAGE_NAME_SUFFIX = "${@ base_conditional( 'WEBOS_DISTRO_BUILD_CODENAME', '', '', '-${WEBOS_DISTRO_BUILD_CODENAME}', d) }"
WEBOS_IMAGE_NAME_SUFFIX .= "${@ base_conditional( 'WEBOS_DISTRO_BUILD_ID', 'unofficial', '', '-${WEBOS_DISTRO_BUILD_ID}', d) }"
IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}"
KERNEL_IMAGE_BASE_NAME = "${KERNEL_IMAGETYPE}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}"
MODULE_IMAGE_BASE_NAME = "modules-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}${WEBOS_IMAGE_NAME_SUFFIX}"

# Don't rebuild the kernel (or the rootfs) images if the only thing that's changed
# is the start time of the build or the build number.
BB_HASHBASE_WHITELIST_append = " WEBOS_IMAGE_NAME_SUFFIX"

# Add WEBOS_DISTRO_RELEASE_CODENAME and WEBOS_DISTRO_BUILD_ID to what's displayed
# as the "build configuration".
BUILDCFG_VARS += "WEBOS_DISTRO_RELEASE_CODENAME WEBOS_DISTRO_BUILD_ID"

# Branch, revision and description of TOPDIR (webos-ports-setup repository)
SETUP_COMMON_DIR = "${TOPDIR}/../common/"
WEBOS_DISTRO_TOPDIR_BRANCH ?= "${@base_get_metadata_git_branch('${SETUP_COMMON_DIR}', None).strip()}"
WEBOS_DISTRO_TOPDIR_REVISION ?= "${@base_get_metadata_git_revision('${SETUP_COMMON_DIR}', None).strip()}"
# Don't add WEBOS_DISTRO_TOPDIR_BRANCH, because in many cases it can be "(nobranch)"
BUILDCFG_VARS += "WEBOS_DISTRO_TOPDIR_REVISION"

# Add build timestamp to what's displayed in build-id.txt for use in pruning script
BUILDCFG_VARS += "DATETIME"

# Override these in webOS based distros to modify DISTRO_EXTRA_R*
WEBOS_DEFAULT_EXTRA_RDEPENDS = ""
WEBOS_DEFAULT_EXTRA_RRECOMMENDS = ""

DISTRO_EXTRA_RDEPENDS += " ${WEBOS_DEFAULT_EXTRA_RDEPENDS}"
DISTRO_EXTRA_RRECOMMENDS += " ${WEBOS_DEFAULT_EXTRA_RRECOMMENDS}"

WEBOSQEMUDEPS = "${@base_contains("INCOMPATIBLE_LICENSE", "GPLv3", "", "qemu-config",d)}"
DISTRO_EXTRA_RDEPENDS_append_qemuall = " ${WEBOSQEMUDEPS}"

USER_CLASSES ?= ""

# WEBOS_EXTRA_MACHINEOVERRIDES will appear in OVERRIDES before what's added by
# the machine architecture .inc file, which appears before MACHINE.
# WEBOS_EXTRA_MACHINEOVERRIDES is set in webos_machine_impl_dep.bbclass . Do it
# this way instead of just in the bbclass so that the position in OVERRIDES is
# fixed.
WEBOS_EXTRA_MACHINEOVERRIDES ?= ""
MACHINEOVERRIDES =. "${WEBOS_EXTRA_MACHINEOVERRIDES}"
MACHINEOVERRIDES[vardepsexclude] += "WEBOS_EXTRA_MACHINEOVERRIDES"
OVERRIDES[vardepsexclude] += "WEBOS_EXTRA_MACHINEOVERRIDES"

INHERIT_DISTRO ?= "debian devshell sstate license buildstats buildstats-summary luneos_mirrors"

WARN_QA_append = " version-going-backwards webos-enh-sub-warning"
WEBOS_ERROR_QA_AUTOREV ?= "webos-enh-sub-autorev-error"
ERROR_QA_append = " webos-enh-sub-error ${WEBOS_ERROR_QA_AUTOREV}"
ERROR_QA_remove = " version-going-backwards"

QEMU_TARGETS ?= "arm i386"

# Default WEBOS_TARGET_* values (see webos_cmake.bbclass)
WEBOS_TARGET_CORE_OS ?= "rockhopper"
WEBOS_TARGET_MACHINE_IMPL ?= "device"
# For qemu we build for the emulator but for all other targets for a real device
WEBOS_TARGET_MACHINE_IMPL_qemuall = "emulator"

# The Open webOS distro is packaged into these image types:
IMAGE_FSTYPES ?= "tar.gz"

# Only create a vmdk image when we're building for x86 emulator,
# i.e. when MACHINE is qemux86 or qemux86-64
# If IMAGE_FSTYPES_append_qemux86 = " vmdk" is used, the inherit of
# image-vmdk.bbclass in image.bbclass doesn't happen, because IMAGE_FSTYPES
# overrides are expanded too late.
IMAGE_FSTYPES := "${@ '${IMAGE_FSTYPES} vmdk' if '${MACHINE}'.startswith('qemux86') else '${IMAGE_FSTYPES}' }"

# Give our emulator images some more space so developers have some more room
# for various things they are doing
IMAGE_ROOTFS_EXTRA_SPACE := "${@ '2097152' if '${MACHINE}'.startswith('qemux86') else '${IMAGE_ROOTFS_EXTRA_SPACE}' }"

# "tar.bz2 ext3" is added by qemu.inc, replace tar.bz2 with tar.gz in it
IMAGE_FSTYPES := "${@ ' ${IMAGE_FSTYPES} '.replace(' tar.bz2 ',' tar.gz ') }"

PATH_prepend = "${WEBOS_EXTRA_PATH}"

EXTRAOPKGCONFIG = ""
OPKGLIBDIR_forcevariable := "${localstatedir}/lib"

# Redirect console output to keep it from spewing all over the VirtualBox window
# (This is added to qemux86 kernel cmdline by openembedded-core/meta/classes/syslinux.bbclass)
APPEND_append_qemuall = " console=ttyS0"

# The CONNECTIVITY_CHECK_URI's are used to test whether we can succesfully
# fetch from the network (and warn you if not). To disable the test set
# the variable to be empty.
CONNECTIVITY_CHECK_URIS ?= "http://jenkins.nas-admin.org \
                            http://build.webos-ports.org"

# TODO can check on all these host machines.
SANITY_TESTED_DISTROS ?= " \
            Ubuntu-12.04 \n \
            Ubuntu-12.10 \n \
            Ubuntu-13.04 \n \
            Ubuntu-14.04 \n \
            "

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

# needed in order to build gtk+ without x11 DISTRO_FEATURE
PACKAGECONFIG_pn-cairo += "directfb"
PACKAGECONFIG_pn-gtk+ += "directfb"
