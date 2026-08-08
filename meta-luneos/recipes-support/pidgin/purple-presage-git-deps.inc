# Git-sourced Rust dependencies for purple-presage. Auto-generated; regenerate if Cargo.lock
# changes. The crates.io half is in purple-presage-crates.inc.
#
# 24 of presage's dependencies are signalapp/whisperfish forks that are not on crates.io, so
# crate:// cannot serve them. cargo.bbclass builds with --frozen (no network), so they have to be
# present locally before do_compile.
#
# The mechanism is SOURCE REPLACEMENT, not EXTRA_OECARGO_PATHS: cargo's `paths` override only
# applies to path and registry dependencies and silently does nothing for git ones. Replacement
# needs a vendor directory holding one directory per CRATE (not per repo -- libsignal alone
# provides 14 of the 24), each with a .cargo-checksum.json. do_configure:prepend below assembles
# that from the fetched checkouts.
#
# Revisions come from Cargo.lock, so this stays reproducible.

SRC_URI += " \
    git://github.com/kamadorueda/rust-mime-sniffer;protocol=https;nobranch=1;name=rust-mime-sniffer;destsuffix=git-deps/rust-mime-sniffer \
    git://github.com/signalapp/SparsePostQuantumRatchet.git;protocol=https;nobranch=1;name=sparsepostquantumratchet;destsuffix=git-deps/sparsepostquantumratchet \
    git://github.com/signalapp/boring;protocol=https;nobranch=1;name=boring;destsuffix=git-deps/boring \
    git://github.com/signalapp/curve25519-dalek;protocol=https;nobranch=1;name=curve25519-dalek;destsuffix=git-deps/curve25519-dalek \
    git://github.com/signalapp/libsignal;protocol=https;nobranch=1;name=libsignal;destsuffix=git-deps/libsignal \
    git://github.com/whisperfish/libsignal-service-rs;protocol=https;nobranch=1;name=libsignal-service-rs;destsuffix=git-deps/libsignal-service-rs \
    git://github.com/whisperfish/presage;protocol=https;nobranch=1;name=presage;destsuffix=git-deps/presage \
    git://github.com/whisperfish/rusqlite;protocol=https;nobranch=1;name=rusqlite;destsuffix=git-deps/rusqlite \
"

SRCREV_rust-mime-sniffer = "0053c4330ca159db118e57435ea781bdf46158ed"
SRCREV_sparsepostquantumratchet = "f2589fef855c10f39d72634dab3d14654dd410bf"
SRCREV_boring = "2c157897feea0c54f5eca93407a157fa90703b5f"
SRCREV_curve25519-dalek = "7c6d34756355a3566a704da84dce7b1c039a6572"
SRCREV_libsignal = "46d867c986f66201e34e7ae20ce423eec742bf3f"
SRCREV_libsignal-service-rs = "fd481655d481388ebfe3a1fdb85a668b4876d592"
SRCREV_presage = "63482efd0cbdc0780baf0650517c7d55f1cac05d"
SRCREV_rusqlite = "2a42b3354c9194700d08aa070f70a131a470e7dc"

SRCREV_FORMAT = "default_rust-mime-sniffer_sparsepostquantumratchet_boring_curve25519-dalek_libsignal_libsignal-service-rs_presage_rusqlite"

