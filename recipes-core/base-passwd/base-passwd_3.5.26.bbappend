# Same as in base recipe but dropped the nobash.patch
SRC_URI = "${DEBIAN_MIRROR}/main/b/base-passwd/base-passwd_${PV}.tar.gz \
           file://add_shutdown.patch \
           file://input.patch"

do_configure_prepend() {
    # nobash.patch has interesting side-effect which we still need
    sed -i 's%^root:[^:]*:%root::%g' ${S}/passwd.master
}
