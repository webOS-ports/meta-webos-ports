# chfn and chsh are setuid root so that users can change their own GECOS
# field and login shell. LuneOS has no interactive user accounts, so the bits
# are only attack surface. Root can still run them.
do_install:append:class-target() {
    chmod u-s ${D}${bindir}/chfn ${D}${bindir}/chsh
}
