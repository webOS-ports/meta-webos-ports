# Git-sourced Rust dependencies for purple-presage. Auto-generated; regenerate if Cargo.lock
# changes. The crates.io half is in purple-presage-crates.inc.
#
# 24 of presage's dependencies are signalapp/whisperfish forks that are not on crates.io, so
# crate:// cannot serve them. cargo.bbclass builds with --frozen (no network), so they have to be
# present locally before do_compile.
#
# The mechanism is cargo's [patch] with path dependencies into the fetched checkouts -- the same
# one cargo_common.bbclass uses for git dependencies, written out here because its generator
# cannot express these (see the end of this file). Not EXTRA_OECARGO_PATHS: cargo's `paths`
# override only applies to path and registry dependencies and silently does nothing for git ones.
# Not source replacement into a vendor directory either: most of these crates are workspace
# members that inherit fields from their workspace root (authors, version, dependencies), and a
# crate copied out of its workspace no longer finds that root ("error inheriting `authors` from
# workspace root manifest's `workspace.package.authors`"). `cargo vendor` normalizes those
# manifests; a [patch] path into the full checkout needs nothing of the sort.
#
# Revisions come from Cargo.lock, so this stays reproducible.

SRC_URI += " \
    git://github.com/kamadorueda/rust-mime-sniffer;protocol=https;nobranch=1;name=rust-mime-sniffer;destsuffix=git-deps/rust-mime-sniffer \
    git://github.com/signalapp/SparsePostQuantumRatchet.git;protocol=https;nobranch=1;name=sparsepostquantumratchet;destsuffix=git-deps/sparsepostquantumratchet \
    gitsm://github.com/signalapp/boring;protocol=https;nobranch=1;name=boring;destsuffix=git-deps/boring \
    git://github.com/signalapp/curve25519-dalek;protocol=https;nobranch=1;name=curve25519-dalek;destsuffix=git-deps/curve25519-dalek \
    git://github.com/signalapp/libsignal;protocol=https;nobranch=1;name=libsignal;destsuffix=git-deps/libsignal \
    git://github.com/whisperfish/libsignal-service-rs;protocol=https;nobranch=1;name=libsignal-service-rs;destsuffix=git-deps/libsignal-service-rs \
    git://github.com/whisperfish/presage;protocol=https;nobranch=1;name=presage;destsuffix=git-deps/presage \
    git://github.com/whisperfish/rusqlite;protocol=https;nobranch=1;name=rusqlite;destsuffix=git-deps/rusqlite \
"

# boring is gitsm://: boring-sys builds BoringSSL from its deps/boringssl submodule, which cargo
# would check out along with a git dependency. Fetched as plain git it is empty, and the build
# script falls back to `git submodule update` -- network access in do_compile, which fails.
SRCREV_rust-mime-sniffer = "0053c4330ca159db118e57435ea781bdf46158ed"
SRCREV_sparsepostquantumratchet = "f2589fef855c10f39d72634dab3d14654dd410bf"
SRCREV_boring = "2c157897feea0c54f5eca93407a157fa90703b5f"
SRCREV_curve25519-dalek = "7c6d34756355a3566a704da84dce7b1c039a6572"
SRCREV_libsignal = "46d867c986f66201e34e7ae20ce423eec742bf3f"
SRCREV_libsignal-service-rs = "fd481655d481388ebfe3a1fdb85a668b4876d592"
SRCREV_presage = "63482efd0cbdc0780baf0650517c7d55f1cac05d"
SRCREV_rusqlite = "2a42b3354c9194700d08aa070f70a131a470e7dc"

SRCREV_FORMAT = "default_rust-mime-sniffer_sparsepostquantumratchet_boring_curve25519-dalek_libsignal_libsignal-service-rs_presage_rusqlite"

