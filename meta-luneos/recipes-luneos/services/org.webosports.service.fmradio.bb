# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "FM radio service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# fmradiod drives the receiver itself - MediaTek's /dev/fm, Qualcomm's
# radio-iris /dev/radio0, or Qualcomm's WCN3990-class FM through the vendor's
# IFmHci HIDL service (libgbinder) - and plays it by loading module-droid-fm
# (from pulseaudio-modules-droid) into PulseAudio while the radio is on.
DEPENDS = "luna-service2 glib-2.0 libpbnjson pulseaudio libgbinder"

PV = "1.0.0-1+git"
PE = "2"

# Pinned, never AUTOREV: see org.webosports.service.torch.bb.
SRCREV = "ac245fde7a4203e034338478f6846ded71f4b6cf"
WEBOS_GIT_PARAM_BRANCH = "main"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_daemon
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# As for torchd: webos_build_daemon() installs the unit from files/launch, and
# systemd.bbclass is only here to enable it.
SYSTEMD_SERVICE:${PN} = "fmradiod.service"

FILES:${PN} += "${sysconfdir}/systemd/system/fmradiod.service"
FILES:${PN} += "${webos_sysbus_datadir}"
