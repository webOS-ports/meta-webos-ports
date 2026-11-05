
SUPPORTED_EXTRA_ARCHS ?= "armv7a-vfp-neon cortexa8-vfp-neon cortexa8t-vfp-neon cortexa9-vfp-neon cortexa9t-vfp-neon x86_64 i586"

# this is just to prevent opkg installing something from e.g. armv4t feed on armv7a, 
# just because some upgrade was available in armv4t feed sooner and when it gets
# available for armv7a (same version) it's not reinstalled to get it from feed with higher priority
SUPPORTED_EXTRA_ARCHS:armv7a ?= "armv7a-vfp-neon cortexa8-vfp-neon cortexa8t-vfp-neon cortexa9-vfp-neon cortexa9t-vfp-neon"
SUPPORTED_EXTRA_ARCHS:armv6 ?= "armv6"

# The feed CI publishes, per build tree: feeds.luneos.org is a Cloudflare R2
# bucket with that domain attached. build.webos-ports.org, where it used to be,
# no longer resolves. org.webosports.service.update reads the manifest from the
# same place (https://feeds.luneos.org/luneos-<tree>/manifest.json).
#
# opkg is built without curl, so it downloads with busybox wget, which fetches
# https:// through "openssl s_client -verify_return_error -verify_hostname":
# the server's certificate is checked against ca-certificates, and a failed
# check fails the download.
DISTRO_FEED_URI = "https://feeds.luneos.org/luneos-${WEBOS_DISTRO_TOPDIR_BRANCH}/ipk"

do_compile() {
    mkdir -p ${S}/${sysconfdir}/opkg
    for feed in all ${MACHINE_ARCH}; do
        echo "src/gz ${DISTRO_FEED_PREFIX}-${feed} ${DISTRO_FEED_URI}/${feed}" > ${S}/${sysconfdir}/opkg/${feed}-feed.conf
    done
    for feed in ${PACKAGE_EXTRA_ARCHS}; do
        echo " ${SUPPORTED_EXTRA_ARCHS} " | grep -q " ${feed} " && echo "src/gz ${DISTRO_FEED_PREFIX}-${feed} ${DISTRO_FEED_URI}/${feed}" > ${S}/${sysconfdir}/opkg/${feed}-feed.conf
    done
    # in case last grep -q was returning error
    exit 0
}

CONFFILES:${PN} = '${@ " ".join( [ ( "${sysconfdir}/opkg/%s-feed.conf" % feed ) for feed in "all ${MACHINE_ARCH}".split() ] ) }'
CONFFILES:${PN} += '${@ " ".join( [ ( "${sysconfdir}/opkg/%s-feed.conf" % feed ) for feed in set("${PACKAGE_EXTRA_ARCHS}".split()).intersection(set("${SUPPORTED_EXTRA_ARCHS}".split())) ] ) }'
