SUMMARY = "Cell broadcast emergency alert channel database"
DESCRIPTION = "Provider metadata describing the public warning cell broadcast \
channels used by emergency alert systems in different countries. Data only, so \
it can be updated without moving the telephony runtime."
HOMEPAGE = "https://github.com/sailfishos/cell-broadcast-provider-info"

# The catalog is derived from AOSP CellBroadcastReceiver resources, which are
# Apache-2.0 too, so the whole package stays under a single license.
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e98a055826fc65091d1e31bc3c808f9a"

PV = "20260511+git"
SRCREV = "c0fc49adf329d70b8f40d2a313d2dcd4674730e5"

SRC_URI = "git://github.com/sailfishos/cell-broadcast-provider-info.git;protocol=https;branch=master"

UPSTREAM_CHECK_COMMITS = "1"

inherit allarch

# data/channels.json is a generated file that upstream commits, so nothing here
# reaches out to AOSP. tools/generate-cellbroadcast-catalog.py regenerates it
# from a local CellBroadcastReceiver checkout and is an authoring tool, not part
# of the build; refreshing the catalog means bumping SRCREV, not running it.

# The attention tone is the one thing upstream generates at package time, and it
# needs an ogg vorbis encoder on the build host - ffmpeg or gst-launch-1.0, per
# tools/generate-cellbroadcast-attention-tones.py. Neither has a -native variant
# in the layers we pull in, and nothing on LuneOS plays the tone yet: there is no
# ngfd, and voicecall is built without enable-ngf. So leave it out by default and
# keep the wiring here for whoever adds a native encoder later.
PACKAGECONFIG ??= ""
PACKAGECONFIG[attention-tones] = ",,ffmpeg-native python3-native"

do_install() {
    install -d ${D}${datadir}/cell-broadcast-provider-info
    install -m 0644 ${S}/data/channels.json \
        ${D}${datadir}/cell-broadcast-provider-info/channels.json

    install -d ${D}${datadir}/pkgconfig
    sed -e 's|^prefix=.*|prefix=${prefix}|' \
        -e 's|^datadir=.*|datadir=${datadir}|' \
        ${S}/cell-broadcast-provider-info.pc \
        > ${D}${datadir}/pkgconfig/cell-broadcast-provider-info.pc
    chmod 0644 ${D}${datadir}/pkgconfig/cell-broadcast-provider-info.pc

    if ${@bb.utils.contains('PACKAGECONFIG', 'attention-tones', 'true', 'false', d)}; then
        python3 ${S}/tools/generate-cellbroadcast-attention-tones.py \
            --output-dir ${D}${datadir}/cell-broadcast-provider-info/attention-tones
    fi
}
