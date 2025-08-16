# Our Android Frankenkernels have some backports of patches and exotic patches that aren't in mainline, therefore patch lttng-modules in order to accomodate this.

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:halium = " \
    file://0001-sched.h-Fix-compile-error-with-HMP-scheduler-used-in.patch \
    file://0002-mm_vmscan.h-Update-the-kernel-version-check-for-mm_v.patch \
"
