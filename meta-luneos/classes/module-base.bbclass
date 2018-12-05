# import from oe-core/pyro and backport the fix to support newer kernels
#
# Adapt the fix from:
# commit 916cb2029d3c97bf12ebf03832b9ba980451dbcf
# Author: Bruce Ashfield <bruce.ashfield@windriver.com>
# Date:   Tue Feb 20 11:25:17 2018 -0500
#
#     make-mod-scripts: add build requirements for external modules
#
# to fix vboxguestdrivers dependency on native openssl (otherwise
# used from the build host).
# http://jenkins.nas-admin.org/view/luneos-testing/job/luneos-testing_qemux86/551/console

inherit kernel-arch

# This is instead of DEPENDS = "virtual/kernel"
do_configure[depends] += "virtual/kernel:do_compile_kernelmodules openssl-native:do_populate_sysroot"

export OS = "${TARGET_OS}"
export CROSS_COMPILE = "${TARGET_PREFIX}"

# This points to the build artefacts from the main kernel build
# such as .config and System.map
# Confusingly it is not the module build output (which is ${B}) but
# we didn't pick the name.
export KBUILD_OUTPUT = "${STAGING_KERNEL_BUILDDIR}"

export KERNEL_VERSION = "${@base_read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"
KERNEL_OBJECT_SUFFIX = ".ko"

# kernel modules are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE = " HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" HOSTCPP="${BUILD_CPP}""

# Function to ensure the kernel scripts are created. Expected to
# be called before do_compile. See module.bbclass for an example.
do_make_scripts() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS 
	oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
	           -C ${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} scripts
}