do_configure:append() {
    # One [patch] table per repository, one entry per crate, keyed by the URL the dependency is
    # declared with (cargo compares them canonically, so the tag or rev does not take part).
    # src/rust/Cargo.toml itself patches two crates.io crates with git checkouts
    # (curve25519-dalek, libsqlite3-sys); a patch is not patched again, so those two are
    # overridden under [patch.crates-io] here -- a [patch] in config takes precedence over the
    # same one in the manifest.
    # Appended to config.toml, the file cargo_common.bbclass writes its own settings into: a
    # second file named plain "config" next to it would be read instead, so drop any such file a
    # reused WORKDIR still has from an earlier version of this recipe.
    rm -f ${CARGO_HOME}/config

    # src/rust/Cargo.toml patches presage with its own copy, src/rust/vendor/presage (rev 63482ef
    # plus one change). Fetched from git, presage-store-sqlite's `presage = { path = "../presage" }`
    # stayed inside the git source and so got that patch too; as a path crate it would reach the
    # upstream presage beside it instead, a second package of the same name and version that
    # Cargo.lock does not know. Put the vendored copy in that place, and patch presage to it
    # below, so both routes end at one package.
    rm -rf ${UNPACKDIR}/git-deps/presage/presage
    cp -a ${S}/${CARGO_SRC_DIR}/vendor/presage ${UNPACKDIR}/git-deps/presage/presage
    # The same WORKDIR may also still have the vendor copies that version made, one directory
    # per crate under its bare name (crates.io crates are unpacked as <name>-<version>). Being
    # in the crates.io vendor directory, cargo would try to parse them -- out of their workspace.
    for c in attest \
             boring \
             boring-sys \
             curve25519-dalek \
             curve25519-dalek-derive \
             libsignal-account-keys \
             libsignal-core \
             libsignal-debug \
             libsignal-keytrans \
             libsignal-net \
             libsignal-net-infra \
             libsignal-protocol \
             libsignal-service \
             libsignal-svrb \
             libsqlite3-sys \
             mime-sniffer \
             poksho \
             presage-store-sqlite \
             signal-crypto \
             spqr \
             tokio-boring \
             usernames \
             zkcredential \
             zkgroup; do
        rm -rf ${CARGO_VENDORING_DIRECTORY}/$c
    done
    cat <<-EOF >> ${CARGO_HOME}/config.toml
	[patch.crates-io]
	curve25519-dalek = { path = "${UNPACKDIR}/git-deps/curve25519-dalek/curve25519-dalek" }
	libsqlite3-sys = { path = "${UNPACKDIR}/git-deps/rusqlite/libsqlite3-sys" }

	[patch."https://github.com/kamadorueda/rust-mime-sniffer"]
	mime-sniffer = { path = "${UNPACKDIR}/git-deps/rust-mime-sniffer" }

	[patch."https://github.com/signalapp/SparsePostQuantumRatchet.git"]
	spqr = { path = "${UNPACKDIR}/git-deps/sparsepostquantumratchet" }

	[patch."https://github.com/signalapp/boring"]
	boring = { path = "${UNPACKDIR}/git-deps/boring/boring" }
	boring-sys = { path = "${UNPACKDIR}/git-deps/boring/boring-sys" }
	tokio-boring = { path = "${UNPACKDIR}/git-deps/boring/tokio-boring" }

	[patch."https://github.com/signalapp/curve25519-dalek"]
	curve25519-dalek = { path = "${UNPACKDIR}/git-deps/curve25519-dalek/curve25519-dalek" }
	curve25519-dalek-derive = { path = "${UNPACKDIR}/git-deps/curve25519-dalek/curve25519-dalek-derive" }

	[patch."https://github.com/signalapp/libsignal"]
	attest = { path = "${UNPACKDIR}/git-deps/libsignal/rust/attest" }
	libsignal-account-keys = { path = "${UNPACKDIR}/git-deps/libsignal/rust/account-keys" }
	libsignal-core = { path = "${UNPACKDIR}/git-deps/libsignal/rust/core" }
	libsignal-debug = { path = "${UNPACKDIR}/git-deps/libsignal/rust/debug" }
	libsignal-keytrans = { path = "${UNPACKDIR}/git-deps/libsignal/rust/keytrans" }
	libsignal-net = { path = "${UNPACKDIR}/git-deps/libsignal/rust/net" }
	libsignal-net-infra = { path = "${UNPACKDIR}/git-deps/libsignal/rust/net/infra" }
	libsignal-protocol = { path = "${UNPACKDIR}/git-deps/libsignal/rust/protocol" }
	libsignal-svrb = { path = "${UNPACKDIR}/git-deps/libsignal/rust/svrb" }
	poksho = { path = "${UNPACKDIR}/git-deps/libsignal/rust/poksho" }
	signal-crypto = { path = "${UNPACKDIR}/git-deps/libsignal/rust/crypto" }
	usernames = { path = "${UNPACKDIR}/git-deps/libsignal/rust/usernames" }
	zkcredential = { path = "${UNPACKDIR}/git-deps/libsignal/rust/zkcredential" }
	zkgroup = { path = "${UNPACKDIR}/git-deps/libsignal/rust/zkgroup" }

	[patch."https://github.com/whisperfish/libsignal-service-rs"]
	libsignal-service = { path = "${UNPACKDIR}/git-deps/libsignal-service-rs" }

	[patch."https://github.com/whisperfish/presage"]
	presage = { path = "${UNPACKDIR}/git-deps/presage/presage" }
	presage-store-sqlite = { path = "${UNPACKDIR}/git-deps/presage/presage-store-sqlite" }

	[patch."https://github.com/whisperfish/rusqlite"]
	libsqlite3-sys = { path = "${UNPACKDIR}/git-deps/rusqlite/libsqlite3-sys" }

	EOF

    # With the crates now path dependencies, Cargo.lock's `source = "git+..."` lines no longer
    # match and --frozen refuses to rewrite them. Drop them, as cargo_common_do_patch_paths does,
    # from a pristine copy kept as Cargo.lock.orig.
    if [ ! -f ${CARGO_LOCK_PATH}.orig ]; then
        cp ${CARGO_LOCK_PATH} ${CARGO_LOCK_PATH}.orig
    fi
    grep -v '^source = "git' ${CARGO_LOCK_PATH}.orig > ${CARGO_LOCK_PATH}
}

# cargo_common.bbclass has its own generator for this, and it cannot serve these crates.
# cargo_common_do_patch_paths turns every git SRC_URI carrying name= and destsuffix= into
#     [patch."<repo url>"]
#     <name> = { path = "${UNPACKDIR}/<destsuffix>" }
# which needs name= to be the CRATE and the checkout root to be that crate's directory. Here
# 8 repositories provide 24 crates, most of them workspace members in subdirectories
# (libsignal/rust/protocol, boring/boring-sys, ...) that cannot be checked out without their
# workspace; and name= has to stay the repository name, since it keys SRCREV_<name>. The step
# would emit entries for "crates" called rust-mime-sniffer, libsignal, boring, ..., and cargo
# stops at the first: "patch location ... does not appear to contain any packages matching the
# name `rust-mime-sniffer`". cargo-update-recipe-crates does not help either: it only writes
# crate:// entries for crates.io sources. So the step is left out and do_configure:append above
# writes the entries it would have, per crate. (Varflags take no :remove override, hence the
# anonymous function.)
python __anonymous() {
    funcs = (d.getVarFlag('do_configure', 'postfuncs') or '').split()
    d.setVarFlag('do_configure', 'postfuncs', ' '.join(f for f in funcs if f != 'cargo_common_do_patch_paths'))
}
