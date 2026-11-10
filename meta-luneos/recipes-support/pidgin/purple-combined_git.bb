SUMMARY = "WhatsApp, Facebook (Messenger E2EE) and Matrix protocol plug-ins for libpurple"
DESCRIPTION = "One shared object hosting three prpls: prpl-hehoe-whatsmeow (WhatsApp, via \
whatsmeow), prpl-gometa (Facebook Messenger, via messagix) and prpl-matrix (Matrix, via \
mautrix-go). They share a single Go runtime, which is why they are built together rather than \
as separate plugins."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-connectors.inc

# Built from luneos-synergy-connectors, the version ended in its commit alone (1.0+git0+d22c6be58f).
# With the upstream commit now in front it can sort lower, so bump the epoch to keep package feeds
# moving forward.
PE = "4"

# Three upstreams, unpacked side by side:
#
#   purple-gowhatsapp   the WhatsApp prpl, one feature per patch. 0001-0005 are generic fixes sent
#                       upstream (pull requests 297, 298, 299, 303 and 310), 0006-0012 and
#                       0016-0022 the webOS Messaging integration, 0013 the calling integration
#                       (voice and video through the Phone app). 0014 adds prpl-gometa (Facebook
#                       Messenger on mautrix-meta's messagix) and 0015 prpl-matrix (Matrix on
#                       mautrix-go, with end-to-end encryption): LuneOS work, not part of
#                       purple-gowhatsapp, which has to live in this .so because two Go c-archives
#                       cannot share one process. 0021 is the rest of the Facebook integration.
#   meowcaller          WhatsApp calling, which go.mod replaces with ./third_party/meowcaller.
#                       main imports whatsmeow as github.com/polymorfa/hypermeow, a renamed copy;
#                       meowcaller/0001 points it back at go.mau.fi/whatsmeow, the module
#                       purple-gowhatsapp builds with. 0002-0005 are the ARMv7 encoder speed-ups,
#                       the video path fixes and the keyframe request; 0006 fixes multi-device
#                       calls, relay election, video orientation, camera off and video RTP that
#                       arrives out of order. 0007 accepts the peer's MLOW audio on payload type
#                       122, where an Android peer sends it in a video call we place.
#
# Echo cancellation (glue/aec.cc) is webrtc-audio-processing-1's AudioProcessing in mobile mode.
#
# The parent checkout must come before meowcaller in SRC_URI: unpacking a git source clears its
# destination, so meowcaller nested inside it has to be unpacked afterwards.
SRC_URI += " \
    git://github.com/hoehermann/purple-gowhatsapp.git;branch=whatsmeow;protocol=https;name=upstream;destsuffix=purple-combined \
    git://github.com/purpshell/meowcaller.git;branch=main;protocol=https;name=meowcaller;destsuffix=purple-combined/third_party/meowcaller \
    file://0001-Show-the-quote-when-the-quoted-message-is-an-extende.patch \
    file://0002-Do-not-dereference-a-missing-device-ID-when-pruning-.patch \
    file://0003-Drop-profile-picture-requests-while-disconnected.patch \
    file://0004-Print-the-message-timestamp-with-a-64-bit-format.patch \
    file://0005-Give-sent-documents-a-file-name.patch \
    file://0006-webOS-show-reactions-on-the-message-they-react-to.patch \
    file://0007-webOS-report-sent-message-ids-and-delivery-and-read-.patch \
    file://0008-webOS-update-edited-and-deleted-messages-in-place-an.patch \
    file://0009-webOS-show-locations-contact-cards-events-link-previ.patch \
    file://0010-webOS-show-polls-and-vote-on-them-from-the-app.patch \
    file://0011-webOS-show-WhatsApp-Channels.patch \
    file://0012-webOS-store-and-cache-defaults-device-removal-messag.patch \
    file://0013-webOS-WhatsApp-voice-and-video-calls-through-the-Pho.patch \
    file://0014-webOS-add-a-Facebook-Messenger-protocol-prpl-gometa-.patch \
    file://0015-webOS-add-a-Matrix-protocol-prpl-matrix-to-the-plugi.patch \
    file://0016-webOS-send-read-receipts-once-a-chat-is-read.patch \
    file://0017-webOS-count-WhatsApp-poll-votes-on-the-poll-one-vote.patch \
    file://0018-webOS-edit-delete-groups-places-contact-cards-and-me.patch \
    file://0019-webOS-keep-the-id-of-what-we-send-into-a-WhatsApp-gr.patch \
    file://0020-webOS-keep-a-received-WhatsApp-sticker-as-WebP.patch \
    file://0021-webOS-Facebook-typing-receipts-edits-deletes-rich-se.patch \
    file://0022-webOS-show-the-old-WhatsApp-messages-the-phone-sends.patch \
    file://meowcaller/0001-Build-against-upstream-whatsmeow.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0002-mlow-speed-up-the-encoder-for-32-bit-ARM.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0003-Fix-the-video-call-media-path-against-live-WhatsApp-.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0004-webOS-mlow-adapt-encoder-complexity-to-the-frame-tim.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0005-webOS-let-the-receiver-ask-the-peer-for-a-keyframe.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0006-webOS-fix-multi-device-calls-relay-election-video-or.patch;patchdir=third_party/meowcaller \
    file://meowcaller/0007-rtp-accept-MLOW-on-its-separate-payload-type-122.patch;patchdir=third_party/meowcaller \
"
SRCREV_upstream = "a8dc1702c22c206dc8ed068e596f986b20839b16"
SRCREV_meowcaller = "c48c3e2a243c672c942cbf0c941d14161720d540"
SRCREV_FORMAT = "upstream_meowcaller_connectors"

