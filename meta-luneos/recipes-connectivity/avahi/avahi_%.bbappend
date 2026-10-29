# avahi-daemon is pulled in for driverless printing (luneos-print-adapter,
# packagegroup-luneos-extended) and only needs link-local mDNS.
#
# - enable-wide-area=no: no unicast DNS-SD lookups of browse domains
#   handed out by whatever network the phone joins.
# - publish-hinfo=no, publish-workstation=no: do not announce the OS/CPU
#   (HINFO) or a _workstation._tcp record for the device on every network.
#   avahi 0.8 already ships both as "no"; they are pinned here so a new
#   default cannot turn them back on.
do_install:append() {
    conf=${D}${sysconfdir}/avahi/avahi-daemon.conf
    sed -i \
        -e 's/^#\?enable-wide-area=.*/enable-wide-area=no/' \
        -e 's/^#\?publish-hinfo=.*/publish-hinfo=no/' \
        -e 's/^#\?publish-workstation=.*/publish-workstation=no/' \
        $conf
    for opt in enable-wide-area publish-hinfo publish-workstation; do
        grep -qx "$opt=no" $conf || bbfatal "avahi-daemon.conf: could not set $opt=no"
    done
}
