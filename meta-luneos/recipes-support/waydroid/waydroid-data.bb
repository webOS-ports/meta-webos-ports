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

# The container's vendor image has to match the *host's* VNDK, not the host's
# Android version and not the system image. Waydroid works this out at runtime
# from ro.vndk.version (initializer.py:get_vendor_type) and picks a vendor OTA
# channel from it. Pre-installing images makes that a build-time choice
# instead, so the same rule has to be applied here by hand.
#
# Measured on device: tissot and mido report vndk 28, so HALIUM_9, so the
# vendor image comes off the lineage-16.0 branch. This recipe previously
# shipped the HALIUM_11 (vndk 30) image to every Halium machine, which is
# correct only for a Halium 11 host.
#
# The `halium` override cannot express this on its own - meta-android-halium.inc
# sets it for every Halium machine and it says nothing about which level - so
# the level is a variable the machine picks.
#
# These pairings are final rather than merely current: upstream stopped
# building the HALIUM_9 vendor line after 2025-08-09 and the LineageOS 18.1
# system line after 2025-06-28. That is also why pre-installing is right for
# these two machines and wrong for a Treble target - pre-installed images make
# Waydroid write system_ota/vendor_ota = "None" and stop upgrading for good.
WAYDROID_HALIUM_LEVEL ?= ""
WAYDROID_HALIUM_LEVEL:tissot-halium = "HALIUM_9"
WAYDROID_HALIUM_LEVEL:mido-halium = "HALIUM_9"

WAYDROID_ARCH:x86-64 = "waydroid_x86_64"
WAYDROID_ARCH:aarch64 = "waydroid_arm64"
# 32-bit ARM is a separate build of both system and vendor, not an arm64
# image with a different vendor variant: mindphone reports armeabi-v7a with
# no arm64 in ro.product.cpu.abilist, and waydroid's arch.py maps armv7l to
# "arm". COMPATIBLE_MACHINE claimed these arches already; without this the
# download URL expanded with an empty arch.
WAYDROID_ARCH:armv7a = "waydroid_arm"
WAYDROID_ARCH:armv7ve = "waydroid_arm"

WAYDROID_SYSTEM_IMAGE = "lineage-18.1-20231028-VANILLA-${WAYDROID_ARCH}-system.zip"
WAYDROID_VENDOR_IMAGE = "lineage-18.1-20231028-MAINLINE-${WAYDROID_ARCH}-vendor.zip"

# Halium machines take the last build of each discontinued line rather than the
# 2023 images the non-Halium machines still use: 18.1 system ran to 2025-06-28
# and the HALIUM_9 vendor to 2025-08-09, so this is as new as either gets and
# there is nothing to track afterwards. The MAINLINE pairing above is left
# alone - qemux86-64 and the Pine machines are not part of this change.
WAYDROID_SYSTEM_IMAGE:halium = "lineage-18.1-20250628-VANILLA-${WAYDROID_ARCH}-system.zip"
WAYDROID_VENDOR_IMAGE:halium = "lineage-16.0-20250809-${WAYDROID_HALIUM_LEVEL}-${WAYDROID_ARCH}-vendor.zip"

SHA256SUM_SYSTEM:x86-64 = "992853ed6849fd26cb750d880016ff605910661229fb3ab22447a7e6f1c8c112"
SHA256SUM_VENDOR:x86-64 = "c0057b233c5dddf7b8f3bb046d3114fa34589c776743ced61840615d4d48f5bc"

SHA256SUM_SYSTEM:aarch64 = "406adff7e346eab019a51287e49765a6d6c24d62c0a47eb74eb8ea9ad2c384ee"
SHA256SUM_VENDOR:aarch64 = "e67f0d92907bd74083f1f83da701609c94c4cdbd8ba7c662c27d3e94194aac70"

# Keyed on `halium` rather than on arch, to match the two variables above.
# Both values are the arm64 builds, which is all that is needed while
# tissot-halium and mido-halium are the only Halium machines here; a 32-bit
# Halium machine would need these split by arch as well as by level.
SHA256SUM_SYSTEM:halium = "2700a68255c234f04453da15bfdaed0b0d30343f3af968cf39a096657d88a625"
SHA256SUM_VENDOR:halium = "923482aacbe37962055491b3974fa318e479da9a8c85a5e95a03d44893b4a54f"

# Content varies by machine (the vendor image above), so this cannot share a
# package with the other machines on this tune. tissot-halium, mido-halium and
# halium-arm64 all resolve TUNE_PKGARCH to aarch64-halium; without this they
# would publish different vendor images under one package arch and race for it
# in sstate and in the feed.
PACKAGE_ARCH = "${MACHINE_ARCH}"

# The two zips unpack their .img files straight into ${UNPACKDIR}, so the
# default S = "${UNPACKDIR}/${BP}" never comes into existence.
S = "${UNPACKDIR}"

SRC_URI = "https://sourceforge.net/projects/waydroid/files/images/system/lineage/${WAYDROID_ARCH}/${WAYDROID_SYSTEM_IMAGE};name=system \
           https://sourceforge.net/projects/waydroid/files/images/vendor/${WAYDROID_ARCH}/${WAYDROID_VENDOR_IMAGE};name=vendor \
           "

SRC_URI[system.sha256sum] = "${SHA256SUM_SYSTEM}"
SRC_URI[vendor.sha256sum] = "${SHA256SUM_VENDOR}"

do_install() {
    install -dm755 "${D}/usr/share/waydroid-extra/images"

    # makepkg have extracted the zips
    install -m 0644 "${UNPACKDIR}/system.img" "${D}/usr/share/waydroid-extra/images"
    install -m 0644 "${UNPACKDIR}/vendor.img" "${D}/usr/share/waydroid-extra/images"
}

FILES:${PN} += "/usr/share/waydroid-extra/images"
