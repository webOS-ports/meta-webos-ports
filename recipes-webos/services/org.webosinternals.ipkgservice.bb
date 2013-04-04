SUMMARY = "Package management service for Open webOS"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 mjson"

inherit webos_component
inherit webos_public_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus

WEBOS_COMPONENT_VERSION = "2.0.0"
PV = "${WEBOS_COMPONENT_VERSION}+git${SRCPV}"
WEBOS_SUBMISSION = "0"

SRCREV = "65cb032e6d61a3190ff58bc76b485d4ccf864702"
SRC_URI = "git://github.com/webOS-ports/preware;protocol=git;branch=master"
S = "${WORKDIR}/git/oe-service"

pkg_postinst_${PN}() {
    #!/bin/sh -e

    # Make sure we're only executed on the device
    if [ x"$D" = "x" ]; then
        APPS=/media/cryptofs/apps

        # Create the opkg config and database areas
        mkdir -p $APPS/${sysconfdir}/opkg $APPS/${libdir}/opkg/cache

        # Remove all list database cache files
        rm -f $APPS/${localstatedir}/lib/opkg/lists/*

        # Set up the architecture configuration file
        rm -f $APPS/${sysconfdir}/opkg/arch.conf
        cp ${sysconfdir}/opkg/arch.conf $APPS/${sysconfdir}/opkg/arch.conf

        # Install optware feeds
        echo "src/gz optware http://ipkg.preware.org/feeds/optware/all" > $APPS/${sysconfdir}/opkg/optware.conf
        echo "src/gz optware-`/bin/uname -m` http://ipkg.preware.org/feeds/optware/`/bin/uname -m`" >> $APPS/${sysconfdir}/opkg/optware.conf
        sed -i -e 's|armv7l|armv7|g' $APPS/${sysconfdir}/opkg/optware.conf

        # Install webosinternals feeds
        echo "src/gz webosinternals http://ipkg.preware.org/feeds/webos-internals/all" > $APPS/${sysconfdir}/opkg/webos-internals.conf
        echo "src/gz webosinternals-`/bin/uname -m` http://ipkg.preware.org/feeds/webos-internals/`/bin/uname -m`" >> $APPS/${sysconfdir}/opkg/webos-internals.conf
        sed -i -e 's|armv7l|armv7|g' $APPS/${sysconfdir}/opkg/webos-internals.conf

        # Install the alpha testing feeds (disabled by default)
        if [ -f /var/preferences/org.webosinternals.preware/enable-alpha-feeds ] ; then

          # Install alpha optware feeds
          echo "src/gz alpha-optware http://ipkg.preware.org/alpha/optware/all" > $APPS/${sysconfdir}/opkg/alpha-optware.conf.new
          echo "src/gz alpha-optware-`/bin/uname -m` http://ipkg.preware.org/alpha/optware/`/bin/uname -m`" >> $APPS/${sysconfdir}/opkg/alpha-optware.conf.new
          sed -i -e 's|armv7l|armv7|g' $APPS/${sysconfdir}/opkg/alpha-optware.conf.new
          sed -i -e 's|armv6l|armv6|g' $APPS/${sysconfdir}/opkg/alpha-optware.conf.new

          # Install alpha apps feeds
          echo "src/gz alpha-apps http://ipkg.preware.org/alpha/apps/all" > $APPS/${sysconfdir}/opkg/alpha-apps.conf.new
          echo "src/gz alpha-apps-`/bin/uname -m` http://ipkg.preware.org/alpha/apps/`/bin/uname -m`" >> $APPS/${sysconfdir}/opkg/alpha-apps.conf.new
          sed -i -e 's|armv7l|armv7|g' $APPS/${sysconfdir}/opkg/alpha-apps.conf.new
          sed -i -e 's|armv6l|armv6|g' $APPS/${sysconfdir}/opkg/alpha-apps.conf.new

          # Remove the obsolete testing feeds
          rm -f $APPS/${sysconfdir}/opkg/webos-testing*.conf*
          rm -f $APPS/${sysconfdir}/opkg/optware-testing*.conf*
          rm -f $APPS/${sysconfdir}/opkg/webos-*-testing*.conf*
        fi

        # Install the beta testing feeds (disabled by default)
        if [ -f /var/preferences/org.webosinternals.preware/enable-beta-feeds ] ; then

          # Install beta optware feeds
          echo "src/gz beta-optware http://ipkg.preware.org/beta/optware/all" > $APPS/${sysconfdir}/opkg/beta-optware.conf.new
          echo "src/gz beta-optware-`/bin/uname -m` http://ipkg.preware.org/beta/optware/`/bin/uname -m`" >> $APPS/${sysconfdir}/opkg/beta-optware.conf.new
          sed -i -e 's|armv7l|armv7|g' $APPS/${sysconfdir}/opkg/beta-optware.conf.new
          sed -i -e 's|armv6l|armv6|g' $APPS/${sysconfdir}/opkg/beta-optware.conf.new

          # Install beta apps feeds
          echo "src/gz beta-apps http://ipkg.preware.org/beta/apps/all" > $APPS/${sysconfdir}/opkg/beta-apps.conf.new
          echo "src/gz beta-apps-`/bin/uname -m` http://ipkg.preware.org/beta/apps/`/bin/uname -m`" >> $APPS/${sysconfdir}/opkg/beta-apps.conf.new
          sed -i -e 's|armv7l|armv7|g' $APPS/${sysconfdir}/opkg/beta-apps.conf.new
          sed -i -e 's|armv6l|armv6|g' $APPS/${sysconfdir}/opkg/beta-apps.conf.new

          # Remove the old testing feeds
          rm -f $APPS/${sysconfdir}/opkg/webos-testing*.conf*
          rm -f $APPS/${sysconfdir}/opkg/optware-testing*.conf*
          rm -f $APPS/${sysconfdir}/opkg/webos-*-testing*.conf*
        fi

        # Retain disabled status of existing feeds
        if [ "`ls $APPS/${sysconfdir}/opkg/*.disabled`" ] ; then
          for f in $APPS/${sysconfdir}/opkg/*.disabled ; do
            if [ -f $APPS/${sysconfdir}/opkg/`basename $f .disabled` ] ; then
              rm -f $f
              mv $APPS/${sysconfdir}/opkg/`basename $f .disabled` $f
            fi
          done
        fi

        # Assert disabled status of new feeds
        if [ "`ls $APPS/${sysconfdir}/opkg/*.new`" ] ; then
          for f in $APPS/${sysconfdir}/opkg/*.new ; do
            if [ -f $APPS/${sysconfdir}/opkg/`basename $f .new` ] ; then
              rm -f $APPS/${sysconfdir}/opkg/`basename $f .new`
              mv $f $APPS/${sysconfdir}/opkg/`basename $f .new`
            else
              rm -f $APPS/${sysconfdir}/opkg/`basename $f .new`.disabled
              mv $f $APPS/${sysconfdir}/opkg/`basename $f .new`.disabled
            fi
          done
        fi
    else
        exit 1
    fi
}