# luna-service2 is for glue/call.c (com.palm.whatsapp.call). It registers one handle with
# LSRegister: LuneOS has a single bus, so the public/private split of legacy webOS is not needed.
#
# alsa-lib is also glue/call.c: call audio is captured and played through ALSA directly.
#
# opusfile is NOT optional in practice. opusreader.c gates itself on `#if __has_include("opusfile.h")`
# and, when absent, compiles to a stub with
#     #pragma message "Warning: Building without opusfile. Sending voice messages is disabled."
# -- a warning, not an error, so the build stays green and voice-note sending silently disappears
# from the finished plugin. It provides the duration and waveform that send_file.go/send_message.go/
# gometa_media.go attach to outgoing voice notes.
#
# webrtc-audio-processing-1 is glue/aec.cc's echo canceller.
DEPENDS = "pidgin glib-2.0 libopus libogg opusfile alsa-lib luna-service2 webrtc-audio-processing-1"

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

# The video bridge. glue/voipkit.cpp binds libpalmgstskype.so, a gstreamer-0.10 plugin that
# exists only in legacy webOS firmware, and glue/voipkit_none.c is a NULL backend. 0013 adds
# glue/voipkit_rtcengine.c, which hands the call's H.264 to luneos-rtc-engine
# (packagegroup-luneos-extended) to decode and, where there is a camera and an encoder, encode.
# Without the engine on the device a video call stays voice-only.
VOIPKIT_GLUE = "voipkit_rtcengine"

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
    #
    # -tags goolm: Matrix's end-to-end encryption (0015) uses mautrix-go's pure-Go Olm. Without
    # the tag mautrix/crypto binds libolm through cgo instead, which is deprecated upstream and not
    # in this layer set.
    ${GO} build -tags goolm -trimpath -modcacherw -buildmode=c-archive -o ${B}/libwhatsmeow.a .

    # -DPURPLE_PLUGINS is belt-and-braces rather than required:
    # plugin.h does switch PURPLE_INIT_PLUGIN on it (unset selects the static-prpl entry point
    # purple_init_whatsmeow_plugin instead of the G_MODULE_EXPORTed purple_init_plugin libpurple
    # looks up after dlopen), yet purple.h itself does `#define PURPLE_PLUGINS 1` at line 46, so any
    # translation unit that reaches plugin.h through purple.h already has it. Verified both ways --
    # the symbol is purple_init_plugin with and without the flag.
    #
    # -DPLUGIN_VERSION, by contrast, IS required: glue/init.c #errors without it.
    GCFLAGS="${CFLAGS} -fPIC -DPURPLE_PLUGINS -DPLUGIN_VERSION=$PLUGIN_VERSION \
             -I${S}/glue -I${S} -I${B} \
             $(pkg-config --cflags purple glib-2.0 opus opusfile ogg alsa luna-service2)"

    # 2. the C glue, including the webOS files 0013 and 0014 add.
    OBJS=""
    for s in init login qrcode bridge process_message display_message groups blist \
             send_message handle_attachment send_file presence options receipt pixbuf commands \
             call playout denoise h264_rtp gometa_init matrix_init ${VOIPKIT_GLUE}; do
        ${CC} ${GCFLAGS} -c ${S}/glue/$s.c -o ${B}/glue_$s.o
        OBJS="$OBJS ${B}/glue_$s.o"
    done
    for s in bridge constants gometabridge matrixbridge; do
        ${CC} ${GCFLAGS} -c ${S}/$s.c -o ${B}/root_$s.o
        OBJS="$OBJS ${B}/root_$s.o"
    done

    # 2b. the echo canceller, C++ against webrtc-audio-processing-1.
    ${CXX} ${CXXFLAGS} -std=c++17 -fPIC -I${S}/glue $(pkg-config --cflags webrtc-audio-processing-1) \
        -c ${S}/glue/aec.cc -o ${B}/glue_aec.o
    OBJS="$OBJS ${B}/glue_aec.o"

    # 3. link. -Bsymbolic-functions: every plug-in is dlopen'd into the same process, so without it
    # ELF interposition can route this plug-in's calls to its own functions (the voipkit_* bridge,
    # the wa_aec_* wrapper) into a same-named copy in another plug-in, depending on load order.
    ${CC} -shared -fPIC ${LDFLAGS} -Wl,-Bsymbolic-functions \
        -Wl,-soname,libwhatsmeow.so -o ${B}/libwhatsmeow.so \
        $OBJS ${B}/libwhatsmeow.a \
        $(pkg-config --libs purple glib-2.0 opus opusfile ogg alsa luna-service2 webrtc-audio-processing-1) \
        -lpthread -ldl -lm -lresolv -lstdc++

    # Fail loudly here rather than shipping a plugin libpurple will quietly refuse to load. A .so
    # missing its entry point or a prpl id builds, links and packages perfectly happily; nothing before runtime notices.
    if ! ${NM} -D --defined-only ${B}/libwhatsmeow.so | grep -q ' purple_init_plugin$'; then
        bbfatal "libwhatsmeow.so exports no purple_init_plugin; libpurple would reject it at runtime"
    fi
    for id in prpl-hehoe-whatsmeow prpl-gometa prpl-matrix; do
        grep -q "$id" ${B}/libwhatsmeow.so || bbfatal "libwhatsmeow.so is missing prpl id $id"
    done
}

