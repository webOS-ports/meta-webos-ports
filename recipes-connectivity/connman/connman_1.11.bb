require connman.inc

PR = "r1"

# 1.11 tag
SRCREV = "f419c26f637f464ec1c63212bc4f2d069f728947"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://0002-storage.c-If-there-is-no-d_type-support-use-fstatat.patch \
            file://inet-fix-ip-cleanup-functions.patch \
            file://add-in.h-for-ipv6.patch"
S = "${WORKDIR}/git"
PR = "${INC_PR}.0"
