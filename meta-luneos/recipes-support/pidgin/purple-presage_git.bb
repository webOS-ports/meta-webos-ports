SUMMARY = "Signal protocol plug-in for libpurple"
DESCRIPTION = "libpurple prpl for Signal, backed by the presage Rust library. The Rust half is \
built as a staticlib and linked into the C plugin."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-connectors.inc

# Built from luneos-synergy-connectors, the version ended in its commit alone (1.0+git0+d22c6be58f).
# With the upstream commit now in front it can sort lower, so bump the epoch to keep package feeds
# moving forward.
PE = "4"
require purple-presage-crates.inc
require purple-presage-git-deps.inc

# One feature per patch. 0001 moves to presage 4b81740 (Cargo.toml and Cargo.lock; SRCREV_presage
# and SRCREV_libsignal-service-rs in purple-presage-git-deps.inc and the crates in
# purple-presage-crates.inc follow it). That presage brings libsignal-service-rs 7a75267, which
# sends uploadLength with an attachment upload form request: since early November 2026
# Signal-Server refuses the form without it, so no attachment could be sent before. 0002-0006 are
# generic fixes, submitted upstream as hoehermann/purple-presage #55-#59 (#58 and #59 build on
# #55); the webOS: ones from 0007 adapt the plug-in to imlibpurpletransport and the webOS
# Messaging app.
#
# 0009 adds 1:1 voice and video calls through the Phone app: call.c registers
# com.palm.signal.call (one luna-service2 handle) and reports active calls to audiod; the Rust half
# does the RingRTC signaling, fetches the TURN relays with presage's Manager::calling_relays(), and
# runs the signal-call-media engine for each call. It adds tokio's "time" feature to Cargo.toml,
# which leaves Cargo.lock as it is. presage's "cdsi" feature, which calling a number that is no
# stored contact would need, is not enabled: it pulls in BoringSSL. 0012 passes how the phone is
# held (the Phone app's deviceOrientation) on to signal-call-media, for the orientation it sends.
#
# presage/0001 is for presage itself (the git-deps checkout, hence its patchdir): presage since
# #471 uses an `if let` match guard, which the Rust 1.94 of oe-core wrynose rejects as
# experimental. Drop it once the Rust here accepts them.
SRC_URI += " \
    git://github.com/hoehermann/purple-presage.git;branch=master;protocol=https;name=upstream;destsuffix=purple-presage \
    file://0001-Move-to-presage-4b81740.patch \
    file://0002-Keep-the-connection-up-on-receive-and-command-errors.patch \
    file://0003-contacts-sync-names-from-the-primary-and-resolve-unk.patch \
    file://0004-Pin-the-module-resident-and-delete-the-store-when-an.patch \
    file://0005-bridge-carry-the-message-timestamp-in-milliseconds-a.patch \
    file://0006-Announce-the-connection-only-once-per-receive-sessio.patch \
    file://0007-webOS-hand-the-device-link-URI-to-the-UI-instead-of-.patch \
    file://0008-webOS-bridge-Signal-reactions-replies-and-receipts-t.patch \
    file://0009-webOS-1-1-Signal-voice-and-video-calls-through-the-P.patch \
    file://0010-webOS-edit-and-delete-Signal-messages-sent-and-recei.patch \
    file://0011-webOS-send-voice-notes-mentions-and-contact-cards-as.patch \
    file://0012-webOS-Pass-how-the-phone-is-held-on-to-the-call-engi.patch \
    file://presage/0001-store-build-without-if-let-guards-for-Rust-1.94.patch;patchdir=${UNPACKDIR}/git-deps/presage \
"
SRCREV_upstream = "235edc846d4f28f306ddfe40dc187ef42face79f"

# gdk-pixbuf is only asked which image types it can load (pixbuf.c); without it the plug-in
# assumes JPEG and PNG. protobuf-native supplies protoc for the prost-build scripts of
# libsignal, libsignal-service and spqr. openssl is libcrypto for the bundled SQLCipher.
# luna-service2 is for call.c.
DEPENDS = "pidgin glib-2.0 gdk-pixbuf openssl protobuf-native luna-service2"

# The call media engine 0009 runs for each call; call_bridge.rs takes its path from this at build
# time.
export SIGNAL_MEDIA_ENGINE = "${libexecdir}/signal-call-media"
RDEPENDS:${PN} += "signal-call-media"

