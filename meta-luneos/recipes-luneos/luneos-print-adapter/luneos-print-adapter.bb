SUMMARY = "LuneOS print service, bridges CUPS/IPP onto the luna-service2 bus"
DESCRIPTION = "Reimplements the webOS com.palm.printmgr API on top of CUPS and \
IPP Everywhere, replacing HP's closed wprint PDL plugins. Preserves the \
original LS2 surface (two categories, 23 methods, the same keyword vocabulary \
and numeric error codes) so existing print dialogs keep working."
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 libpbnjson glib-2.0 cups"

# cupsd does the mDNS browsing and owns the queues; the adapter is only the
# translation surface and reports an empty printer list without it.
RDEPENDS:${PN} += "cups"

# Driverless discovery needs CUPS built with --with-dnssd=avahi. oe-core gates
# that on "zeroconf" in DISTRO_FEATURES, which luneos-features.inc already sets
# (WEBOS_DISTRO_FEATURES_NETWORKING), so no PACKAGECONFIG override is needed -
# but the daemon still has to be running for anything to be discovered.
RRECOMMENDS:${PN} += "avahi-daemon"

# What cups-filters buys, and what it costs.
#
# An IPP Everywhere printer advertises the formats it accepts. Many take
# application/pdf and image/jpeg directly, which is all this service ever
# sends, and those work with cups alone. But a large set of AirPrint printers
# accept only image/urf (Apple Raster) or image/pwg-raster, and converting PDF
# to those is what cups-filters does. Without it, printing to such a printer
# fails at the filter stage rather than doing anything sensible.
#
# The cost is real: cups-filters pulls poppler, qpdf, lcms, tiff and
# ghostscript. If image size matters more than covering raster-only printers,
# drop it with BAD_RECOMMENDATIONS - the service and cups both still work, just
# with a narrower set of printers.
RRECOMMENDS:${PN} += "cups-filters"

PV = "0.1.0-1+git"
SRCREV = "bbda833ba359016803e032e6319f114f345376e0"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "luneos-print-adapter"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# The spool directory holds in-flight page files handed over by rendering
# clients. Nothing in it survives a job, but it must exist and be private.
do_install:append() {
    install -d ${D}${localstatedir}/spool/luneos-print
    install -d ${D}${localstatedir}/preferences/com.palm.printmgr
}

FILES:${PN} += "\
    ${localstatedir}/spool/luneos-print \
    ${localstatedir}/preferences/com.palm.printmgr \
"
