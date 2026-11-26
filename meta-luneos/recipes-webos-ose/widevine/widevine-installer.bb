SUMMARY = "On-device installer for Google's Widevine CDM"
DESCRIPTION = "Downloads Google's proprietary Widevine CDM from Google's own \
servers, on the device, when the owner opts in (the First Use 'streaming DRM' \
choice), and installs it to /var/lib/widevine/WidevineCdm for LuneOS's \
Chromium. LuneOS ships only this installer (free software) and never \
redistributes Google's binary - the same model as Debian's widevine-installer. \
The install is driven by the 'enableWidevineCdm' system preference: a systemd \
path unit watches the preference store and runs the installer when it is turned \
on. Netflix and other unlisted sites use this CDM; the bridge CDM \
(luneos-widevine-bridge) handles only its own site list. arm64 only: 32-bit arm \
has no Google Chrome Linux build."
AUTHOR = "Herman van Hazendonk <github.com@herrie.org>"
HOMEPAGE = "https://github.com/webOS-ports/luneos-widevine-bridge"
SECTION = "webos/base"

# Our own installer code is free software; the CDM it fetches is Google's and is
# never shipped by LuneOS, so this recipe carries no proprietary bits and needs
# no LICENSE_FLAGS.
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "\
    file://widevine-install \
    file://widevine-install-ifenabled \
    file://widevine-install.service \
    file://widevine-install.path \
"

# The Android container pieces the bridge needs are halium-only, and arm64 is
# the only arch with a Google CDM to fetch; the script no-ops elsewhere, but
# gate the package to halium so it is not pulled into unrelated images.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:halium = "halium"

S = "${UNPACKDIR}"

inherit systemd
# Enable the path watcher (fires the service on opt-in) and the service (also
# retries at each online boot, covering an opt-in made while offline).
SYSTEMD_SERVICE:${PN} = "widevine-install.path widevine-install.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/widevine-install           ${D}${bindir}/widevine-install
    install -m 0755 ${UNPACKDIR}/widevine-install-ifenabled ${D}${bindir}/widevine-install-ifenabled

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/widevine-install.service ${D}${systemd_system_unitdir}/widevine-install.service
    install -m 0644 ${UNPACKDIR}/widevine-install.path    ${D}${systemd_system_unitdir}/widevine-install.path
}

# Tools the on-device script uses: curl (download), ar (split the .deb), tar + xz
# (unpack it). luna-send, used by the opt-in wrapper, is part of the base system.
RDEPENDS:${PN} = "curl tar xz binutils"
