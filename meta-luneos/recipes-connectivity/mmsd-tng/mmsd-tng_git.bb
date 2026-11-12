SUMMARY = "Multimedia Messaging Service daemon"
DESCRIPTION = "mmsd-tng sends and receives MMS messages. LuneOS builds it \
with the oFono plugin only and runs it on the system bus."
HOMEPAGE = "https://gitlab.com/kop316/mmsd"
SECTION = "network"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

DEPENDS = "glib-2.0 glib-2.0-native libsoup c-ares libphonenumber protobuf mobile-broadband-provider-info"

SRCREV = "341117141f8d30949fb1294cc71ee44af9b4c90f"
PV = "2.6.4+git"

# The patches are the work submitted upstream, in order: parser bounds and
# leak fixes, the system bus option, the optional ModemManager plugin and the
# oFono plugin.
SRC_URI = " \
    git://gitlab.com/kop316/mmsd.git;protocol=https;branch=master \
    file://0001-meson-link-what-libphonenumber-s-headers-need-from-p.patch \
    file://0002-wsputil-keep-the-memchr-result-const-in-decode_text_.patch \
    file://0003-Fix-the-build-with-clang.patch \
    file://0004-wsputil-never-read-a-Text-string-past-its-length.patch \
    file://0005-mmsutil-decode-the-From-header-within-its-length.patch \
    file://0006-service-mmsutil-don-t-leak-every-GVariant-but-the-la.patch \
    file://0007-service-don-t-send-the-SMIL-part-with-a-NUL-terminat.patch \
    file://0008-service-try-every-address-the-MMSC-resolves-to.patch \
    file://0009-service-bind-every-connection-of-a-message-to-the-be.patch \
    file://0010-service-return-after-replying-with-an-error-to-Messa.patch \
    file://0011-service-don-t-lose-or-leak-a-request-when-sending-it.patch \
    file://0012-service-free-a-rejected-attachment-and-munmap-only-w.patch \
    file://0013-service-fix-the-leading-whitespace-test-in-valid_con.patch \
    file://0014-mmsutil-end-the-va_list-when-mms_encode_headers-fail.patch \
    file://0015-mmsutil-initialise-to-NULL-instead-of-the-uninitiali.patch \
    file://0016-service-providers-don-t-leak-or-reuse-the-attachment.patch \
    file://0017-unit-free-the-numbers-test-phone-utils-formats.patch \
    file://0018-wsputil-check-the-length-before-reading-a-PDU-octet.patch \
    file://0019-wsputil-keep-Content-Type-decoding-inside-its-Value-.patch \
    file://0020-wsputil-check-multipart-part-lengths-separately.patch \
    file://0021-service-check-the-push-PDU-lengths.patch \
    file://0022-service-only-build-the-push-PDU-hex-dump-when-debugg.patch \
    file://0023-mmsutil-don-t-leak-a-repeated-Content-ID.patch \
    file://0024-service-handle-IPv6-literals-in-the-proxy-and-the-Ho.patch \
    file://0025-mmsutil-make-the-decoded-header-text-valid-UTF-8.patch \
    file://0026-service-don-t-leak-the-Transaction-ID-of-a-retrieved.patch \
    file://0027-main-add-an-option-to-run-on-the-system-bus.patch \
    file://0028-meson-make-the-ModemManager-plugin-optional.patch \
    file://0029-plugins-add-an-oFono-plugin.patch \
    file://mmsd.conf \
    file://mmsd.service \
    file://mmsd-migrate-store \
"

inherit meson pkgconfig systemd useradd

EXTRA_OEMESON = "-Dofono=true -Dmodemmanager=disabled"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "mmsd.service"

# mmsd.service runs the daemon as this user; see the unit.
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --user-group --home-dir ${localstatedir}/lib/mmsd --no-create-home --shell /usr/sbin/nologin mmsd"

do_install:append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${UNPACKDIR}/mmsd.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/mmsd.service ${D}${systemd_system_unitdir}/

    # Run by mmsd.service before the daemon to move the store mmsd kept.
    install -d ${D}${libexecdir}
    install -m 0755 ${UNPACKDIR}/mmsd-migrate-store ${D}${libexecdir}/
}

# The APN database is read at run time to find the MMS settings of a carrier.
RDEPENDS:${PN} = "dbus mobile-broadband-provider-info"
RRECOMMENDS:${PN} = "${VIRTUAL-RUNTIME_ofono}"

# mmsd-tng takes over the bus name, the unit and the store of mmsd.
RPROVIDES:${PN} = "mmsd"
RREPLACES:${PN} = "mmsd"
RCONFLICTS:${PN} = "mmsd"