S = "${UNPACKDIR}/purple-presage"
CARGO_SRC_DIR = "src/rust"

inherit cargo cargo-update-recipe-crates

# Built with the rust/cargo 1.94 from oe-core wrynose. Upstream's Cargo.lock is lockfile version 4
# and nothing in it needs a newer rust, so it is used exactly as upstream ships it.
#
# Upstream builds with CMake, which FetchContent-clones Corrosion at configure time to drive cargo.
# That needs network outside do_fetch and duplicates what cargo.bbclass does, so CMake is not used:
# cargo.bbclass builds the staticlib (offline, --frozen), and do_compile:append compiles the C
# sources -- every file in src/c, the same list as upstream's CMakeLists.txt -- and links them with
# it, as upstream's Makefile does.

# The libsqlite3-sys built here is SQLCipher, linked against the system libcrypto.
export OPENSSL_LIB_DIR = "${STAGING_LIBDIR}"
export OPENSSL_INCLUDE_DIR = "${STAGING_INCDIR}"
export PROTOC = "${STAGING_BINDIR_NATIVE}/protoc"

# The C parts of ring and SQLCipher are compiled by cc-rs inside the vendored crates under
# ${CARGO_HOME} and the git-deps checkouts, both in ${UNPACKDIR} but outside ${S} and ${B}, so the
# default prefix maps miss them and the debug info keeps TMPDIR paths (QA buildpaths). Prepended,
# so the more specific ${S} and ${B} maps still win for the plug-in's own sources.
DEBUG_PREFIX_MAP:prepend = "-ffile-prefix-map=${UNPACKDIR}=${TARGET_DBGSRC_DIR} "

# Replaces cargo_common.bbclass's function of the same name. That one maps every git SRC_URI entry
# with a name and destsuffix to `[patch."<repo>"] <name> = { path = "<destsuffix>" }`, which only
# works for a repository holding a single crate at its root. Here it would also catch the upstream
# and connectors checkouts, and the libsignal repository holds nine of the crates in
# subdirectories. This one uses the explicit crate -> repo:path list in purple-presage-git-deps.inc.
#
# libsqlite3-sys needs different handling: upstream's Cargo.toml takes it from git through
# [patch.crates-io], and cargo cannot [patch] a source that itself comes from a [patch]; it then
# tries to fetch it and fails offline. So that entry in Cargo.toml is pointed at the checkout
# directly.
#
# As in the original, the lock's `source = "git+..."` lines are dropped (from a saved .orig, so
# this can run more than once): the patched crates are path crates now and --frozen does not let
# cargo rewrite the lock itself.
python cargo_common_do_patch_paths() {
    import re, shutil, tomllib

    unpackdir = d.getVar('UNPACKDIR')
    patches = {}
    paths = {}
    for entry in (d.getVar('PRESAGE_GIT_CRATES') or '').split():
        crate, location = entry.split('=', 1)
        repo, subdir = location.split(':', 1)
        path = os.path.join(unpackdir, 'git-deps', repo, subdir)
        if not os.path.exists(os.path.join(path, 'Cargo.toml')):
            bb.fatal('%s: no Cargo.toml in %s' % (crate, path))
        paths[crate] = path
        url = d.getVarFlag('PRESAGE_GIT_URL', repo)
        if not url:
            bb.fatal('PRESAGE_GIT_URL[%s] is not set' % repo)
        patches.setdefault(url, []).append((crate, '%s = { path = "%s" }' % (crate, path)))

    manifest = d.getVar('CARGO_MANIFEST_PATH')
    with open(manifest, 'rb') as f:
        crates_io_patched = tomllib.load(f).get('patch', {}).get('crates-io', {})
    with open(manifest) as f:
        text = f.read()
    for crate, spec in crates_io_patched.items():
        if isinstance(spec, dict) and 'git' in spec:
            if crate not in paths:
                bb.fatal('%s is patched from git in Cargo.toml but not in PRESAGE_GIT_CRATES' % crate)
            text, n = re.subn(r'(?m)^%s\s*=.*$' % re.escape(crate),
                              '%s = { path = "%s" }' % (crate, paths[crate]), text)
            if n != 1:
                bb.fatal('could not rewrite %s in %s' % (crate, manifest))
            for url in patches:
                patches[url] = [p for p in patches[url] if p[0] != crate]
    with open(manifest, 'w') as f:
        f.write(text)

    with open(os.path.join(d.getVar('CARGO_HOME'), 'config.toml'), 'a') as config:
        for url, entries in patches.items():
            if entries:
                print('\n[patch."%s"]' % url, file=config)
                for crate, line in entries:
                    print(line, file=config)

    lockfile = d.getVar('CARGO_LOCK_PATH')
    lockfile_orig = lockfile + '.orig'
    if not os.path.exists(lockfile_orig):
        shutil.copy(lockfile, lockfile_orig)
    with open(lockfile_orig) as f:
        lines = [l for l in f if not l.startswith('source = "git')]
    with open(lockfile, 'w') as f:
        f.writelines(lines)
}

