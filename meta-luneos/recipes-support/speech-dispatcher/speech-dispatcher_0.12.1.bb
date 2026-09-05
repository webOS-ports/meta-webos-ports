SUMMARY = "Common interface to speech synthesis"
DESCRIPTION = "Speech Dispatcher provides a device independent layer for speech synthesis. \
Chromium's Linux text-to-speech backend (content/browser/speech/tts_linux.cc) talks to it through \
libspeechd, which it dlopens as libspeechd.so.2 at runtime rather than linking. Without this package \
the Web Speech API's speechSynthesis object exists but getVoices() returns an empty list, so pages \
that speak simply stay silent -- and html5test scores it as unsupported."
HOMEPAGE = "https://freebsoft.org/speechd"
SECTION = "libs"

# The server is GPL-2.0+; the client library applications link against is LGPL-2.1+.
LICENSE = "GPL-2.0-or-later AND LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING.GPL-2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "glib-2.0 dotconf libsndfile1 libtool"

SRC_URI = "https://github.com/brailcom/speechd/releases/download/${PV}/speech-dispatcher-${PV}.tar.gz \
           file://0001-modules-use-AM_LDFLAGS-instead-of-blanking-LDFLAGS.patch \
           file://luneos-speechd.service"
SRC_URI[sha256sum] = "b14a5238d287d2dcce4dd42bbd66ca65fa228e7e683708267f7b34036f7ba4b4"

# gettext is not optional here: configure runs AM_GNU_GETTEXT, NLS defaults to
# enabled, and it hard-fails with "msgfmt missing from the gettext package" when
# the tools are not in the sysroot. Inheriting gettext pulls gettext-native in and
# lets USE_NLS decide, rather than switching NLS off and dropping the translations.
inherit autotools pkgconfig systemd gettext

# Chromium only needs the client library, but a client with no server and no output module cannot
# actually speak, so the synthesiser is part of the default set. espeak (not espeak-ng) because that
# is what meta-oe carries; configure finds it with AC_CHECK_LIB([espeak], [espeak_Synth]).
PACKAGECONFIG ??= "espeak pulse alsa systemd"

PACKAGECONFIG[espeak] = "--with-espeak,--without-espeak,espeak"
# Not in any layer here, and configure would otherwise probe for it; say so explicitly so the result
# does not depend on what happens to be in the sysroot.
PACKAGECONFIG[espeak-ng] = "--with-espeak-ng,--without-espeak-ng,espeak-ng"
PACKAGECONFIG[pulse] = "--with-pulse,--without-pulse,pulseaudio"
PACKAGECONFIG[alsa] = "--with-alsa,--without-alsa,alsa-lib"
PACKAGECONFIG[libao] = "--with-libao,--without-libao,libao"
PACKAGECONFIG[flite] = "--with-flite,--without-flite,flite"
# Both of these default to "check" in configure.ac, i.e. they switch on whatever
# happens to be in the sysroot, which is the situation the espeak-ng note above
# is about. pipewire matters because meta-multimedia does carry it, so an
# unrelated dependency pulling libpipewire-0.3 in would silently add a pipewire
# output module and an undeclared dependency. oss keys off sys/soundcard.h, which
# is generally present. Neither is wanted: PulseAudio is the backend here.
PACKAGECONFIG[pipewire] = "--with-pipewire,--without-pipewire,pipewire"
PACKAGECONFIG[oss] = "--with-oss,--without-oss,"
# nas must be switched off explicitly rather than left to autodetect. Its probe is
# the only place in configure.ac with a hardcoded host path:
#
#   AC_CHECK_LIB([audio], [AuOpenServer], ..., [-L/usr/X11R6/lib -lXau])
#
# so even though NAS is not in any layer here and the check fails, running it is
# enough to put "library search path /usr/X11R6/lib is unsafe for cross-compilation"
# into config.log, and do_configure's QA greps for exactly that string and fails
# the task with [configure-unsafe].
PACKAGECONFIG[nas] = "--with-nas,--without-nas,"
# Both unit directories default to asking pkg-config for systemd's own variables, which is not
# reliable when cross-compiling, so pin them. The user units (speech-dispatcher.service/.socket, for
# socket activation) are only generated when libsystemd is found as well.
PACKAGECONFIG[systemd] = "--with-systemdsystemunitdir=${systemd_system_unitdir} --with-systemduserunitdir=${systemd_user_unitdir},--without-systemdsystemunitdir --without-systemduserunitdir,systemd"

