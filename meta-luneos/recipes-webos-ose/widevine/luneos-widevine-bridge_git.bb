SUMMARY = "Widevine bridge CDM for LuneOS's Chromium on halium devices"
DESCRIPTION = "The LuneOS half of luneos-widevine-bridge: the library CDM that \
Chromium loads from /var/lib/widevine/BridgeCdm and offers to the sites listed \
in its 'sites' file, forwarding sessions and samples to the Android Widevine \
HAL through the Android container. Also installs the boot services and the \
wvbridgectl helper. The bridge is off by default; enable it per device with \
'wvbridgectl install && wvbridgectl enable'. The Android half (wvbridged, \
wvlaunch, libwvshim.so) ships in the Halium GSI. Netflix is deliberately not \
on the site list: its servers refuse the HAL's licence request. Netflix and \
every other unlisted site use Google's CDM at /var/lib/widevine/WidevineCdm."
AUTHOR = "Herman van Hazendonk <github.com@herrie.org>"
HOMEPAGE = "https://github.com/webOS-ports/luneos-widevine-bridge"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "git://github.com/webOS-ports/luneos-widevine-bridge.git;protocol=https;branch=main"
SRCREV = "a636968badef43fde4fe9c43b2657b72856bb390"
PV = "0.1+git${SRCPV}"

# The bridge talks to the Android container at runtime, so it only does anything
# on a halium machine; the CDM library itself builds for any target.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:halium = "halium"

# Chromium's media::GetPlatformSpecificDirectory() appends _platform_specific/
# <os>_<arch>; neva loads libwidevinecdm.so from there under the bridge path.
WV_PLATDIR = "linux_${@d.getVar('TARGET_ARCH').replace('aarch64', 'arm64').replace('x86_64', 'x64')}"
WV_BASE = "${localstatedir}/lib/widevine"
WV_BCDM = "${WV_BASE}/BridgeCdm"
WV_BRIDGE = "${WV_BASE}/bridge"

# The source was built and checked warning-clean (clang and gcc, armv7/aarch64/
# x86-64) and through the static analysers upstream; here it is only compiled
# with the image toolchain, as the Makefile's LuneOS half expects.
do_compile() {
    oe_runmake CXX="${CXX}"
}

do_install() {
    # The bridge CDM, next to Google's WidevineCdm, offered to listed sites only.
    install -d ${D}${WV_BCDM}/_platform_specific/${WV_PLATDIR}
    install -m 0755 ${S}/libwidevinecdm.so ${D}${WV_BCDM}/_platform_specific/${WV_PLATDIR}/libwidevinecdm.so
    install -m 0644 ${S}/cdm/manifest.json ${D}${WV_BCDM}/manifest.json
    install -m 0644 ${S}/scripts/sites     ${D}${WV_BCDM}/sites

    # Boot services, sourced and installed by wvbridgectl at enable time.
    install -d ${D}${WV_BRIDGE}
    install -m 0644 ${S}/scripts/common.sh ${D}${WV_BRIDGE}/common.sh
    install -m 0755 ${S}/scripts/libs-up.sh ${D}${WV_BRIDGE}/libs-up.sh
    install -m 0755 ${S}/scripts/libs-down.sh ${D}${WV_BRIDGE}/libs-down.sh
    install -m 0755 ${S}/scripts/hal-pre.sh ${D}${WV_BRIDGE}/hal-pre.sh
    install -m 0755 ${S}/scripts/hal-post.sh ${D}${WV_BRIDGE}/hal-post.sh
    install -m 0755 ${S}/scripts/haltest.sh ${D}${WV_BRIDGE}/haltest.sh
    install -m 0644 ${S}/systemd/wvbridge-libs.service ${D}${WV_BRIDGE}/wvbridge-libs.service
    install -m 0644 ${S}/systemd/wvbridge-hal.service ${D}${WV_BRIDGE}/wvbridge-hal.service
    install -m 0644 ${S}/systemd/wvbridged.service ${D}${WV_BRIDGE}/wvbridged.service
    install -m 0644 ${S}/systemd/video-plane.conf ${D}${WV_BRIDGE}/video-plane.conf

    install -d ${D}${bindir}
    install -m 0755 ${S}/scripts/wvbridgectl ${D}${bindir}/wvbridgectl
}

FILES:${PN} += "${WV_BASE}"

# libwidevinecdm.so is a dlopen plugin, not a linkable library, and it lives
# outside ${libdir}; it links libstdc++ and libgcc statically.
INSANE_SKIP:${PN} += "dev-so"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

# wvbridgectl drives systemd and the Android container; the units call lxc.
# webruntime (which reads /var/lib/widevine) is pulled into the image by the
# packagegroup, not depended on here.
RDEPENDS:${PN} = "lxc"