# init.c reports this as the plug-in version. Upstream composes it from the crate version, the
# commit date, the commit count and the presage commit, asking git and cargo metadata; here it is
# the crate version and the two pinned commits. The preprocessor stringifies it, so it has to stay
# a single token.
PRESAGE_PLUGIN_VERSION = "0.0.0_${@d.getVar('SRCREV_upstream')[:7]}_${@d.getVar('SRCREV_presage')[:7]}"

# The libraries are the ones `cargo rustc -- --print native-static-libs` lists for the Rust half,
# plus libcrypto for SQLCipher, gmodule-2.0 for g_module_make_resident() from 0004 and
# luna-service2 for call.c from 0009.
# --no-undefined turns a symbol that would only be missed at dlopen time into a link error.
#
# The version script exports purple_init_plugin, the one symbol libpurple looks up, and nothing
# else: without it every global symbol of the plug-in and of the Rust staticlib (about 11,800) ends
# up in the dynamic symbol table, where it can interpose on the same names in other libraries.
do_compile:append() {
    printf '{\n    global: purple_init_plugin;\n    local: *;\n};\n' > ${B}/libpresage.map
    install -d ${B}/c
    for src in ${S}/src/c/*.c; do
        ${CC} ${CFLAGS} -fPIC -DPLUGIN_VERSION=${PRESAGE_PLUGIN_VERSION} \
            $(pkg-config --cflags purple glib-2.0 gdk-pixbuf-2.0 luna-service2) \
            -c $src -o ${B}/c/$(basename $src .c).o
    done
    ${CC} ${LDFLAGS} -shared -Wl,--no-undefined -Wl,--version-script=${B}/libpresage.map \
        -o ${B}/libpresage.so \
        ${B}/c/*.o \
        ${B}/target/${CARGO_TARGET_SUBDIR}/libpurple_presage_backend.a \
        $(pkg-config --libs purple gmodule-2.0 glib-2.0 gdk-pixbuf-2.0 luna-service2) \
        -lcrypto -lgcc_s -lutil -lrt -lpthread -lm -ldl
}

# Installed by hand, so no Pidgin pixmaps are shipped -- see purple-connectors.inc. This also
# replaces cargo.bbclass's do_install, which only knows about binaries and Rust libraries.
#
# Not yet run on LuneOS: a clean build says the plug-in links and loads, not that Signal works.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${B}/libpresage.so ${D}${libdir}/purple-2/libpresage.so

    # Account template, so Signal can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/signal/account/com.palm.signal ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/signal/apps/com.palm.app.signal ${D}${webos_applicationsdir}/

    # The signal call service (call.c, from 0009): its bus name is listed, never activated --
    # imlibpurpleservice, which loads this plug-in, registers com.palm.signal.call itself, so the
    # .service file is Type=static -- and the Phone app is granted imlibpurple-call.operation to
    # call it. The role letting imlibpurpleservice own the name, and the api-permissions/groups
    # behind that group, ship with imlibpurpleservice. webos_configure_manifest lists both files
    # in this package's manifest.
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_permissionsdir}
    install -m 0644 ${CONNECTORS_DIR}/im/signal/files/sysbus/com.palm.signal.call.service \
        ${D}${webos_sysbus_servicedir}/
    install -m 0644 ${CONNECTORS_DIR}/im/signal/files/sysbus/com.palm.signal.call.perm.json \
        ${D}${webos_sysbus_permissionsdir}/
}
