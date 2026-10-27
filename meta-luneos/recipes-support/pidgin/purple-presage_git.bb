SUMMARY = "Signal protocol plug-in for libpurple"
DESCRIPTION = "libpurple prpl for Signal, backed by the presage Rust library. The Rust half is \
built as a staticlib and linked into the C plugin."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-synergy.inc
require purple-presage-crates.inc
require purple-presage-git-deps.inc

DEPENDS = "pidgin glib-2.0 openssl sqlite3 protobuf-native"

# protobuf-native for protoc: libsignal-svrb's build script (and other libsignal crates') runs
# protobuf-parse on its .proto files, which panics with "protoc binary not found" without one.
# PROTOC names it explicitly rather than relying on PATH.
export PROTOC = "${STAGING_BINDIR_NATIVE}/protoc"

# boring-sys runs bindgen over the BoringSSL headers, through libclang. Without a target and a
# sysroot, clang parses the BUILD host's /usr/include and stops at its x86-64 bits/floatn.h
# ("__float128 is not supported on this target"). Same settings as thin-provisioning-tools in
# meta-oe: libclang from clang-native, the target triple and the tune's -m flags, and the
# --sysroot that TOOLCHAIN_OPTIONS carries.
DEPENDS += "clang-native"
export LIBCLANG_PATH = "${STAGING_LIBDIR_NATIVE}"
BINDGEN_EXTRA_CLANG_ARGS = "${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} --target=${TARGET_SYS}"
export BINDGEN_EXTRA_CLANG_ARGS

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/im/signal/plugin/purple-presage"
CARGO_SRC_DIR = "src/rust"

# cargo builds the Rust half (libpurple_presage_backend.a); the C prpl around it is compiled and
# linked by hand below, the way build-presage.sh (the device build this plug-in was developed
# with) does it. Not with upstream's CMakeLists.txt: that pulls Corrosion with FetchContent, a
# git clone at configure time that a build without network cannot do, and its source list leaves
# out src/c/call.c -- which init.c and connection.c call unconditionally (callLunaInit), so a
# libpresage.so linked from it would not even load under -z now.
inherit cargo cargo-update-recipe-crates

# presage's dependencies arrive from two places, because they come from two:
#
#   purple-presage-crates.inc     443 crates.io packages, as crate:// entries. Regenerate with
#                                 `bitbake -c update_crates purple-presage`.
#   purple-presage-git-deps.inc   the 24 that are signalapp/whisperfish forks and are NOT on
#                                 crates.io. Fetched as 8 git repos and wired in with [patch] --
#                                 see that file for why cargo_common.bbclass's own git support
#                                 cannot express them.
#
# Both pin revisions from src/rust/Cargo.lock, so the build is reproducible and needs no network
# during do_compile, which cargo.bbclass forbids anyway (it builds --frozen).
#
# The Rust half links libcrypto for SQLCipher, so openssl has to be visible to the crate build
# scripts and not only to the final C link. boring-sys builds BoringSSL from its own submodule,
# which is the slowest part of this recipe by a wide margin.

# C code compiled by crate build scripts (SQLCipher in libsqlite3-sys, ring) sits outside ${S}:
# under ${UNPACKDIR}/git-deps and the crates.io vendor directory. OE's automatic prefix maps cover
# ${S} and ${B} only, so those paths survive into the debug info and trip buildpaths QA:
#   File /usr/lib/purple-2/.debug/libpresage.so in package purple-presage-dbg contains
#   reference to TMPDIR [buildpaths]
# Map the whole unpack directory, as purple-teams and purple-combined do.
DEBUG_PREFIX_MAP:append = " -ffile-prefix-map=${UNPACKDIR}=${TARGET_DBGSRC_DIR}"

# luna-service2 for src/c/call.c, the com.palm.signal.call service the Phone app talks to.
DEPENDS += "luna-service2"

# The C half: every file build-presage.sh compiles, call.c included.
PRESAGE_C_SOURCES = "init bridge connection qrcode receive_text send_text blist status groups \
                     receive_attachment send_file profile options attachment_common pixbuf call"

do_compile:append() {
    # PLUGIN_VERSION is stringified by init.c (MAKE_STR), so it goes in as a bare token.
    # webos-ls2-compat.h, which call.c includes, maps the legacy split-bus LS2 API onto the
    # single-bus luna-service2 3.x LuneOS ships; luna-service2's own .pc gives only
    # -I${includedir}, while lunaservice.h is included bare.
    PCFLAGS="${CFLAGS} -fPIC -DPURPLE_PLUGINS -DPLUGIN_VERSION=${PV} \
             -I${S}/src/c -I${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/im/_core/include \
             -I${STAGING_INCDIR}/luna-service2 \
             $(pkg-config --cflags purple glib-2.0 gmodule-2.0 luna-service2)"
    OBJS=""
    for s in ${PRESAGE_C_SOURCES}; do
        ${CC} $PCFLAGS -c ${S}/src/c/$s.c -o ${B}/presage_$s.o
        OBJS="$OBJS ${B}/presage_$s.o"
    done

    # The staticlib's native dependencies, as `cargo rustc -- --print native-static-libs` lists
    # them (and build-presage.sh links them), plus libcrypto for SQLCipher.
    ${CC} -shared -fPIC ${LDFLAGS} -Wl,-soname,libpresage.so -o ${B}/libpresage.so \
        $OBJS \
        -Wl,--start-group ${B}/target/${CARGO_TARGET_SUBDIR}/libpurple_presage_backend.a \
        $(pkg-config --libs purple glib-2.0 gmodule-2.0 luna-service2) -lcrypto -Wl,--end-group \
        -lstdc++ -lpthread -ldl -lm -lrt -lutil -lgcc_s

    # A plug-in libpurple cannot initialise links just as happily; check here, not at runtime.
    if ! ${NM} -D --defined-only ${B}/libpresage.so | grep -q ' purple_init_plugin$'; then
        bbfatal "libpresage.so exports no purple_init_plugin; libpurple would reject it"
    fi
}

# NOTE, unrelated to whether this builds: Signal has been disabled on webOS. libpresage aborted
# the whole imlibpurple transport on login there, from an uncaught "Couldn't find prpl" raised in
# Util::getProtocolInfo. That is a transport bug rather than a LuneOS one, but a green build here
# does not mean a working plugin.

do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${B}/libpresage.so ${D}${libdir}/purple-2/libpresage.so

    # Account template, so Signal can be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/im/signal/account/com.palm.signal ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/im/signal/apps/com.palm.app.signal ${D}${webos_applicationsdir}/

    # The call service call.c builds: its bus name is listed, never activated (imlibpurpleservice,
    # which loads this plug-in, registers com.palm.signal.call itself, so the .service file is
    # Type=static), and the Phone app is granted imlibpurple-call.operation to call it. The role
    # and the group come with imlibpurpleservice; webos_configure_manifest lists both files in
    # this package's manifest.
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_permissionsdir}
    install -m 0644 ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/im/signal/files/sysbus/com.palm.signal.call.service \
        ${D}${webos_sysbus_servicedir}/
    install -m 0644 ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/im/signal/files/sysbus/com.palm.signal.call.perm.json \
        ${D}${webos_sysbus_permissionsdir}/
}
