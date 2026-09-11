SUMMARY = "Biometric service provider"
DESCRIPTION = "biomd is FuriLabs' biometrics daemon: one system service covering \
both fingerprint and face. The fingerprint backend talks to the Android HAL \
directly over binder via libgbinder - android.hardware.biometrics.fingerprint@2.1 \
and android.hardware.gatekeeper@1.0 - so unlike droidian-fpd it needs no \
libhybris and no Android-side biometry shim. The face backend drives libfart. \
It exposes io.FuriOS.Biomd on the system bus, and also ships an fprintd provider \
and a PAM module as separate packages."
HOMEPAGE = "https://github.com/FuriLabs/biomd"
SECTION = "webos/support"

# Note: GPL-2.0-only, and it links libfart which is GPL-2.0-only as well.
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=a8c8bc0f09b5c02c7332dcda700ec1dc"

# libfart is here even though WITH_FACE=0 below drops it from the daemon's
# link: biomd-session's session_face.c includes <fart/fart_enums.h>, so the
# headers are needed to compile it. It is a build-time dependency only -
# nothing links libfart in this configuration, so neither opencv nor
# tensorflow-lite ends up in the image.
DEPENDS = "glib-2.0 libgbinder sqlite3 openssl gstreamer1.0 libpam libfart"

PV = "0.1+git"
SRCREV = "d6ba5fedc7e9d2aa44a9207ced4008df13b30a72"
SRC_URI = " \
    git://github.com/FuriLabs/biomd.git;protocol=https;branch=forky \
    file://0001-Makefile-make-toolchain-flags-and-install-paths-over.patch \
    file://0002-Makefile-allow-building-without-the-face-backend.patch \
    file://0003-fingerprint-accept-free-form-finger-names.patch \
"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"

# libgbinder is MACHINE_ARCH, so this must not be shared as a tune-only package.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig systemd

# WITH_FACE=0: fingerprint only for now, so we do not drag libfart - and with it
# opencv and tensorflow-lite - into the image for a subsystem nothing calls yet.
# It is not merely weight: libtensorflow-lite.so as built here does not record
# its dependency on abseil, so linking an executable against it fails on
# undefined absl::log_internal symbols. That has to be fixed in the
# libtensorflow-lite recipe before the face half can work at all; flip this to 1
# and add libfart back to DEPENDS once it is.
EXTRA_OEMAKE = " \
    PREFIX=${prefix} \
    LIBDIR=${libdir} \
    WITH_FACE=0 \
"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install DESTDIR=${D}

    # The Makefile does not install the system unit - upstream ships it from
    # debian/ via dh_installsystemd - so place it here under its final name.
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/debian/biomd.biomd.service ${D}${systemd_system_unitdir}/biomd.service

    # Upstream orders the unit after lxc@android.service, which is Droidian's
    # Android container. The equivalent here is android-system.service, and
    # without a running Android side there is no hwservicemanager to find the
    # fingerprint HAL in.
    sed -i 's/^After=lxc@android\.service$/After=android-system.service/' \
        ${D}${systemd_system_unitdir}/biomd.service
}

SYSTEMD_SERVICE:${PN} = "biomd.service"

# Split the optional consumers out: LuneOS drives biomd over its own D-Bus API,
# so the fprintd bridge, the PAM module, the user-session agent (face) and the
# Python client are not wanted in a minimal fingerprint install.
PACKAGES =+ "${PN}-fprintd ${PN}-pam ${PN}-session ${PN}-client"

FILES:${PN}-fprintd = " \
    ${libexecdir}/biomd-fprintd \
    ${systemd_system_unitdir}/fprintd.service.d/10-biomd.conf \
"
RDEPENDS:${PN}-fprintd += "${PN}"

FILES:${PN}-pam = "${libdir}/security/pam_biomd.so"
RDEPENDS:${PN}-pam += "${PN}"

FILES:${PN}-session = " \
    ${libexecdir}/biomd-session \
    ${systemd_user_unitdir}/biomd-session.service \
"
RDEPENDS:${PN}-session += "${PN}"

FILES:${PN}-client = "${bindir}/biomdctl"
RDEPENDS:${PN}-client += "${PN} python3-core python3-dbus python3-pygobject"

FILES:${PN} += "${datadir}/dbus-1/system.d/io.FuriOS.Biomd.conf"

# pam_biomd.so is a PAM module, not a shared library with a SONAME; it belongs
# in -pam rather than -dev, and has no dev symlink.
INSANE_SKIP:${PN}-pam = "dev-so"