do_configure:prepend() {
    # One vendor directory per crate, plus the checksum stub cargo requires for a
    # replaced source (empty 'files' is what `cargo vendor` itself writes for git deps).
    mkdir -p ${CARGO_VENDORING_DIRECTORY}
    cp -a ${WORKDIR}/git-deps/rust-mime-sniffer ${CARGO_VENDORING_DIRECTORY}/mime-sniffer
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/mime-sniffer/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/sparsepostquantumratchet ${CARGO_VENDORING_DIRECTORY}/spqr
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/spqr/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/boring/boring ${CARGO_VENDORING_DIRECTORY}/boring
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/boring/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/boring/boring-sys ${CARGO_VENDORING_DIRECTORY}/boring-sys
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/boring-sys/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/boring/tokio-boring ${CARGO_VENDORING_DIRECTORY}/tokio-boring
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/tokio-boring/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/curve25519-dalek/curve25519-dalek ${CARGO_VENDORING_DIRECTORY}/curve25519-dalek
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/curve25519-dalek/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/curve25519-dalek/curve25519-dalek-derive ${CARGO_VENDORING_DIRECTORY}/curve25519-dalek-derive
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/curve25519-dalek-derive/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/attest ${CARGO_VENDORING_DIRECTORY}/attest
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/attest/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/account-keys ${CARGO_VENDORING_DIRECTORY}/libsignal-account-keys
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-account-keys/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/core ${CARGO_VENDORING_DIRECTORY}/libsignal-core
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-core/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/debug ${CARGO_VENDORING_DIRECTORY}/libsignal-debug
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-debug/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/keytrans ${CARGO_VENDORING_DIRECTORY}/libsignal-keytrans
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-keytrans/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/net ${CARGO_VENDORING_DIRECTORY}/libsignal-net
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-net/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/net/infra ${CARGO_VENDORING_DIRECTORY}/libsignal-net-infra
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-net-infra/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/protocol ${CARGO_VENDORING_DIRECTORY}/libsignal-protocol
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-protocol/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/svrb ${CARGO_VENDORING_DIRECTORY}/libsignal-svrb
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-svrb/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/poksho ${CARGO_VENDORING_DIRECTORY}/poksho
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/poksho/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/crypto ${CARGO_VENDORING_DIRECTORY}/signal-crypto
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/signal-crypto/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/usernames ${CARGO_VENDORING_DIRECTORY}/usernames
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/usernames/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/zkcredential ${CARGO_VENDORING_DIRECTORY}/zkcredential
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/zkcredential/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal/rust/zkgroup ${CARGO_VENDORING_DIRECTORY}/zkgroup
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/zkgroup/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/libsignal-service-rs ${CARGO_VENDORING_DIRECTORY}/libsignal-service
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsignal-service/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/presage/presage-store-sqlite ${CARGO_VENDORING_DIRECTORY}/presage-store-sqlite
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/presage-store-sqlite/.cargo-checksum.json
    cp -a ${WORKDIR}/git-deps/rusqlite/libsqlite3-sys ${CARGO_VENDORING_DIRECTORY}/libsqlite3-sys
    echo '{"files":{},"package":null}' > ${CARGO_VENDORING_DIRECTORY}/libsqlite3-sys/.cargo-checksum.json
}

do_configure:append() {
    # Point each git source at the vendor directory assembled above. The source key must
    # match the URL and ref exactly as Cargo.lock records them, or cargo ignores it.
    cat <<-EOF >> ${CARGO_HOME}/config
	[source."git+https://github.com/kamadorueda/rust-mime-sniffer"]
	git = "https://github.com/kamadorueda/rust-mime-sniffer"
	replace-with = "bitbake"

	[source."git+https://github.com/signalapp/SparsePostQuantumRatchet.git?tag=v1.5.1"]
	git = "https://github.com/signalapp/SparsePostQuantumRatchet.git"
	tag = "v1.5.1"
	replace-with = "bitbake"

	[source."git+https://github.com/signalapp/boring?tag=signal-v5.0.2"]
	git = "https://github.com/signalapp/boring"
	tag = "signal-v5.0.2"
	replace-with = "bitbake"

	[source."git+https://github.com/signalapp/curve25519-dalek?tag=signal-curve25519-4.1.3"]
	git = "https://github.com/signalapp/curve25519-dalek"
	tag = "signal-curve25519-4.1.3"
	replace-with = "bitbake"

	[source."git+https://github.com/signalapp/libsignal?tag=v0.94.4"]
	git = "https://github.com/signalapp/libsignal"
	tag = "v0.94.4"
	replace-with = "bitbake"

	[source."git+https://github.com/whisperfish/libsignal-service-rs?rev=fd481655d481388ebfe3a1fdb85a668b4876d592"]
	git = "https://github.com/whisperfish/libsignal-service-rs"
	rev = "fd481655d481388ebfe3a1fdb85a668b4876d592"
	replace-with = "bitbake"

	[source."git+https://github.com/whisperfish/presage?rev=63482ef"]
	git = "https://github.com/whisperfish/presage"
	rev = "63482ef"
	replace-with = "bitbake"

	[source."git+https://github.com/whisperfish/rusqlite?rev=2a42b3354c9194700d08aa070f70a131a470e7dc"]
	git = "https://github.com/whisperfish/rusqlite"
	rev = "2a42b3354c9194700d08aa070f70a131a470e7dc"
	replace-with = "bitbake"

	EOF
}
