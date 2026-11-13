FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Reap children on Android kernels that backport pidfd_open() but have no
# waitid(P_PIDFD); see the patch.
SRC_URI:append:class-target = " file://0001-gmain-reap-children-with-waitpid-when-waitid-has-no-P_PIDFD.patch"
