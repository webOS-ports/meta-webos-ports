# SBOM, CVE and VEX data for LuneOS builds.

# create-spdx is in oe-core's default INHERIT_DISTRO, which luneos.inc
# replaces with its own list; it is listed there again. vex writes each
# recipe's CVE status and an image .vex.json without needing a CVE database.
INHERIT += "vex"

# sbom-cve-check matches each image's SPDX SBOM against the NVD and CVE List
# databases. Those are fetched at AUTOREV and are several GB, so it is for CI
# and release builds: set LUNEOS_SBOM_CVE_CHECK = "1" in local.conf. This uses
# oe-core's own fragment, the same as
# "bitbake-config-build enable-fragment core/yocto/sbom-cve-check".
LUNEOS_SBOM_CVE_CHECK ??= "0"
require ${@'conf/fragments/yocto/sbom-cve-check.conf' if d.getVar('LUNEOS_SBOM_CVE_CHECK') == '1' else ''}

# CPE names and versions where the recipe name or version does not match
# what the CVE databases use.
KERNEL_CLASSES:append = " luneos_kernel_cve"

# webruntime is Chromium; its CVEs are filed against Google Chrome.
CVE_PRODUCT:pn-webruntime = "google:chrome"
CVE_VERSION:pn-webruntime = "${CHROMIUM_PV}"
CVE_PRODUCT:pn-webruntime-clang = "google:chrome"
CVE_VERSION:pn-webruntime-clang = "${CHROMIUM_PV}"

CVE_PRODUCT:pn-lxc = "linuxcontainers:lxc"

# adb and adbd are built from AOSP 5.1.1 (5.1.1.r37); their CVEs are filed
# against Android. This also matches CVEs in parts of Android that are not
# built here, which the weekly triage has to mark as such.
CVE_PRODUCT:pn-android-tools = "google:android"
CVE_VERSION:pn-android-tools = "5.1.1"
