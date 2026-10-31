SUMMARY = "Music+Spotify streaming backend, sign-in app and provider account types"
DESCRIPTION = "spotify-webos-service, the Go daemon behind Music+Spotify's streaming providers \
(Spotify, Apple Music, Deezer, Tidal, Qobuz, SoundCloud, Jamendo, the Internet Archive and \
YouTube), which the player and the sign-in UI reach over localhost HTTP (127.0.0.1:8730) -- it is \
not a luna-service2 service -- plus org.webosports.app.musicauth, the validator app every \
provider signs in through, and one account-type package per provider. The player itself is \
org.webosports.app.music."
SECTION = "webos/services"

require luneos-synergy-connectors.inc

# music/ is the repository's own code. The Go modules the daemon links (music/_core/service/go.sum)
# carry their own licences, which this does not list.
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI += "file://spotify-webos-service.service"

# 0.9.2 is musicauth's appinfo.json version at that SRCREV. The player is its own recipe
# from its own repository now (org.webosports.app.music, 1.0.0+git).
PV = "0.9.2+git"

S = "${LSC_SRCDIR}"

# `go`, not `go-mod`, for the reason purple-combined_git.bb gives: go-mod chdirs do_compile into
# ${B}/src/${GO_WORKDIR}, which exists only under the GOPATH relocation the destsuffix in
# luneos-synergy-connectors.inc turns off. The module is music/_core/service, under its own
# module path.
inherit go
GO_IMPORT = "github.com/herrie/spotify-webos-service"

inherit systemd
inherit webos_filesystem_paths
inherit webos_system_bus
inherit webos_app
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

# Go modules come through GOPROXY in do_compile, with that task granted network access -- what
# purple-combined and the other Go recipes in this layer set do.
export GOMODCACHE = "${B}/.mod"
do_compile[cleandirs] += "${B}/.mod"
export GOPROXY = "https://proxy.golang.org,direct"
do_compile[network] = "1"

# Build with the go-cross this layer provides, whatever its version. go.mod says "go 1.25.0" with
# no toolchain line, so any go >= 1.25 builds it; GOTOOLCHAIN=local makes a future go.mod bump
# past the layer's go fail here instead of quietly downloading a toolchain through GOPROXY.
export GOTOOLCHAIN = "local"

do_compile() {
    cd ${S}/music/_core/service
    # GOBUILDFLAGS is go.bbclass's set for linking an executable for the target (-trimpath, the
    # external linker and its flags, -buildmode=pie). No -ldflags="-s -w": OE packaging strips
    # the binary and keeps its debug info in -dbg.
    ${GO} build ${GOBUILDFLAGS} -modcacherw -o ${B}/spotify-webos-service .
}

# The Spotify and Jamendo client ids are the builder's own and never belong in this layer: set
# SPOTIFY_CLIENT_ID / JAMENDO_CLIENT_ID in a local.conf (or a file it includes, kept out of any
# repository). When set, they are installed as /etc/spotify-webos/spotify-client-id and
# .../jamendo-clientid, the fallback the daemon reads after /media/cryptofs/spotify-webos/; unset,
# nothing is installed and the ids have to be provisioned on the device. They become part of this
# recipe's task hashes, so an sstate cache built with them is as private as they are.
SPOTIFY_CLIENT_ID ?= ""
JAMENDO_CLIENT_ID ?= ""

# One account-type package per provider. music/youtube/ carries no template yet.
MUSIC_PROVIDERS = "apple archive deezer jamendo qobuz soundcloud spotify tidal"

do_install() {
    # The daemon, and the unit that starts it at boot. On legacy webOS this was an upstart job
    # (start on started LunaSysMgr, respawn); LuneOS has no upstart and the repository ships no
    # systemd unit, so the unit is carried here.
    install -d ${D}${sbindir}
    install -m 0755 ${B}/spotify-webos-service ${D}${sbindir}/spotify-webos-service

    install -d ${D}${systemd_system_unitdir}
    sed -e 's|@SBINDIR@|${sbindir}|g' -e 's|@BASE_BINDIR@|${base_bindir}|g' \
        ${UNPACKDIR}/spotify-webos-service.service \
        > ${D}${systemd_system_unitdir}/spotify-webos-service.service
    chmod 0644 ${D}${systemd_system_unitdir}/spotify-webos-service.service

    # Client ids, only when the builder set them (see SPOTIFY_CLIENT_ID above). bitbake expands
    # them into this task's run script under WORKDIR/temp, which cannot be avoided; nothing here
    # prints them, so they stay out of the task log.
    if [ -n "${SPOTIFY_CLIENT_ID}${JAMENDO_CLIENT_ID}" ]; then
        install -d -m 0755 ${D}${sysconfdir}/spotify-webos
    fi
    if [ -n "${SPOTIFY_CLIENT_ID}" ]; then
        printf '%s' "${SPOTIFY_CLIENT_ID}" > ${D}${sysconfdir}/spotify-webos/spotify-client-id
        chmod 0600 ${D}${sysconfdir}/spotify-webos/spotify-client-id
    fi
    if [ -n "${JAMENDO_CLIENT_ID}" ]; then
        printf '%s' "${JAMENDO_CLIENT_ID}" > ${D}${sysconfdir}/spotify-webos/jamendo-clientid
        chmod 0600 ${D}${sysconfdir}/spotify-webos/jamendo-clientid
    fi

    # The validator app every provider template names in validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -R --no-preserve=ownership ${S}/music/_core/apps/org.webosports.app.musicauth ${D}${webos_applicationsdir}/

    # Provider account templates.
    install -d ${D}${webos_accttemplatesdir}
    for p in ${MUSIC_PROVIDERS}; do
        cp -R --no-preserve=ownership ${S}/music/$p/account/org.webosports.app.music.$p ${D}${webos_accttemplatesdir}/
    done

    # The daemon reads and writes provider credentials in the Accounts service with
    #     luna-send -n 1 -a org.webosports.app.music.service palm://com.palm.service.accounts/...
    # (creds.go), i.e. luna-send acting for that id; the templates name it in their read and
    # write permissions. Give that id the Accounts group it needs, and mediacontroller's
    # session calls: from connectors commit c99129b on, the daemon holds a media session of
    # com.webos.service.mediacontroller for librespot (registerMediaSession and the set*
    # calls, music/_core/service/mediasession.go), so that it is paused when another player
    # starts or a call comes in, and takes the headset's keys.
    #
    # Deliberately no role for that id: it would have to claim exeName /usr/bin/luna-send, which
    # the stock luna-send role owns already, and two roles on one exeName once broke every
    # luna-send call on a device.
    # (Indented, and unindented by sed: a '}' in column 0 would end this shell function.)
    install -d ${D}${webos_sysbus_permissionsdir}
    sed 's/^    //' > ${D}${webos_sysbus_permissionsdir}/org.webosports.app.music.service.perm.json <<EOF
    {
        "org.webosports.app.music.service": ["accountsservice.operation", "mediacontroller.query"]
    }
EOF
}

