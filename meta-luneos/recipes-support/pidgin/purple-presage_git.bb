SUMMARY = "Signal protocol plug-in for libpurple"
DESCRIPTION = "libpurple prpl for Signal, backed by the presage Rust library. The Rust half is \
built as a staticlib and linked into the C plugin."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-synergy.inc
require purple-presage-crates.inc

DEPENDS = "pidgin glib-2.0 openssl sqlite3"

S = "${WORKDIR}/git/messaging/signal/plugin/purple-presage"
CARGO_SRC_DIR = "src/rust"

inherit cmake cargo

# The crate list in purple-presage-crates.inc is generated from src/rust/Cargo.lock and covers
# the 443 crates.io dependencies. Regenerate it after any Cargo.lock change:
#
#     bitbake -c update_crates purple-presage
#
# (cargo-update-recipe-crates.bbclass is already in this layer.)
inherit cargo-update-recipe-crates

# THE REMAINING BLOCKER, and it is not something the crate list solves: 24 of presage's
# dependencies are NOT on crates.io. They are signalapp/whisperfish forks pinned by tag or
# revision -- libsignal, boring, curve25519-dalek, SparsePostQuantumRatchet, libsignal-service-rs
# and their friends (the full list is at the bottom of purple-presage-crates.inc). crate:// only
# serves the registry, so each needs its own git SRC_URI entry plus a [patch] section in the
# cargo config pointing at the fetched checkout. Until that is written this recipe will fail in
# do_compile with cargo trying to reach github.
#
# Two more things that bit the ARM build and will bite here:
#   * the Rust half links libcrypto for SQLCipher, so openssl must be visible to the crate build
#     script, not just to the final C link;
#   * Signal is currently DISABLED on webOS -- libpresage aborts the whole transport on login,
#     from an uncaught "Couldn't find prpl" in Util::getProtocolInfo. That is a webOS transport
#     bug rather than a LuneOS one, but a green build here does not mean a working plugin.

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"
