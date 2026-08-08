SUMMARY = "Signal protocol plug-in for libpurple"
DESCRIPTION = "libpurple prpl for Signal, backed by the presage Rust library. The Rust half is \
built as a staticlib and linked into the C plugin."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-synergy.inc
require purple-presage-crates.inc
require purple-presage-git-deps.inc

DEPENDS = "pidgin glib-2.0 openssl sqlite3"

S = "${WORKDIR}/git/messaging/signal/plugin/purple-presage"
CARGO_SRC_DIR = "src/rust"

inherit cmake cargo cargo-update-recipe-crates

# presage's dependencies arrive from two places, because they come from two:
#
#   purple-presage-crates.inc     443 crates.io packages, as crate:// entries. Regenerate with
#                                 `bitbake -c update_crates purple-presage`.
#   purple-presage-git-deps.inc   the 24 that are signalapp/whisperfish forks and are NOT on
#                                 crates.io. Fetched as 8 git repos and wired in by source
#                                 replacement -- see that file for why EXTRA_OECARGO_PATHS does
#                                 not work here.
#
# Both pin revisions from src/rust/Cargo.lock, so the build is reproducible and needs no network
# during do_compile, which cargo.bbclass forbids anyway (it builds --frozen).

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"

# The Rust half links libcrypto for SQLCipher, so openssl has to be visible to the crate build
# scripts and not only to the final C link. boring-sys additionally builds BoringSSL from its own
# vendored copy, which is the slowest part of this recipe by a wide margin.

# NOTE, unrelated to whether this builds: Signal is currently DISABLED on webOS. libpresage
# aborts the whole imlibpurple transport on login, from an uncaught "Couldn't find prpl" raised in
# Util::getProtocolInfo. That is a webOS transport bug rather than a LuneOS one, but a green build
# here does not mean a working plugin.
