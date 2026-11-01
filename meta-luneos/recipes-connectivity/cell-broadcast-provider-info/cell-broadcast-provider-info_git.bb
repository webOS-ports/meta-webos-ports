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
# in the layers we pull in, so the file is kept here. It is that script's output
# at this SRCREV, every sample computed, and voicecall-manager only offers the
# tone when the file exists. With ffmpeg 8.0.1 and libvorbis on the host this
# gives the same bytes again:
#   python3 tools/generate-cellbroadcast-attention-tones.py --encoder ffmpeg \
#       --output-dir meta-luneos/recipes-connectivity/cell-broadcast-provider-info/cell-broadcast-provider-info
# luna-next-cardshell plays it, on a public warning only.
SRC_URI += "file://cellbroadcast-attention-853-960.ogg"

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

    install -d ${D}${datadir}/cell-broadcast-provider-info/attention-tones
    install -m 0644 ${UNPACKDIR}/cellbroadcast-attention-853-960.ogg \
        ${D}${datadir}/cell-broadcast-provider-info/attention-tones/
}
