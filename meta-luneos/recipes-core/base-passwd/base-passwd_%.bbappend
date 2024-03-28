# This would work:
# SRC_URI:remove = "file://0002-Use-bin-sh-instead-of-bin-bash-for-the-root-user.patch"
# but then we would need to provide own version of 0003-Remove-for-root-since-we-do-not-have-an-etc-shadow.patch
# as the one in oe-core expects 0002-Use-bin-sh-instead-of-bin-bash-for-the-root-user.patch to be already
# applied on the same line, do_configure:prepend to use bash is simpler

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Need to change the IDs of input and audio group to match those from
# the android system, but they are also used in webOS anyway

SRC_URI:append = " file://0001-base-passwd-Update-gid-for-input-and-audio-so-they-m.patch"

do_configure:prepend() {
    # Undo 0002-Use-bin-sh-instead-of-bin-bash-for-the-root-user.patch
    sed -i 's%^\(root:.*\):/bin/sh%\1:/bin/bash%g' ${S}/passwd.master
}
