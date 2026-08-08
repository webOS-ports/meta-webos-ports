SUMMARY = "WhatsApp and Facebook (Messenger E2EE) protocol plug-ins for libpurple"
DESCRIPTION = "One shared object hosting two prpls: prpl-hehoe-whatsmeow (WhatsApp, via \
whatsmeow) and prpl-gometa (Facebook Messenger, via messagix). They share a single Go runtime, \
which is why they are built together rather than as two plugins."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-synergy.inc

# luna-service2 is for glue/call.c (com.palm.whatsapp.call). It compiles against 3.21.2 thanks to
# messaging/common/webos-ls2-compat.h, which re-expresses the legacy split-bus API -- LSPalmService
# and friends were removed outright in 3.x.
DEPENDS = "pidgin glib-2.0 libopus libogg luna-service2"

S = "${WORKDIR}/git/messaging/facebook-e2ee/plugin/purple-combined"
# B defaults to ${WORKDIR}/build in go.bbclass; left at the default.

# `go`, deliberately not `go-mod`. go-mod adds
#     do_compile[dirs] += "${B}/src/${GO_WORKDIR}"
# and bitbake chdirs into the last [dirs] entry before running the task. That path only exists
# under the GOPATH relocation go_do_unpack performs -- which purple-synergy.inc disables with
# destsuffix=git so that S lands where every other recipe here expects. With the relocation off,
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

    # 1. the Go half as a c-archive, plus its generated header.
    # GOBUILDFLAGS is deliberately not used: go.bbclass builds it around GO_LDFLAGS, which is an
    # -ldflags= string for linking executables (-linkmode, -extldflags, the dynamic loader). None
    # of that applies to -buildmode=c-archive, where the C toolchain does the final link. The
    # cross settings that DO matter -- CC, CGO_ENABLED, CGO_CFLAGS, GOARCH -- come from the
    # environment the class exports, not from these flags. build-combined.sh invokes go the same
    # bare way.
    ${GO} build -trimpath -modcacherw -buildmode=c-archive -o ${B}/libwhatsmeow.a .

    GCFLAGS="${CFLAGS} -fPIC -I${S}/glue -I${S} -I${B} -I${WORKDIR}/git/messaging/common \
             $(pkg-config --cflags purple glib-2.0 opus ogg)"

    # 2. the C glue. This list mirrors build-combined.sh; keep the two in step.
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
    # lists live in build-combined.sh (NS_SRCS/AEC_SRCS); they are read from there rather than
    # copied, so the two cannot drift apart.
    NS_SRCS=$(sed -n '/^NS_SRCS=(/,/^)/p' ${S}/build-combined.sh | grep -oE "[a-z_0-9/]+\.(c|cc)")
    AEC_SRCS=$(sed -n '/^AEC_SRCS=(/,/^)/p' ${S}/build-combined.sh | grep -oE "[a-z_0-9/]+\.(c|cc)")
    for s in $NS_SRCS; do
        ${CC} -O2 -fPIC -std=gnu11 -DNDEBUG -DWEBRTC_POSIX -DWEBRTC_APM_DEBUG_DUMP=0 \
              -DWEBRTC_NS_FLOAT -I${WEBRTC_DSP} -c ${WEBRTC_DSP}/$s -o ${B}/ns_$(echo $s | tr / _).o
        OBJS="$OBJS ${B}/ns_$(echo $s | tr / _).o"
    done
    for s in $AEC_SRCS; do
        ${CXX} -O2 -fPIC -std=c++11 -DNDEBUG -DWEBRTC_POSIX -DWEBRTC_APM_DEBUG_DUMP=0 \
               -I${WEBRTC_DSP} -c ${WEBRTC_DSP}/$s -o ${B}/aec_$(echo $s | tr / _).o
        OBJS="$OBJS ${B}/aec_$(echo $s | tr / _).o"
    done

    # 3. link. -Bsymbolic-functions: this plugin and purple-teams both define the identically
    # named voipkit_* bridge symbols and are dlopen'd into the same process, so without it ELF
    # interposition can route one plugin's calls into the other's copy depending on load order.
    ${CC} -shared -fPIC ${LDFLAGS} -Wl,-Bsymbolic-functions \
        -Wl,-soname,libwhatsmeow.so -o ${B}/libwhatsmeow.so \
        $OBJS ${B}/libwhatsmeow.a \
        $(pkg-config --libs purple glib-2.0 opus ogg) \
        -lpthread -ldl -lm -lresolv -lstdc++
}

# The WebRTC DSP sources live in the Telegram plug-in's libtgvoip copy, in the same monorepo
# checkout -- purple-combined reuses them rather than carrying a second copy.
WEBRTC_DSP = "${WORKDIR}/git/messaging/telegram/plugin/libtgvoip/webrtc_dsp"

do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${B}/libwhatsmeow.so ${D}${libdir}/purple-2/libwhatsmeow.so
}
