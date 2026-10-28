SUMMARY = "WhatsApp and Facebook (Messenger E2EE) protocol plug-ins for libpurple"
DESCRIPTION = "One shared object hosting two prpls: prpl-hehoe-whatsmeow (WhatsApp, via \
whatsmeow) and prpl-gometa (Facebook Messenger, via messagix). They share a single Go runtime, \
which is why they are built together rather than as two plugins."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-connectors.inc

# Built from luneos-synergy-connectors, the version ended in its commit alone (1.0+git0+d22c6be58f).
# With the upstream commit now in front it can sort lower, so bump the epoch to keep package feeds
# moving forward.
PE = "1"

# Three upstreams, unpacked side by side:
#
#   purple-gowhatsapp   the WhatsApp prpl. 0001-0005 are generic fixes meant for upstream, 0006-0007
#                       the webOS Messaging and calling integration, 0008 adds prpl-gometa (Facebook
#                       Messenger on mautrix-meta's messagix), which is LuneOS work and not part of
#                       purple-gowhatsapp. It has to live in this .so: two Go c-archives cannot
#                       share one process.
#   meowcaller          WhatsApp calling, which go.mod replaces with ./third_party/meowcaller.
#                       Kept at 9769d5a: later main moved to a whatsmeow fork and reworked video,
#                       which our meowcaller/0002 conflicts with.
#   libtgvoip           only for its webrtc_dsp sources (noise suppression and AECM, see
#                       WEBRTC_DSP below), with the same GCC 15 <stdint.h> fix libtgvoip_git.bb uses.
#
# The parent checkout must come before meowcaller in SRC_URI: unpacking a git source clears its
# destination, so meowcaller nested inside it has to be unpacked afterwards.
FILESEXTRAPATHS:prepend := "${THISDIR}/libtgvoip:"
SRC_URI += " \
    git://github.com/hoehermann/purple-gowhatsapp.git;branch=whatsmeow;protocol=https;name=upstream;destsuffix=purple-combined \
    git://github.com/purpshell/meowcaller.git;branch=main;protocol=https;name=meowcaller;destsuffix=purple-combined/third_party/meowcaller \
    git://github.com/grishka/libtgvoip.git;branch=public;protocol=https;name=libtgvoip;destsuffix=libtgvoip \
    file://0001-Initialise-the-GError-used-when-saving-a-profile-pic.patch \
    file://0002-Guard-attachment-path-templating-against-NULL-values.patch \
    file://0003-Keep-the-plugin-module-resident.patch \
    file://0004-Set-the-group-subject-when-group-info-loads-on-the-f.patch \
    file://0005-Download-attachments-into-memory-with-a-timeout.patch \
    file://0006-webOS-integrate-with-the-LuneOS-Messaging-app.patch \
    file://0007-webOS-WhatsApp-voice-and-video-calls-through-the-Pho.patch \
    file://0008-webOS-add-a-Facebook-Messenger-protocol-prpl-gometa-.patch \
    file://meowcaller/0001-mlow-speed-up-the-encoder-for-32-bit-ARM.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0002-Fix-the-video-call-media-path-against-live-WhatsApp-.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0003-webOS-mlow-adapt-encoder-complexity-to-the-frame-tim.patch;patchdir=third_party/meowcaller \
    file://0002-Include-the-fixed-width-integer-headers-where-they-a.patch;patchdir=../libtgvoip \
"
SRCREV_upstream = "1078b14c6dcdd862f374a1b2c812d7f0cc4016cb"
SRCREV_meowcaller = "9769d5aaaeca43f6c69d612a913aeb10298b2c07"
SRCREV_libtgvoip = "6c82c9de85cca69ea099a738fcbd2ab2de4f839d"
SRCREV_FORMAT = "upstream_meowcaller_libtgvoip_connectors"

# luna-service2 is for glue/call.c (com.palm.whatsapp.call). It compiles against 3.21.2 thanks to
# im/_core/include/webos-ls2-compat.h from luneos-synergy-connectors, which re-expresses the legacy split-bus API -- LSPalmService
# and friends were removed outright in 3.x.
#
# alsa-lib is also glue/call.c: call audio is captured and played through ALSA directly.
#
# opusfile is NOT optional in practice. opusreader.c gates itself on `#if __has_include("opusfile.h")`
# and, when absent, compiles to a stub with
#     #pragma message "Warning: Building without opusfile. Sending voice messages is disabled."
# -- a warning, not an error, so the build stays green and voice-note sending silently disappears
# from the finished plugin. It provides the duration and waveform that send_file.go/send_message.go/
# gometa_media.go attach to outgoing voice notes.
DEPENDS = "pidgin glib-2.0 libopus libogg opusfile alsa-lib luna-service2"

S = "${UNPACKDIR}/purple-combined"
# B defaults to ${WORKDIR}/build in go.bbclass; left at the default.

