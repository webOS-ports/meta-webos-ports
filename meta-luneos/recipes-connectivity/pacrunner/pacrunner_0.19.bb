SUMMARY = "Proxy configuration daemon"
DESCRIPTION = "PACrunner resolves which proxy to use for a given URL. It \
answers on D-Bus for clients that cannot or do not want to execute proxy \
auto-configuration (PAC) scripts themselves, evaluating the script with a \
bundled Duktape interpreter and fetching it over HTTP with libcurl. It is \
ConnMan's companion daemon: ConnMan's own online check asks it to resolve \
the check URL, and without it that lookup fails outright even when the \
service has no proxy at all."
HOMEPAGE = "https://git.kernel.org/pub/scm/network/connman/pacrunner.git/about/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://src/main.c;beginline=1;endline=20;md5=8f46d3ca7257d2f6832e2734008c4ce1 \
"

DEPENDS = "dbus glib-2.0 curl"

SRC_URI = "${KERNELORG_MIRROR}/linux/network/connman/${BP}.tar.xz"
SRC_URI[sha256sum] = "d9778423e7ad20cea14232dfec97bb92213ab7e05c6aef817eb6d97a9437e7c5"

inherit autotools pkgconfig

# Duktape is vendored in the tarball and built as a static convenience
# library, so --enable-duktape adds no dependency. curl is the PAC fetcher.
#
# libproxy stays off: it builds a drop-in replacement for the libproxy shared
# library, which would collide with the real libproxy oe-core already carries.
EXTRA_OECONF = " \
    --enable-duktape \
    --enable-curl \
    --disable-libproxy \
"

# There is no systemd unit upstream and none is wanted: pacrunner is started
# on demand by D-Bus activation through org.pacrunner.service, and exits when
# it is no longer needed.
FILES:${PN} += "${datadir}/dbus-1/system-services"
