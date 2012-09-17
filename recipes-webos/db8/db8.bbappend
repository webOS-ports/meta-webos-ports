FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI += " file://gcc-47-fix.patch"

# Compiling with -fno-rtti makes it impossible to link against our libraries from other
# binaries compiled with -frtti (which is default). Patch is submitted upstream and can
# be removed once db8 version is updated.
SRC_URI += " file://no-rtti.patch"
