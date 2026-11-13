SUMMARY = "F-Droid in the Waydroid container, as a system app"
DESCRIPTION = "Puts the F-Droid client and the F-Droid Privileged Extension into \
Waydroid's system overlay, so Android has an app store from its first boot and \
F-Droid can install and update apps without a prompt for each one. The client \
is an ordinary system app and updates itself through F-Droid as usual; the \
extension is a privileged app holding INSTALL_PACKAGES and DELETE_PACKAGES, \
which is what F-Droid looks for to install silently."
HOMEPAGE = "https://f-droid.org"
SECTION = "webos/support"

# The client is GPL-3.0-or-later, the extension Apache-2.0 (both as listed in
# the f-droid.org index). Neither APK carries its licence text, so the common
# licence files stand in.
LICENSE = "GPL-3.0-or-later & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-3.0-or-later;md5=1c76c4cc354acaac30ed4d5eefea7245 \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

# The versions f-droid.org marks as suggested (suggestedVersionCode in its
# index), as of 13 Nov 2026, and the sha256 the index lists for each. These are
# F-Droid's own builds, signed with its key; the client only uses an extension
# signed by that same key.
FDROID_CLIENT_VERSION_CODE = "1023052"
FDROID_PRIVEXT_VERSION_CODE = "2070"
PV = "1.23.2"

SRC_URI = " \
    https://f-droid.org/repo/org.fdroid.fdroid_${FDROID_CLIENT_VERSION_CODE}.apk;name=client;unpack=0 \
    https://f-droid.org/repo/org.fdroid.fdroid.privileged_${FDROID_PRIVEXT_VERSION_CODE}.apk;name=privext;unpack=0 \
    file://permissions_org.fdroid.fdroid.privileged.xml \
"
SRC_URI[client.sha256sum] = "985f5181d48bb6bafd54083a048b391271e0ab28385881cc41294fb01a222762"
SRC_URI[privext.sha256sum] = "429bde81414c8779283c2e552c4d27893ca8ed17dff6d7296cf2a06ff197672e"

S = "${UNPACKDIR}"

inherit allarch

# Waydroid mounts /var/lib/waydroid/overlay over the root of the system image
# and overlay/vendor over the vendor image (mount_overlays = True, its default),
# so a file at overlay/system/app/... is /system/app/... to Android.
WAYDROID_OVERLAY = "${localstatedir}/lib/waydroid/overlay"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # The client's only native library is stored uncompressed in the APK, so
    # Android maps it from there and nothing has to be extracted beside it.
    install -D -m 0644 ${UNPACKDIR}/org.fdroid.fdroid_${FDROID_CLIENT_VERSION_CODE}.apk \
        ${D}${WAYDROID_OVERLAY}/system/app/F-Droid/F-Droid.apk
    install -D -m 0644 ${UNPACKDIR}/org.fdroid.fdroid.privileged_${FDROID_PRIVEXT_VERSION_CODE}.apk \
        ${D}${WAYDROID_OVERLAY}/system/priv-app/F-DroidPrivilegedExtension/F-DroidPrivilegedExtension.apk

    # Android 13 enforces the privileged-permission allowlist
    # (ro.control_privapp_permissions=enforce in the Waydroid image): a priv-app
    # asking for a privileged permission that no allowlist grants stops
    # system_server from booting.
    install -D -m 0644 ${UNPACKDIR}/permissions_org.fdroid.fdroid.privileged.xml \
        ${D}${WAYDROID_OVERLAY}/system/etc/permissions/permissions_org.fdroid.fdroid.privileged.xml

    # Not optional. "waydroid init" creates overlay/vendor only when it has to
    # create overlay itself, and this package creates overlay first. The vendor
    # overlay mount has no fallback, so with the directory missing the
    # container would not start at all.
    install -d ${D}${WAYDROID_OVERLAY}/vendor
}

FILES:${PN} = " \
    ${WAYDROID_OVERLAY}/system \
    ${WAYDROID_OVERLAY}/vendor \
"
