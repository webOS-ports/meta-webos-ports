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

#Let's add the system and media users and groups early on as well as the netdev group, since these are needed by multiple recipes.

WEBOS_EXTRA_USERS ?= "system:x:1000:1000::/var:/usr/sbin/nologin \
                      input:x:1004:1004::/var:/usr/sbin/nologin \
                      audio:x:1005:1005::/var:/usr/sbin/nologin \
                      media:x:1013:1013::/var:/usr/sbin/nologin \
"
WEBOS_EXTRA_GROUPS ?= "system:x:1000:system \
                       netdev:x:82:system,network,bluetooth,wifi \
                       media:x:1013:media \
                       "

do_install:append() {
    local i

    for i in ${WEBOS_EXTRA_USERS}; do
        echo $i >>${D}${datadir}/base-passwd/passwd.master
    done
    awk -F: '{
        if ($1 in name || $3 in gid) exit 1
        name[$1] = 1; gid[$3] = 1
    }' ${D}${datadir}/base-passwd/passwd.master || \
      bbfatal "Same username or UID detected"

    for i in ${WEBOS_EXTRA_GROUPS}; do
        echo $i >>${D}${datadir}/base-passwd/group.master
    done
    awk -F: '{
        if ($1 in name || $3 in gid) exit 1
        name[$1] = 1; gid[$3] = 1
    }' ${D}${datadir}/base-passwd/group.master || \
      bbfatal "Same groupname or GID found"
}