# `go`, deliberately not `go-mod`. go-mod adds
#     do_compile[dirs] += "${B}/src/${GO_WORKDIR}"
# and bitbake chdirs into the last [dirs] entry before running the task. That path only exists
# under the GOPATH relocation go_do_unpack performs -- which the explicit destsuffix on every git
# source in SRC_URI disables, so that S lands where it is expected. With the relocation off,
# nothing creates that directory and the task dies as FileNotFoundError before writing any log,
# which is a confusing way to learn this.
#
# The module handling go-mod would have contributed is two lines, reproduced below.
inherit go

GO_IMPORT = "github.com/hoehermann/purple-gowhatsapp"

export GOMODCACHE = "${B}/.mod"
do_compile[cleandirs] += "${B}/.mod"

# Go modules follow what this layer set already does for Go recipes (influxdb, etcd, syzkaller):
# fetch through GOPROXY during do_compile, with that task granted network access, which bitbake
# otherwise denies outside do_fetch.
#
# Vendoring was the alternative and was measured rather than assumed: `go mod vendor` on this
# module produces 172 MB, too much to carry in the source repo for every checkout. If a fully
# offline build is ever required, vendor into a separate artifact and add -mod=vendor here.
export GOPROXY = "https://proxy.golang.org,direct"
do_compile[network] = "1"

# The video bridge is the NULL backend. glue/voipkit.cpp binds libpalmgstskype.so, a
# gstreamer-0.10 plugin that exists only in legacy webOS firmware; glue/voipkit_none.c stands in
# so the plugin links without it. Messaging and voice calling are unaffected -- video is what is
# missing, and a LuneOS video backend still has to be written.
VOIPKIT_GLUE = "voipkit_none"

