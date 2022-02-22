SECTION = "base"
DESCRIPTION = "IIO accelerometer sensor to input device proxy."

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

DEPENDS = "udev systemd autoconf-archive-native gtk-doc-native glib-2.0-native libgudev"

inherit autotools gettext pkgconfig gtk-doc systemd

SRC_URI = " \
    git://github.com/hadess/iio-sensor-proxy.git;branch=master;protocol=https \
    file://0001-Disable-gtk-doc.patch \
"
SRCREV = "d3d3c79096188e14344d926e6efb475da34ff293"
S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-gtk-doc --disable-gtk-tests"

FILES:${PN} += "${systemd_unitdir}"