# S is only the purple-gowhatsapp checkout, so OE's automatic ${S} prefix-map does not cover the
# luneos-synergy-connectors headers this recipe also compiles, and their paths would survive into
# the debug info:
#   File /usr/lib/purple-2/.debug/libwhatsmeow.so in package purple-combined-dbg
#   contains reference to TMPDIR [buildpaths]
# Map the whole unpack directory instead.
DEBUG_PREFIX_MAP:append = " -ffile-prefix-map=${UNPACKDIR}=/usr/src/debug/${PN}/${PV}"


do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${B}/libwhatsmeow.so ${D}${libdir}/purple-2/libwhatsmeow.so

    # Three account templates, because this one .so registers three prpls: com.palm.whatsapp for
    # prpl-hehoe-whatsmeow, com.palm.gometa for prpl-gometa and com.palm.matrix for prpl-matrix.
    # Without them the protocols are loaded but cannot be added as accounts.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/whatsapp/account/com.palm.whatsapp ${D}${webos_accttemplatesdir}/
    cp -rf ${CONNECTORS_DIR}/im/facebook/account/com.palm.gometa ${D}${webos_accttemplatesdir}/
    cp -rf ${CONNECTORS_DIR}/im/matrix/account/com.palm.matrix ${D}${webos_accttemplatesdir}/

    # ...and the validator apps those templates name in validator.customUI.appId. Without them the
    # account type appears but selecting it finds no validator.html.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/whatsapp/apps/com.palm.app.whatsapp ${D}${webos_applicationsdir}/
    cp -rf ${CONNECTORS_DIR}/im/facebook/apps/com.palm.app.gometa ${D}${webos_applicationsdir}/
    cp -rf ${CONNECTORS_DIR}/im/matrix/apps/com.palm.app.matrix ${D}${webos_applicationsdir}/

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

# prpl-matrix replaces matrix-org's purple-matrix (no end-to-end encryption, unmaintained), which
# registered the same protocol id: libpurple keeps whichever of the two loads first, so they
# cannot be installed together.
RREPLACES:${PN} += "purple-matrix"
RCONFLICTS:${PN} += "purple-matrix"