do_compile() {
    cd ${S}

    # The plugin version is a build input, not a recipe constant: glue/init.c has
    #     #ifndef PLUGIN_VERSION
    #     #error Must set PLUGIN_VERSION in build system
    # and both halves stamp it into their PurplePluginInfo.version. Read it from upstream's VERSION
    # file, as its own CMake build does.
    PLUGIN_VERSION=$(cat ${S}/VERSION)

    # Upstream's go.mod asks for go 1.27.2; this release ships an older go, and no dependency
    # needs more than 1.26.0. Lower the requirement rather than let go download a toolchain.
    export GOTOOLCHAIN=local
    ${GO} mod edit -go=1.26.0

    # cgo needs libpurple's own include directory. go.bbclass exports CGO_CFLAGS as bare ${CFLAGS},
    # which carries the sysroot and the cross settings but nothing package-specific, so the very
    # first cgo file fails on gometabridge.h's `#include <purple.h>`. Append rather than replace --
    # what go.bbclass put there is what makes this a cross build at all.
    export CGO_CFLAGS="$CGO_CFLAGS $(pkg-config --cflags purple glib-2.0 opus opusfile ogg) -DPLUGIN_VERSION=$PLUGIN_VERSION -D_DEFAULT_SOURCE"
    export CGO_LDFLAGS="$CGO_LDFLAGS $(pkg-config --libs purple glib-2.0 opus opusfile ogg)"

    # 1. the Go half as a c-archive, plus its generated header.
    # GOBUILDFLAGS is deliberately not used: go.bbclass builds it around GO_LDFLAGS, which is an
    # -ldflags= string for linking executables (-linkmode, -extldflags, the dynamic loader). None
    # of that applies to -buildmode=c-archive, where the C toolchain does the final link. The
    # cross settings that DO matter -- CC, CGO_ENABLED, CGO_CFLAGS, GOARCH -- come from the
    # environment the class exports, not from these flags.
    ${GO} build -trimpath -modcacherw -buildmode=c-archive -o ${B}/libwhatsmeow.a .

    # -DPURPLE_PLUGINS is belt-and-braces rather than required:
    # plugin.h does switch PURPLE_INIT_PLUGIN on it (unset selects the static-prpl entry point
    # purple_init_whatsmeow_plugin instead of the G_MODULE_EXPORTed purple_init_plugin libpurple
    # looks up after dlopen), yet purple.h itself does `#define PURPLE_PLUGINS 1` at line 46, so any
    # translation unit that reaches plugin.h through purple.h already has it. Verified both ways --
    # the symbol is purple_init_plugin with and without the flag.
    #
    # -DPLUGIN_VERSION, by contrast, IS required: glue/init.c #errors without it.
    #
    # luna-service2 needs its own -I on top of pkg-config: the .pc gives only -I${includedir}, while
    # the header sits at ${includedir}/luna-service2/lunaservice.h and webos-ls2-compat.h includes it
    # bare as <lunaservice.h>.
    GCFLAGS="${CFLAGS} -fPIC -DPURPLE_PLUGINS -DPLUGIN_VERSION=$PLUGIN_VERSION \
             -I${S}/glue -I${S} -I${B} -I${CONNECTORS_DIR}/im/_core/include \
             -I${STAGING_INCDIR}/luna-service2 \
             $(pkg-config --cflags purple glib-2.0 opus opusfile ogg alsa luna-service2)"

    # 2. the C glue, including the webOS files 0006-0007 add.
    OBJS=""
    for s in init login qrcode bridge process_message display_message groups blist \
             send_message handle_attachment send_file presence options receipt pixbuf commands \
             call denoise aec h264_rtp gometa_init ${VOIPKIT_GLUE}; do
        ${CC} ${GCFLAGS} -I${WEBRTC_DSP} -c ${S}/glue/$s.c -o ${B}/glue_$s.o
        OBJS="$OBJS ${B}/glue_$s.o"
    done
    for s in bridge constants gometabridge; do
        ${CC} ${GCFLAGS} -c ${S}/$s.c -o ${B}/root_$s.o
        OBJS="$OBJS ${B}/root_$s.o"
    done

    # 2b. WebRTC float noise suppression + AECM, for glue/denoise.c and glue/aec.c. The source
    # lists are WEBRTC_NS_SRCS and WEBRTC_AEC_SRCS below.
    NS_SRCS="${WEBRTC_NS_SRCS}"
    AEC_SRCS="${WEBRTC_AEC_SRCS}"
    for s in $NS_SRCS; do
        # ${DEBUG_PREFIX_MAP} rather than ${CFLAGS}: these two compiles deliberately use a
        # minimal flag set, but without the prefix map the debug info records absolute build
        # paths and buildpaths QA rejects the result:
        #   File /usr/lib/purple-2/.debug/libwhatsmeow.so in package purple-combined-dbg
        #   contains reference to TMPDIR [buildpaths]
        ${CC} -O2 -fPIC -std=gnu11 ${DEBUG_PREFIX_MAP} -DNDEBUG -DWEBRTC_POSIX -DWEBRTC_APM_DEBUG_DUMP=0 \
              -DWEBRTC_NS_FLOAT -I${WEBRTC_DSP} -c ${WEBRTC_DSP}/$s -o ${B}/ns_$(echo $s | tr / _).o
        OBJS="$OBJS ${B}/ns_$(echo $s | tr / _).o"
    done
    for s in $AEC_SRCS; do
        ${CXX} -O2 -fPIC -std=c++11 ${DEBUG_PREFIX_MAP} -DNDEBUG -DWEBRTC_POSIX -DWEBRTC_APM_DEBUG_DUMP=0 \
               -I${WEBRTC_DSP} -c ${WEBRTC_DSP}/$s -o ${B}/aec_$(echo $s | tr / _).o
        OBJS="$OBJS ${B}/aec_$(echo $s | tr / _).o"
    done

    # 3. link. -Bsymbolic-functions: this plugin and purple-teams both define the identically
    # named voipkit_* bridge symbols and are dlopen'd into the same process, so without it ELF
    # interposition can route one plugin's calls into the other's copy depending on load order.
    ${CC} -shared -fPIC ${LDFLAGS} -Wl,-Bsymbolic-functions \
        -Wl,-soname,libwhatsmeow.so -o ${B}/libwhatsmeow.so \
        $OBJS ${B}/libwhatsmeow.a \
        $(pkg-config --libs purple glib-2.0 opus opusfile ogg alsa luna-service2) \
        -lpthread -ldl -lm -lresolv -lstdc++

    # Fail loudly here rather than shipping a plugin libpurple will quietly refuse to load. A .so
    # missing its entry point or a prpl id builds, links and packages perfectly happily; nothing before runtime notices.
    if ! ${NM} -D --defined-only ${B}/libwhatsmeow.so | grep -q ' purple_init_plugin$'; then
        bbfatal "libwhatsmeow.so exports no purple_init_plugin; libpurple would reject it at runtime"
    fi
    for id in prpl-hehoe-whatsmeow prpl-gometa; do
        grep -q "$id" ${B}/libwhatsmeow.so || bbfatal "libwhatsmeow.so is missing prpl id $id"
    done
}

