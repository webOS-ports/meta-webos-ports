SUMMARY = "Telegram protocol plug-in for libpurple (TDLib)"
DESCRIPTION = "libpurple prpl for Telegram built on TDLib, with webOS voice calling via an \
in-tree libtgvoip."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=60e1c5c15dfde4c879ef9a5f0f20feab"

require purple-synergy.inc

# tdlib comes from tdlib_1.8.66.bb alongside this recipe. It is the expensive part of a Telegram
# build: TDLib compiles a very large set of generated C++ sources, and on a constrained builder it
# is the first thing to OOM (set PARALLEL_MAKE in local.conf if that is you).
#
# A note on the No* switches, because they are easy to misread: they are plain CMake cache
# variables, but CMakeLists configure_file()s buildopt.h.in, which #cmakedefine's every one of them.
# So they reach the sources as real preprocessor macros, and the `#ifndef NoVoip` / `#ifndef NoWebp`
# guards in call.cpp, account-data.h and sticker.cpp do what they look like they do. Turning one off
# genuinely removes that code rather than merely unlinking its library.
#
# libtgvoip is therefore a choice, not a necessity: -DNoVoip=ON would give a working messaging-only
# Telegram plugin with no voice engine at all. Calling is kept because it works and is a real
# feature on this platform -- see EXTRA_OECMAKE below.
#
# libopus/alsa-lib are here because libtgvoip is a STATIC archive: its undefined opus, ALSA and
# libcrypto references have to be satisfied at this link, not its own.
#
# libwebp/libpng decode Telegram's webp stickers. The device build passes -DNoWebp=TRUE and drops
# them; both libraries are readily available here, so this build keeps sticker decoding instead.
DEPENDS = "pidgin glib-2.0 zlib openssl tdlib libtgvoip libopus alsa-lib luna-service2 libwebp libpng"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/telegram/plugin/tdlib-purple"

inherit cmake pkgconfig

# tdlib-purple declares cmake_minimum_required(VERSION 3.2). Scarthgap's CMake still accepted that
# with a deprecation warning; wrynose's CMake 4 does not:
#   CMake Error at CMakeLists.txt:1 (cmake_minimum_required):
#     Compatibility with CMake < 3.5 has been removed from CMake.
# so -DCMAKE_POLICY_VERSION_MINIMUM=3.5 is now set below, as this note
# anticipated. webos_cmake.bbclass does the same for the webOS components, and
# clang_cmake.bbclass inherits it, but this recipe is a plain "inherit cmake".

# NoVoip=OFF keeps libtgvoip, which is what carries webOS calling. NoLottie=ON drops the rlottie
# sticker renderer: a large dependency the webOS UI does not animate anyway. Note that NoLottie also
# has to stay ON because the renderer is a bundled add_subdirectory(rlottie) that is not in this
# repository -- NoBundledLottie exists for an external one, but no rlottie recipe is wired up here.
#
# NoTranslations=ON, matching the device build, purely to avoid a gettext dependency:
# find_package(Gettext REQUIRED) fires at configure time otherwise. translate.h has a proper
# fallback for it (_() becomes a const_cast passthrough), so this costs the plugin's own localised
# strings and nothing else. Add `inherit gettext` and drop this line to turn them back on.
#
# NoTgcallsLite=ON is the one deviation from the device build. tgcalls-lite is the newer ICE/
# DTLS-SRTP video engine and CMake enables it by default, pulling in ../tgcalls-lite/*.cpp and
# REQUIRing nice/libsrtp2/json-glib at configure time. Those sources are not in the repository --
# messaging/telegram/plugin/tgcalls-lite/ is untracked local work -- so a clean clone cannot build
# it at all, and configure fails before that even becomes visible. Turn this back off (and add
# libnice/libsrtp/json-glib to DEPENDS) once the engine is committed.
#
# tgvoip_LIBRARIES is a CMake list, mirroring build-prpl.sh: the static libtgvoip.a leaves opus,
# ALSA and libcrypto undefined for this link to resolve. tgvoip_NO_DSP is deliberately NOT set --
# see the note in libtgvoip_git.bb about keeping both sides of TGVOIP_NO_DSP in agreement.
#
# lunaservice_* both need overriding. The defaults are the legacy webOS ones: the library there is
# liblunaservice.so, whereas LuneOS ships libluna-service2.so, and CMakeLists links the value
# unconditionally -- left alone it fails with `cannot find -llunaservice`. The include list also
# carries messaging/common, which is where webos-ls2-compat.h lives; call-luna.cpp includes it to
# map the legacy split-bus API onto 3.21.2's single bus, and passing it here is the same seam
# build-prpl.sh uses. The luna-service2 subdirectory is explicit because the header is included
# bare as <lunaservice.h> while the .pc offers only -I${includedir}.
#
# The semicolons must stay quoted: cmake_do_configure expands EXTRA_OECMAKE unquoted into a shell
# command, which would otherwise split the line at each one.
EXTRA_OECMAKE = " \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -DNoVoip=OFF \
    -DNoLottie=ON \
    -DNoTgcallsLite=ON \
    -DNoTranslations=ON \
    -Dtgvoip_INCLUDE_DIRS=${STAGING_INCDIR}/tgvoip \
    -Dtgvoip_LIBRARIES='tgvoip;opus;asound;crypto' \
    -Dlunaservice_INCLUDE_DIRS='${STAGING_INCDIR}/luna-service2;${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/common' \
    -Dlunaservice_LIBRARIES=luna-service2 \
"

# Video calling is still absent on LuneOS regardless of the above: voipkit.cpp's real backend is
# libpalmgstskype.so, a gstreamer-0.10 plugin that exists only in legacy webOS firmware. CMake
# leaves it unlinked unless SkypeKitFwRootfs is set, which it is not here, so the bridge builds
# against the null backend. Voice calling is unaffected.

# CMakeLists installs two things webOS has no use for, and unlike the other plugin recipes here
# this one runs a real `make install`, so they have to be removed after the fact rather than simply
# never installed. Patching CMakeLists is not an option: it is shared with the device build.
#
#   pixmaps  Pidgin protocol icons in ${datadir}/pixmaps/pidgin/protocols/{16,22,48}. There is no
#            Pidgin GTK UI here -- libpurple is loaded headless by imlibpurpletransport and account
#            icons come from the account template -- and no plugin recipe in this directory ships
#            them (see purple-synergy.inc).
#   metainfo AppStream metadata for software centres. It would also fail packaging outright:
#            ${datadir}/metainfo is not in the default FILES:${PN}, so it lands as an
#            installed-but-not-shipped QA error rather than being quietly ignored.
do_install:append() {
    rm -rf ${D}${datadir}/pixmaps
    rm -rf ${D}${datadir}/metainfo

    # Account template, so Telegram can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/telegram/account/com.palm.telegram ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/telegram/apps/com.palm.app.telegram ${D}${webos_applicationsdir}/
}