# The python bindings pull a target python runtime in for no benefit here: Chromium speaks to the
# daemon over its own socket protocol, never through them.
EXTRA_OECONF = "--disable-python --disable-static"

# The proprietary synthesisers must be switched off explicitly. Their --with-*
# options take yes|no|shim, and configure.ac sets default_shim=shim whenever
# shared libraries are enabled, so leaving them alone does NOT mean "off": it
# means "build against a locally generated stub .so". The modules then get built
# and packaged against libraries that exist nowhere, and package QA fails with
#
#   sd_kali ... requires libKali.so()(64bit), but no providers found
#   sd_voxin ... requires libvoxin.so()(64bit), but no providers found
#   sd_baratinoo ... requires libbaratinoo.so()(64bit), but no providers found
#
# ivona and pico only ever probe with AC_CHECK_LIB and so stay off by themselves,
# but they are pinned here too so the module set does not depend on what happens
# to be in the sysroot.
EXTRA_OECONF += "--without-ibmtts --without-voxin --without-baratinoo --without-kali \
                 --without-ivona --without-pico"

# PulseAudio is what LuneOS actually runs, so ask for it first instead of letting the daemon take
# whichever backend it happens to discover.
EXTRA_OECONF += "${@bb.utils.contains('PACKAGECONFIG', 'pulse', '--with-default-audio-method=pulse', '', d)}"

# Chromium cannot use the autospawn path, so the daemon has to be running already.
#
# libspeechd connects to $XDG_RUNTIME_DIR/speech-dispatcher/speechd.sock and, finding nothing there,
# spawns a daemon through g_spawn. That spawn closes file descriptors Chromium owns, so
# base::CrashOnFdOwnershipViolation() aborts browser_shell the first time a page touches
# speechSynthesis - and Cloudflare's bot detection does exactly that, so it showed up as a challenge
# loop rather than as anything to do with speech. Reproduced and confirmed on tissot: with a daemon
# already listening the same call is harmless.
#
# luneos-speechd.service runs it as wam:compositor on /tmp/xdg, matching what run_browser_shell sets,
# so libspeechd connects instead of spawning. speech-dispatcherd.service stays installed but
# unmanaged; it runs as root with a different socket and browser_shell would not find it.
SYSTEMD_SERVICE:${PN} = "luneos-speechd.service"
SYSTEMD_AUTO_ENABLE = "enable"

# Output modules are dlopened helper binaries under libdir, and the daemon reads its configuration
# from sysconfdir; both belong to the main package rather than to -dev.
FILES:${PN} += "${libexecdir}/speech-dispatcher-modules ${libdir}/speech-dispatcher-modules \
                ${datadir}/speech-dispatcher ${sysconfdir}/speech-dispatcher \
                ${systemd_user_unitdir}"
# Listed explicitly because it is no longer in SYSTEMD_SERVICE (see above), which is what would
# otherwise have packaged it. Shipped but never enabled: it runs as root on a socket browser_shell
# does not look at, so it is there for anyone who wants it rather than for us.
FILES:${PN} += "${systemd_system_unitdir}/speech-dispatcherd.service"

# libspeechd itself is what Chromium dlopens (soname libspeechd.so.2, from version-info 8:0:6).
PACKAGES =+ "libspeechd"
FILES:libspeechd = "${libdir}/libspeechd.so.*"
RDEPENDS:${PN} += "libspeechd"

# The shipped speechd.conf has every AddModule line commented out, so the daemon comes up with no
# output modules at all and getVoices() returns an empty list however it is reached - which is the
# very thing --enable-speech-dispatcher was turned on to fix. sd_espeak and espeak are both built and
# installed by this recipe, so enable that one.
do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/luneos-speechd.service ${D}${systemd_system_unitdir}/

    cat >> ${D}${sysconfdir}/speech-dispatcher/speechd.conf <<EOF

# LuneOS: enable an output module, or the daemon exposes no voices.
AddModule "espeak"  "sd_espeak"  "espeak.conf"
DefaultModule espeak
EOF
}
