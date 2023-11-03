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
WAYDROID_SYSTEM_IMAGE = "lineage-18.1-20231028-VANILLA-${WAYDROID_ARCH}-system.zip" 
WAYDROID_VENDOR_IMAGE = "lineage-18.1-20231028-MAINLINE-${WAYDROID_ARCH}-vendor.zip" 
WAYDROID_VENDOR_IMAGE:halium = "lineage-16.0-20231028-HALIUM_9-${WAYDROID_ARCH}-vendor.zip"

SHA256SUM_SYSTEM:x86-64 = "992853ed6849fd26cb750d880016ff605910661229fb3ab22447a7e6f1c8c112"
SHA256SUM_VENDOR:x86-64 = "c0057b233c5dddf7b8f3bb046d3114fa34589c776743ced61840615d4d48f5bc"

SHA256SUM_SYSTEM:aarch64 = "406adff7e346eab019a51287e49765a6d6c24d62c0a47eb74eb8ea9ad2c384ee"
SHA256SUM_VENDOR:aarch64 = "e67f0d92907bd74083f1f83da701609c94c4cdbd8ba7c662c27d3e94194aac70"

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
