SUMMARY = "Biometric service provider"
DESCRIPTION = "biomd is FuriLabs' biometrics daemon: one system service covering \
both fingerprint and face. The fingerprint backend talks to the Android HAL \
directly over binder via libgbinder - android.hardware.biometrics.fingerprint@2.1 \
and android.hardware.gatekeeper@1.0 - so unlike droidian-fpd it needs no \
libhybris and no Android-side biometry shim. The face backend drives libfart: \
it keeps the enrolled face encrypted and matches the frames a client hands it, \
capturing nothing itself. It exposes io.FuriOS.Biomd on the system bus, and also \
ships an fprintd provider and a PAM module as separate packages. On LuneOS \
webos-fingerprint-adapter and luneos-faced bridge it onto luna-service2."
HOMEPAGE = "https://github.com/FuriLabs/biomd"
SECTION = "webos/support"

# Note: GPL-2.0-only, and it links libfart which is GPL-2.0-only as well.
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=a8c8bc0f09b5c02c7332dcda700ec1dc"

# The face backend links libfart, and with it opencv and tensorflow-lite.
DEPENDS = "glib-2.0 libgbinder sqlite3 openssl gstreamer1.0 libpam libfart"

PV = "0.1+git"
# forky with FuriLabs/biomd PR #1 merged: overridable Makefile paths,
# WITH_FACE, free-form finger names, the AIDL backend and the Gatekeeper
# reconnect, which LuneOS used to carry as patches.
SRCREV = "d48b50bb2c072d4c9bdb79f8dcfdbd80ffd4f066"
SRC_URI = " \
    git://github.com/FuriLabs/biomd.git;protocol=https;branch=forky \
    file://biomd-hal-wait.sh \
    file://10-hal-wait.conf \
"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"

# libgbinder is MACHINE_ARCH, so this must not be shared as a tune-only package.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig systemd

# WITH_FACE=1 builds the FART face backend: biomd holds the enrolled face
# (encrypted under /var/lib/biomd) and runs the matcher, and luneos-faced feeds
# it frames from the camera service, as webos-fingerprint-adapter drives its
# fingerprint half. This links libfart, which brings opencv and tensorflow-lite
# into the image. It needs libtensorflow-lite's
# 002-link-abseil-log-and-check.patch, without which the link fails on
# undefined absl::log_internal symbols. WITH_FACE=0 would build a disabled face
# backend instead (ImplementationType 0, NotSupported).
EXTRA_OEMAKE = " \
    PREFIX=${prefix} \
    LIBDIR=${libdir} \
    WITH_FACE=1 \
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

    # The HAL is looked up once; wait for it to register first.
    install -D -m 0755 ${UNPACKDIR}/biomd-hal-wait.sh ${D}${libexecdir}/biomd-hal-wait.sh
    install -D -m 0644 ${UNPACKDIR}/10-hal-wait.conf \
        ${D}${systemd_system_unitdir}/biomd.service.d/10-hal-wait.conf
}

SYSTEMD_SERVICE:${PN} = "biomd.service"

# Split the optional consumers out: LuneOS drives biomd over its own D-Bus API
# (webos-fingerprint-adapter, and luneos-faced for the face), so the fprintd
# bridge, the PAM module, the user-session agent (face, GStreamer capture) and
# the Python client are not wanted.
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

FILES:${PN} += " \
    ${datadir}/dbus-1/system.d/io.FuriOS.Biomd.conf \
    ${libexecdir}/biomd-hal-wait.sh \
    ${systemd_system_unitdir}/biomd.service.d/10-hal-wait.conf \
"
RDEPENDS:${PN} += "libgbinder-tools"
# libfart has no SONAME, so the shlibs scan does not turn the link into a
# runtime dependency; its package also carries the models biomd loads from
# /usr/share/fart/models.
RDEPENDS:${PN} += "libfart"

# pam_biomd.so is a PAM module, not a shared library with a SONAME; it belongs
# in -pam rather than -dev, and has no dev symlink.
INSANE_SKIP:${PN}-pam = "dev-so"
