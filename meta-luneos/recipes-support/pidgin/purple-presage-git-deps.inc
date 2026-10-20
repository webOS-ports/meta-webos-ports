# Git-sourced Rust dependencies for purple-presage, generated from the `source = "git+..."`
# entries of src/rust/Cargo.lock. Regenerate when the lock changes: one SRC_URI entry and SRCREV
# per repository, one PRESAGE_GIT_CRATES entry per crate. The crates.io half is in
# purple-presage-crates.inc.
#
# 15 of presage's dependencies are signalapp/whisperfish forks that are not on crates.io, so
# crate:// cannot serve them. cargo.bbclass builds with --frozen (no network), so they are fetched
# here, one git repo each, pinned to the revision Cargo.lock records, and handed to cargo as
# [patch."<repo>"] path overrides by cargo_common_do_patch_paths in purple-presage_git.bb.
#
# PRESAGE_GIT_CRATES lists <crate>=<repo name>:<path inside the repo> for that function. Paths
# rather than a vendor directory, because the libsignal crates
# inherit version/edition/dependencies from their workspace root (`.workspace = true`) and only
# resolve inside the full checkout.

SRC_URI += " \
    git://github.com/kamadorueda/rust-mime-sniffer;protocol=https;nobranch=1;name=rust-mime-sniffer;destsuffix=git-deps/rust-mime-sniffer \
    git://github.com/signalapp/SparsePostQuantumRatchet.git;protocol=https;nobranch=1;name=sparsepostquantumratchet;destsuffix=git-deps/sparsepostquantumratchet \
    git://github.com/signalapp/libsignal;protocol=https;nobranch=1;name=libsignal;destsuffix=git-deps/libsignal \
    git://github.com/whisperfish/libsignal-service-rs;protocol=https;nobranch=1;name=libsignal-service-rs;destsuffix=git-deps/libsignal-service-rs \
    git://github.com/whisperfish/presage;protocol=https;nobranch=1;name=presage;destsuffix=git-deps/presage \
    git://github.com/whisperfish/rusqlite;protocol=https;nobranch=1;name=rusqlite;destsuffix=git-deps/rusqlite \
"

SRCREV_rust-mime-sniffer = "0053c4330ca159db118e57435ea781bdf46158ed"
SRCREV_sparsepostquantumratchet = "fd320484dcec89004021e6fdc7481825f5f261fa"
SRCREV_libsignal = "439f035d9cfd7efe5435cbea0294591c9ed17232"
SRCREV_libsignal-service-rs = "bb43e8125e0c9e7a1ce20d57fb330f6fbd9257eb"
SRCREV_presage = "f74b96e0fb9099ce8be2b28b7ca9d11f78a6faea"
SRCREV_rusqlite = "2a42b3354c9194700d08aa070f70a131a470e7dc"

SRCREV_FORMAT .= "_rust-mime-sniffer_sparsepostquantumratchet_libsignal_libsignal-service-rs_presage_rusqlite"

# <crate>=<repo>:<path in repo>; also used to name the [patch] source by its repo URL.
PRESAGE_GIT_CRATES = " \
    mime-sniffer=rust-mime-sniffer: \
    spqr=sparsepostquantumratchet: \
    libsignal-account-keys=libsignal:rust/account-keys \
    libsignal-core=libsignal:rust/core \
    libsignal-debug=libsignal:rust/debug \
    libsignal-protocol=libsignal:rust/protocol \
    poksho=libsignal:rust/poksho \
    signal-crypto=libsignal:rust/crypto \
    usernames=libsignal:rust/usernames \
    zkcredential=libsignal:rust/zkcredential \
    zkgroup=libsignal:rust/zkgroup \
    libsignal-service=libsignal-service-rs: \
    presage=presage:presage \
    presage-store-sqlite=presage:presage-store-sqlite \
    libsqlite3-sys=rusqlite:libsqlite3-sys \
"

# repo name -> URL as Cargo.lock spells it (the [patch] key)
PRESAGE_GIT_URL[rust-mime-sniffer] = "https://github.com/kamadorueda/rust-mime-sniffer"
PRESAGE_GIT_URL[sparsepostquantumratchet] = "https://github.com/signalapp/SparsePostQuantumRatchet.git"
PRESAGE_GIT_URL[libsignal] = "https://github.com/signalapp/libsignal"
PRESAGE_GIT_URL[libsignal-service-rs] = "https://github.com/whisperfish/libsignal-service-rs"
PRESAGE_GIT_URL[presage] = "https://github.com/whisperfish/presage"
PRESAGE_GIT_URL[rusqlite] = "https://github.com/whisperfish/rusqlite"