SYSTEMD_SERVICE:${PN} = "spotify-webos-service.service"

# The template packages keep the names they had when org.webosports.app.music built them, so an
# installed device upgrades in place.
MUSIC_ACCOUNT_PACKAGES = "${@' '.join('org.webosports.app.music-account-' + p for p in d.getVar('MUSIC_PROVIDERS').split())}"

python __anonymous() {
    acctdir = d.getVar('webos_accttemplatesdir')
    pn = d.getVar('PN')
    for p in d.getVar('MUSIC_PROVIDERS').split():
        pkg = 'org.webosports.app.music-account-%s' % p
        d.setVar('FILES:' + pkg, '%s/org.webosports.app.music.%s' % (acctdir, p))
        d.setVar('RDEPENDS:' + pkg, pn)
        d.setVar('SUMMARY:' + pkg, '%s account type for Music+Spotify' % p)
}

PACKAGES =+ "${MUSIC_ACCOUNT_PACKAGES}"
RRECOMMENDS:${PN} += "${MUSIC_ACCOUNT_PACKAGES}"

# The template packages, not the base: the Accounts service has to be restarted to see a template
# come or go (synergy-accounts-reload.inc). The base package keeps its systemd postinst alone.
require synergy-accounts-reload.inc
SYNERGY_ACCOUNTS_RELOAD_PACKAGES = "${MUSIC_ACCOUNT_PACKAGES}"

FILES:${PN} += "${webos_applicationsdir} ${systemd_system_unitdir} ${sysconfdir}/spotify-webos"

# What the daemon runs, beyond itself:
#   - luna-send (luna-service2) for the Accounts calls;
#   - curl, which feeds every stream-URL provider into the pipeline (curl -sL <url> | gst);
#   - gst-launch-1.0 with: fdsrc and queue (gstreamer1.0, coreelements), decodebin (playback),
#     typefind (decodebin cannot pick a decoder without it), audioconvert, audioresample, and
#     pulsesink for media.role=music output;
#   - decoders for what the providers serve: MP3 (SoundCloud, Archive, Jamendo; mpg123 +
#     mpegaudioparse from audioparsers), FLAC (Qobuz, Tidal; flac), AAC in MP4 (Apple Music,
#     Tidal, YouTube; isomp4 + avdec_aac from libav, as there is no faad/fdkaac here), Opus and
#     Vorbis in Ogg/WebM (YouTube, Archive; ogg, vorbis, matroska, and opus - plugins-base is
#     built with its opus PACKAGECONFIG here, so opusdec comes from
#     gstreamer1.0-plugins-base-opus rather than libav's avdec_opus), and HLS for the SoundCloud
#     fallback (hls).
# librespot (recipes-multimedia/librespot) plays Spotify. It is recommended rather than
# required: without it the daemon disables Spotify playback and everything else works. The
# daemon runs /usr/bin/librespot from connectors commit 3e01288 on; before that it only looked
# in /media/cryptofs/spotify-webos.
RRECOMMENDS:${PN} += "librespot"

RDEPENDS:${PN} += " \
    curl \
    luna-service2 \
    enyo-1.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-base-ogg \
    gstreamer1.0-plugins-base-opus \
    gstreamer1.0-plugins-base-playback \
    gstreamer1.0-plugins-base-typefindfunctions \
    gstreamer1.0-plugins-base-vorbis \
    gstreamer1.0-plugins-good-audioparsers \
    gstreamer1.0-plugins-good-flac \
    gstreamer1.0-plugins-good-isomp4 \
    gstreamer1.0-plugins-good-matroska \
    gstreamer1.0-plugins-good-mpg123 \
    gstreamer1.0-plugins-good-pulseaudio \
    gstreamer1.0-plugins-bad-hls \
    gstreamer1.0-libav \
"
