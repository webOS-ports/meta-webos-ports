SUMMARY = "Waydroid uses a container-based approach to boot a full Android system"
DESCRIPTION = "Android image file for Waydroid"
# this isn't very clear, there is no information in build.anbox.io and it surely doesn't
# cover all components included in this built image, e.g.
# https://aur.archlinux.org/packages/waydroid-image says Apache license
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

# works only for following 4 archs
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:x86-64 = "(.*)"
COMPATIBLE_MACHINE:armv7a = "(.*)"
COMPATIBLE_MACHINE:armv7ve = "(.*)"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

WAYDROID_ARCH:x86-64 = "waydroid_x86_64"
WAYDROID_ARCH:aarch64 = "waydroid_arm64"
WAYDROID_SYSTEM_IMAGE = "lineage-17.1-20211021-VANILLA-${WAYDROID_ARCH}-system.zip"
WAYDROID_VENDOR_IMAGE = "lineage-17.1-20211021-MAINLINE-${WAYDROID_ARCH}-vendor.zip"
WAYDROID_VENDOR_IMAGE:halium = "lineage-17.1-20211021-HALIUM_10-${WAYDROID_ARCH}-vendor.zip"

SHA256SUM_SYSTEM:x86-64 = "c56225255380e1707a0380555e1d55961a5bc5e670b2b769724b52ae40283d84"
SHA256SUM_VENDOR:x86-64 = "99512ed7bcc1190ead02447ba9a716f50eeb64a87135ce15a030475e119464f1"

SHA256SUM_SYSTEM:aarch64 = "ea5af33272e9761016d3f36fe7a414ac22b16f70214f5b4839ff75b806a77819"
SHA256SUM_VENDOR:aarch64 = "e4f98d0b7702ccee1caeb8757a9fdc9e7473ff0053dc8326d9b4da72699a5f84"

SRC_URI = "https://sourceforge.net/projects/waydroid/files/images/system/lineage/${WAYDROID_ARCH}/${WAYDROID_SYSTEM_IMAGE};name=system \
           https://sourceforge.net/projects/waydroid/files/images/vendor/${WAYDROID_ARCH}/${WAYDROID_VENDOR_IMAGE};name=vendor \
           "

SRC_URI[system.sha256sum] = "${SHA256SUM_SYSTEM}"
SRC_URI[vendor.sha256sum] = "${SHA256SUM_VENDOR}"

do_install() {
    install -dm755 "${D}/usr/share/waydroid-extra/images"

    # makepkg have extracted the zips
    install -m 0644 "${WORKDIR}/system.img" "${D}/usr/share/waydroid-extra/images"
    install -m 0644 "${WORKDIR}/vendor.img" "${D}/usr/share/waydroid-extra/images"
}

FILES:${PN} += "/usr/share/waydroid-extra/images"
