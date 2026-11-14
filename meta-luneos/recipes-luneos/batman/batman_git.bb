SUMMARY = "libbatman, the screen-on/off power tuning engine from FuriLabs batman"
DESCRIPTION = "FuriLabs batman's tuning engine as a shared library: CPU, GPU and \
devfreq governors, Android power HAL hints, vendor tuning knobs and process \
niceness. luna-batman drives it on LuneOS."
HOMEPAGE = "https://github.com/FuriLabs/batman"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 libgbinder libnl"

# libbatman branch of the fork, proposed upstream as FuriLabs/batman#2
SRC_URI = "git://github.com/Herrie82/batman.git;branch=libbatman;protocol=https"
SRCREV = "656a8a348fea6ff36f52d6f0431e0917c6b4bae7"
PV = "1.0+git"
PR = "r2"

inherit meson pkgconfig

# Library only: the FuriOS daemon needs upower, PulseAudio and wayland.
EXTRA_OEMESON = "-Ddaemon=false -Dnfcd_plugin=disabled"

COMPATIBLE_MACHINE = "(halium)"

do_install:append() {
    # luna-batman ships /etc/batman/config, which takes precedence over
    # /var/lib/batman/config; drop the FuriOS one so there is a single place
    # to look.
    rm -f ${D}/var/lib/batman/config
}

PACKAGES =+ "libbatman"
FILES:libbatman = "${libdir}/libbatman.so.* /var/lib/batman"
