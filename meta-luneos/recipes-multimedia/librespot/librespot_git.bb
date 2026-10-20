SUMMARY = "Open source Spotify Connect receiver"
DESCRIPTION = "librespot is an open source client library for Spotify, with a Spotify Connect \
receiver binary. On LuneOS it is the audio half of Spotify in Music+Spotify: luneos-synergy-music \
runs it as a Connect device and drives it through the Spotify Web API. Needs Spotify Premium."
HOMEPAGE = "https://github.com/librespot-org/librespot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=98b2b0c9a6081259c441045ca68b640f"

# 16 commits past the v0.8.0 tag (d36f9f19) on dev, and no release since. This is the exact commit
# the legacy webOS port of the daemon ran, unmodified; it also carries fixes v0.8.0 lacks, among
# them falling back to the next CDN URL on a non-206 reply and "--volume-ctrl fixed" working.
PV = "0.8.0+git"
SRC_URI = "git://github.com/librespot-org/librespot.git;protocol=https;branch=dev;destsuffix=${BB_GIT_DEFAULT_DESTSUFFIX}"
SRCREV = "9c7d75615fc093bdcbdb29adbce3fed38c531852"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"

inherit cargo cargo-update-recipe-crates pkgconfig

# The crates.io packages from Cargo.lock, as crate:// entries. Regenerate with
# `bitbake -c update_crates librespot` after moving SRCREV. Cargo.lock has no git dependencies.
# Required after SRC_URI is set, as it appends to it.
require ${BPN}-crates.inc

# The daemon (music/_core/service/librespot.go) runs it as
#
#   librespot --name <name> --backend subprocess --device "gst-launch-1.0 fdsrc fd=0 ! ... ! pulsesink"
#             --system-cache <dir> --bitrate 320 --dither none --autoplay off
#             --initial-volume 100 --volume-ctrl linear [--access-token <token>]
#
# so the build only needs what those flags reach:
#
#   * the subprocess backend is always compiled in, it has no feature. PCM goes to gst-launch on
#     stdin and from there to pulsesink, so no rodio/ALSA/PulseAudio backend is built: the default
#     rodio-backend would only pull in ALSA for a sink nothing uses.
#   * native-tls: openssl, which the image ships anyway, and the system CA store. The rustls
#     variants would build ring and carry a second CA list compiled into the binary.
#   * no zeroconf backend (default with-libmdns). Login is by --access-token, then the credentials
#     in --system-cache, and the daemon selects the device through the Web API, so nothing needs
#     LAN discovery. Without it librespot opens no unauthenticated listener on the network, and
#     exits at start-up when it has neither a token nor cached credentials; the daemon restarts it.
CARGO_BUILD_FLAGS += "--no-default-features --features native-tls"

DEPENDS += "openssl"

# native-tls verifies Spotify's certificates against the system store.
RDEPENDS:${PN} += "ca-certificates"
