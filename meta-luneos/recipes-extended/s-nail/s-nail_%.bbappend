# s-nail-dotlock is setuid root so that s-nail can create lock files in a
# mail spool it cannot write to. LuneOS has no local mail spool; s-nail is here
# only as smartmontools' recommended mailer. Without the bit s-nail still
# works, it just cannot dotlock a spool owned by someone else.
do_install:append() {
    chmod u-s ${D}${libexecdir}/s-nail-dotlock
}