# The WebRTC DSP sources come from libtgvoip's tree, fetched alongside -- see SRC_URI. These are
# the noise-suppression and AECM translation units glue/denoise.c and glue/aec.c need.
WEBRTC_NS_SRCS = " \
    common_audio/signal_processing/auto_correlation.c \
    common_audio/signal_processing/auto_corr_to_refl_coef.c \
    common_audio/signal_processing/complex_fft.c \
    common_audio/signal_processing/copy_set_operations.c \
    common_audio/signal_processing/cross_correlation.c \
    common_audio/signal_processing/division_operations.c \
    common_audio/signal_processing/downsample_fast.c \
    common_audio/signal_processing/energy.c \
    common_audio/signal_processing/filter_ar.c \
    common_audio/signal_processing/filter_ar_fast_q12.c \
    common_audio/signal_processing/filter_ma_fast_q12.c \
    common_audio/signal_processing/get_hanning_window.c \
    common_audio/signal_processing/get_scaling_square.c \
    common_audio/signal_processing/ilbc_specific_functions.c \
    common_audio/signal_processing/levinson_durbin.c \
    common_audio/signal_processing/lpc_to_refl_coef.c \
    common_audio/signal_processing/min_max_operations.c \
    common_audio/signal_processing/randomization_functions.c \
    common_audio/signal_processing/real_fft.c \
    common_audio/signal_processing/refl_coef_to_lpc.c \
    common_audio/signal_processing/resample_48khz.c \
    common_audio/signal_processing/resample_by_2.c \
    common_audio/signal_processing/resample_by_2_internal.c \
    common_audio/signal_processing/resample.c \
    common_audio/signal_processing/resample_fractional.c \
    common_audio/signal_processing/spl_init.c \
    common_audio/signal_processing/spl_inl.c \
    common_audio/signal_processing/splitting_filter1.c \
    common_audio/signal_processing/spl_sqrt.c \
    common_audio/signal_processing/sqrt_of_one_minus_x_squared.c \
    common_audio/signal_processing/vector_scaling_operations.c \
    common_audio/signal_processing/dot_product_with_scale.cc \
    common_audio/third_party/fft4g/fft4g.c \
    common_audio/signal_processing/complex_bit_reverse.c \
    common_audio/ring_buffer.c \
    common_audio/third_party/spl_sqrt_floor/spl_sqrt_floor.c \
    modules/audio_processing/ns/ns_core.c \
    modules/audio_processing/ns/noise_suppression.c \
"
WEBRTC_AEC_SRCS = " \
    modules/audio_processing/aecm/aecm_core.cc \
    modules/audio_processing/aecm/aecm_core_c.cc \
    modules/audio_processing/aecm/echo_control_mobile.cc \
    modules/audio_processing/utility/delay_estimator.cc \
    modules/audio_processing/utility/delay_estimator_wrapper.cc \
"
WEBRTC_DSP = "${UNPACKDIR}/libtgvoip/webrtc_dsp"

# S is only the purple-gowhatsapp checkout, so OE's automatic ${S} prefix-map does not cover the
# luneos-synergy-connectors headers or the libtgvoip webrtc_dsp sources this recipe also compiles,
# and their paths would survive into the debug info:
#   File /usr/lib/purple-2/.debug/libwhatsmeow.so in package purple-combined-dbg
#   contains reference to TMPDIR [buildpaths]
# Map the whole unpack directory instead.
DEBUG_PREFIX_MAP:append = " -ffile-prefix-map=${UNPACKDIR}=/usr/src/debug/${PN}/${PV}"


do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${B}/libwhatsmeow.so ${D}${libdir}/purple-2/libwhatsmeow.so

    # Two account templates, because this one .so registers two prpls: com.palm.whatsapp for
    # prpl-hehoe-whatsmeow and com.palm.gometa for prpl-gometa. Without them the protocols are
    # loaded but cannot be added as accounts.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/whatsapp/account/com.palm.whatsapp ${D}${webos_accttemplatesdir}/
    cp -rf ${CONNECTORS_DIR}/im/facebook/account/com.palm.gometa ${D}${webos_accttemplatesdir}/

    # ...and the validator apps those templates name in validator.customUI.appId. Without them the
    # account type appears but selecting it finds no validator.html.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/whatsapp/apps/com.palm.app.whatsapp ${D}${webos_applicationsdir}/
    cp -rf ${CONNECTORS_DIR}/im/facebook/apps/com.palm.app.gometa ${D}${webos_applicationsdir}/

    # The whatsapp call service (glue/call.c builds it): its bus name is listed, never activated --
    # imlibpurpleservice, which loads this plug-in, registers com.palm.whatsapp.call itself, so the
    # .service file is Type=static -- and the Phone app is granted imlibpurple-call.operation to
    # call it. The role letting imlibpurpleservice own the name, and the api-permissions/groups
    # behind that group, ship with imlibpurpleservice. webos_configure_manifest lists both files
    # in this package's manifest.
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_permissionsdir}
    install -m 0644 ${CONNECTORS_DIR}/im/whatsapp/files/sysbus/com.palm.whatsapp.call.service \
        ${D}${webos_sysbus_servicedir}/
    install -m 0644 ${CONNECTORS_DIR}/im/whatsapp/files/sysbus/com.palm.whatsapp.call.perm.json \
        ${D}${webos_sysbus_permissionsdir}/
}
