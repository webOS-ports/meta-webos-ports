SUMMARY = "Simple service to retrieve, set and get notified about android properties."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;beginline=86;endline=105;md5=649a1e756b7d4ea0e24d38c2d5a572ee"

DEPENDS += "luna-service2 libhybris libpbnjson luna-prefs virtual/android-headers"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"


# The source has carried a systemd unit at files/systemd/ all along, but
# webos_systemd only picks it up when LUNEOS_SYSTEMD_SERVICE names it. Without
# that the package shipped the luna-service2 files and the binary but no unit,
# and the bus file declares Type=static - which tells ls-hubd not to launch the
# service on demand but to wait for something else to start it. Nothing did:
#
#   ls-hubd LSHUB_NO_SERVICE: Failed Connecting to Service err_code: -3,
#           service_name: "com.android.properties", static
#   ls-hubd LS_SOCK: Broken pipe, APP_ID: "org.webosports.app.settings.deviceinfo"
#
# so Device Info showed no Android version at all. Every other service in
# services.d that gets launched on demand is Type=dynamic; this one is not.
LUNEOS_SYSTEMD_SERVICE = "android-property-service.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/android-property-service.service.d
    install -m 0644 ${UNPACKDIR}/after-android-system.conf \
        ${D}${systemd_unitdir}/system/android-property-service.service.d
}

PV = "0.1.0-2+git"
SRCREV = "6461ebfbffc7ba17d2b560f62bb9ad7b65bb69f5"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRC_URI += " \
    file://after-android-system.conf \
"
