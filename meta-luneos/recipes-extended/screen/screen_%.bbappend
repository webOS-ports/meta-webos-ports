# screen installs itself setuid root for its multiuser mode and for utmp
# entries, neither of which LuneOS uses. It is a developer tool here, run by
# root over ssh or adb, and works the same without the bit.
do_install:append() {
    chmod u-s ${D}${bindir}/screen-${PV}
}
