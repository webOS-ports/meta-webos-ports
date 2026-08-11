SUMMARY = "PulseAudio policy module providing the audiod control socket"
DESCRIPTION = "module-palm-policy, extracted from LG's PulseAudio fork and built \
out-of-tree against upstream PulseAudio. audiod does not mix through libpulse; it \
drives PulseAudio over an abstract UNIX socket served by this module, so audiod is \
inert without it. Building it standalone lets LuneOS keep upstream PulseAudio 17.0 \
(and with it pulseaudio-modules-droid, which needs the 17.0 pulsecore ABI) instead \
of switching wholesale to LG's PulseAudio 15.0 fork."
HOMEPAGE = "https://github.com/webosose/pulseaudio-webos"
SECTION = "multimedia"

LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = "pulseaudio pulseaudio-pulsecore-private-headers alsa-lib"

# The module resolves pa_* against the daemon that dlopen()s it, so it must be
# built against exactly the PulseAudio it will be loaded into.
RDEPENDS:${PN} = "pulseaudio-server"

PV = "15.0+git"
SRCREV = "872586e51216f733f38cdf27358860849a0d4095"

SRC_URI = "git://github.com/webosose/pulseaudio-webos.git;branch=master;protocol=https \
    file://0001-module-palm-policy-build-out-of-tree-against-upstrea.patch \
    file://Makefile \
"

S = "${WORKDIR}/git"

inherit pkgconfig

# PA_MAJORMINOR must match the running daemon or PulseAudio refuses to load the
# module. The Makefile derives it from the libpulse being built against, so a
# PulseAudio upgrade cannot silently produce an unloadable module.
EXTRA_OEMAKE = " \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    libdir='${libdir}' \
    includedir='${includedir}' \
"

do_configure[noexec] = "1"

do_compile() {
    oe_runmake -C ${S}/src/modules -f ${WORKDIR}/Makefile all
}

do_install() {
    oe_runmake -C ${S}/src/modules -f ${WORKDIR}/Makefile install DESTDIR=${D}
}

FILES:${PN} = "${libdir}/pulseaudio/modules/module-palm-policy.so"
FILES:${PN}-dev = "${includedir}/pulse"

# PulseAudio modules are plugins: they legitimately leave pa_* unresolved at link
# time and are never linked against directly.
INSANE_SKIP:${PN} += "dev-so"
