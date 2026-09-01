# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "C-based eUICC LPA (Local Profile Assistant) for SGP.22 eSIM profile management"
HOMEPAGE = "https://github.com/estkme-group/lpac"
LICENSE = "AGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSES/AGPL-3.0-only.txt;md5=216109e2c1c3eaf57de2bfc18c11c9f8"
SECTION = "webos/support"

DEPENDS = "glib-2.0"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/estkme-group/lpac.git;branch=main;protocol=https \
           file://0001-driver-apdu-add-ofono-backend.patch \
"

PV = "2.3.0"
SRCREV = "c2fcf5e4b21c712d54e35a11da2ad9ad134fb821"

inherit cmake pkgconfig

# Backends are compiled into the lpac binary itself: LPAC_DYNAMIC_DRIVERS is OFF by
# default, so there is no plugin directory to package or configure. Pick the transport at
# runtime with $LPAC_APDU (e.g. LPAC_APDU=gbinder_hidl, LPAC_APDU=at).
#
# gbinder only makes sense on Halium, where libgbinder and an Android RIL exist; on
# mainline machines (PinePhone, PineTab2, ...) the AT backend against the modem's AT port
# is the usable one. PCSC is off everywhere: no pcscd in LuneOS and no reason to pull it in.
PACKAGECONFIG ??= "at curl ofono"
PACKAGECONFIG:append:halium = " gbinder"

PACKAGECONFIG[ofono]   = "-DLPAC_WITH_APDU_OFONO=ON,-DLPAC_WITH_APDU_OFONO=OFF,glib-2.0"
PACKAGECONFIG[gbinder] = "-DLPAC_WITH_APDU_GBINDER=ON,-DLPAC_WITH_APDU_GBINDER=OFF,libgbinder"
PACKAGECONFIG[at]      = "-DLPAC_WITH_APDU_AT=ON,-DLPAC_WITH_APDU_AT=OFF,"
PACKAGECONFIG[pcsc]    = "-DLPAC_WITH_APDU_PCSC=ON,-DLPAC_WITH_APDU_PCSC=OFF,pcsc-lite"
PACKAGECONFIG[mbim]    = "-DLPAC_WITH_APDU_MBIM=ON,-DLPAC_WITH_APDU_MBIM=OFF,libmbim"
PACKAGECONFIG[qmi]     = "-DLPAC_WITH_APDU_QMI=ON,-DLPAC_WITH_APDU_QMI=OFF,libqmi"
PACKAGECONFIG[curl]    = "-DLPAC_WITH_HTTP_CURL=ON,-DLPAC_WITH_HTTP_CURL=OFF,curl"

# lpac only installs the binary (install(TARGETS lpac RUNTIME ...) in src/CMakeLists.txt).
FILES:${PN} = "${bindir}/lpac"

# jq is not required, but every lpac invocation in the notes pipes through it.
RRECOMMENDS:${PN} += "jq"
