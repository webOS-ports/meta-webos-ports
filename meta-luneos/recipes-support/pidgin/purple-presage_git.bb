SUMMARY = "Signal protocol plug-in for libpurple"
DESCRIPTION = "libpurple prpl for Signal, backed by the presage Rust library. The Rust half is \
built as a staticlib and linked into the C plugin."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-connectors.inc

# Built from luneos-synergy-connectors, the version ended in its commit alone (1.0+git0+d22c6be58f).
# With the upstream commit now in front it can sort lower, so bump the epoch to keep package feeds
# moving forward.
PE = "1"
require purple-presage-crates.inc
require purple-presage-git-deps.inc

# 0001-0005 are generic fixes meant for upstream; the webOS: ones from 0006 adapt the plug-in to
# imlibpurpletransport and the webOS Messaging app.
#
# The webOS tree also carries 1:1 voice calls (call.c, call_bridge.rs, call_media.rs, ice.rs, and
# a presage patch exposing the identified push service). That is left out here on purpose:
#   * it only works on legacy webOS: it spawns a media engine from /media/internal, uses WPE and
#     gst paths that do not exist on LuneOS, LD_PRELOADs libasound and assumes there is no IPv6;
#   * it writes the TURN credentials into log files;
#   * upstream's build does not compile call.c, which needs luna-service2 and the split-bus
#     compat header, so with the Rust half calling into it the plug-in would not even dlopen.
# None of 0001-0009 reference it, and without it the Rust half keeps upstream's Cargo.toml and
# Cargo.lock unchanged. The one generic fix it contained -- announce "connected" only once per
# receive session -- is 0005 here.
SRC_URI += " \
    git://github.com/hoehermann/purple-presage.git;branch=master;protocol=https;name=upstream;destsuffix=purple-presage \
    file://0001-Keep-the-connection-up-on-receive-and-command-errors.patch \
    file://0002-contacts-sync-names-from-the-primary-and-resolve-unk.patch \
    file://0003-Pin-the-module-resident-and-delete-the-store-when-an.patch \
    file://0004-bridge-carry-the-message-timestamp-in-milliseconds-a.patch \
    file://0005-Announce-the-connection-only-once-per-receive-sessio.patch \
    file://0006-webOS-hand-the-device-link-URI-to-the-UI-instead-of-.patch \
    file://0007-webOS-map-Signal-reactions-to-and-from-the-transport.patch \
    file://0008-webOS-show-Signal-replies-as-inline-quote-cards.patch \
    file://0009-webOS-report-delivery-and-read-receipts-to-the-trans.patch \
"
SRCREV_upstream = "c4c9b8d8e1a822f973520e8870aba1d2347b18c6"

# gdk-pixbuf is only asked which image types it can load (pixbuf.c); without it the plug-in
# assumes JPEG and PNG. protobuf-native supplies protoc for the prost-build scripts of
# libsignal, libsignal-service and spqr. openssl is libcrypto for the bundled SQLCipher.
DEPENDS = "pidgin glib-2.0 gdk-pixbuf openssl protobuf-native"

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
# plus libcrypto for SQLCipher and gmodule-2.0 for g_module_make_resident() from 0003.
# --no-undefined turns a symbol that would only be missed at dlopen time into a link error.
do_compile:append() {
    install -d ${B}/c
    for src in ${S}/src/c/*.c; do
        ${CC} ${CFLAGS} -fPIC -DPLUGIN_VERSION=${PRESAGE_PLUGIN_VERSION} \
            $(pkg-config --cflags purple glib-2.0 gdk-pixbuf-2.0) \
            -c $src -o ${B}/c/$(basename $src .c).o
    done
    ${CC} ${LDFLAGS} -shared -Wl,--no-undefined -o ${B}/libpresage.so \
        ${B}/c/*.o \
        ${B}/target/${CARGO_TARGET_SUBDIR}/libpurple_presage_backend.a \
        $(pkg-config --libs purple gmodule-2.0 glib-2.0 gdk-pixbuf-2.0) \
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
}
