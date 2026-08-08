SUMMARY = "WhatsApp and Facebook (Messenger E2EE) protocol plug-ins for libpurple"
DESCRIPTION = "One shared object hosting two prpls: prpl-hehoe-whatsmeow (WhatsApp, via \
whatsmeow) and prpl-gometa (Facebook Messenger, via messagix). They share a single Go runtime, \
which is why they are built together rather than as two plugins."
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

require purple-synergy.inc

DEPENDS = "pidgin glib-2.0 libopus libogg"

S = "${WORKDIR}/git/messaging/facebook-e2ee/plugin/purple-combined"
B = "${WORKDIR}/build"

inherit go-mod

GO_IMPORT = "github.com/hoehermann/purple-gowhatsapp"

# Go module handling follows what the rest of this layer set already does for Go recipes
# (influxdb, etcd, syzkaller): let the go tool fetch modules through GOPROXY during do_compile,
# and grant that task network access, which bitbake otherwise denies outside do_fetch.
#
# The alternative is vendoring. It was measured rather than guessed: `go mod vendor` on this
# module produces 172 MB, which is not something to carry in the source repo for every checkout.
# If a fully offline build is ever needed, vendor into a separate artifact and add -mod=vendor
# here rather than committing it upstream.
export GOPROXY = "https://proxy.golang.org,direct"
do_compile[network] = "1"

# Three stages, mirroring build-combined.sh:
#   1. cgo builds the Go half as a c-archive plus its generated header
#   2. the C glue compiles against that header
#   3. everything links into one plugin
#
# The video bridge is deliberately the NULL backend here. glue/voipkit.cpp binds
# libpalmgstskype.so, a gstreamer-0.10 plugin that exists only in legacy webOS firmware;
# glue/voipkit_none.c is the stand-in that lets the plugin link without it (WA_VOIPKIT=0 in
# build-combined.sh). Messaging and voice calling are unaffected -- video is what is missing, and
# a real LuneOS backend still has to be written.
#
# Also not built: the com.palm.whatsapp.call LS2 service in glue/call.c. Turning it on needs
# DEPENDS += "luna-service2" and -I${WORKDIR}/git/messaging/common for webos-ls2-compat.h, which
# maps the legacy split-bus API onto the single bus luna-service2 3.21.2 provides.

do_compile() {
    cd ${S}
    ${GO} build ${GOBUILDFLAGS} -buildmode=c-archive -o ${B}/libwhatsmeow.a .

    for f in whatsmeow gometa_init voipkit_none; do
        [ -f ${S}/glue/$f.c ] || continue
        ${CC} ${CFLAGS} -fPIC -I${S}/glue -I${S} -I${B} \
            $(pkg-config --cflags purple glib-2.0) \
            -c ${S}/glue/$f.c -o ${B}/$f.o
    done

    # -Bsymbolic-functions: this plugin and purple-teams both define the identically-named
    # voipkit_* bridge symbols and are dlopen'd into the same process, so without it ELF
    # interposition can route one plugin's calls into the other's copy depending on load order.
    ${CC} -shared -fPIC ${LDFLAGS} -Wl,-Bsymbolic-functions \
        -Wl,-soname,libwhatsmeow.so -o ${B}/libwhatsmeow.so \
        ${B}/*.o ${B}/libwhatsmeow.a \
        $(pkg-config --libs purple glib-2.0) \
        -lopus -logg -lpthread -ldl -lm -lresolv -lstdc++
}

do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${B}/libwhatsmeow.so ${D}${libdir}/purple-2/libwhatsmeow.so
}
