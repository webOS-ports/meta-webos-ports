# Copyright (c) 2013 Hewlett-Packard Development Company, L.P.

PRINC := "${@int(PRINC) + 3}"

# for all 3 opkg classes use only prefix (/usr) because recipe itself appends /lib to it.
target_localstatedir := "${prefix}"

# for class-target we have to override whole EXTRA_OECONF because this one is using
# ${localstatedir}/lib in original recipe
EXTRA_OECONF_class-target = "\
  --disable-gpg \
  --disable-openssl \
  --disable-ssl-curl \
  --disable-curl \
  --disable-sha256 \
  --with-opkglibdir=${target_localstatedir}/lib \
"

do_install_append() {
    # We need to create the lock directory
    install -d ${D}${libdir}/opkg
    # ${localstatedir}/lib/opkg is created by do_install_append in original .bb
    rmdir ${D}${localstatedir}/lib/opkg
    rmdir ${D}${localstatedir}/lib
    rmdir ${D}${localstatedir}
}

FILES_libopkg = "${libdir}/*.so.* ${libdir}/opkg"

# We're running opkg-postinst on first boot a little bit earlier to be able to run
# different needed hardware initilization after this which needs things only available
# after postinst-phase is done (e.g. android binaries).
POSTINSTALL_INITPOSITION = "80"
