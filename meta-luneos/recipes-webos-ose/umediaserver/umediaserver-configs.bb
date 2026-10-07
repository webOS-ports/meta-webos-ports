# Copyright (c) 2014-2025 LG Electronics, Inc.

SUMMARY = "umediaserver configs installation"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

WEBOS_VERSION = "1.0.0-17_33b442fca4ed27a779dd4cd55e8fc7a48c8c31ac"
PR = "r8"

inherit webos_cmake
inherit webos_machine_dep
inherit webos_enhanced_submissions
#inherit webos_distro_variant_dep
inherit webos_filesystem_paths
inherit webos_public_repo

EXTRA_OECMAKE += "-DWEBOS_INSTALL_CONFCAPSDIR:STRING=${webos_frameworksdir}"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

# Halium: the default resource table has no video-encoder units, so any
# pipeline that requests VENC (mediarecorder's record pipeline, future
# call pipelines) fails its resource acquisition. The Venus VPU on these
# SoCs handles concurrent encode sessions; declare two.
do_install:append:halium() {
    # insert the VENC block right after "resources = ("
    awk 'BEGIN{done=0}
         /^resources = \($/ && !done {
             print;
             print "	{";
             print "		id = \"VENC\";";
             print "		name = \"Digital Video Encoder\";";
             print "		qty = 2;";
             print "	},";
             print "";
             done=1; next
         }
         {print}' \
        ${D}${sysconfdir}/umediaserver/umediaserver_resource_config.txt \
        > ${D}${sysconfdir}/umediaserver/rc.tmp
    mv ${D}${sysconfdir}/umediaserver/rc.tmp \
        ${D}${sysconfdir}/umediaserver/umediaserver_resource_config.txt
}

# Two faults in the upstream table that stop anything playing through the webOS
# media server (HTML5 audio and video in web apps, e.g. the Testr app). Found on
# the SM-T220 and then confirmed on hammerhead and q25, so this is not
# device-specific:
#
# 1. The "media" pipeline entry launches /usr/sbin/reference-media-pipeline.
#    This tree ships the GStreamer pipeline, which installs
#    /usr/sbin/g-media-pipeline, and no package provides the other name, so
#    every pipeline process failed with ENOENT at exec (seen with strace on
#    umediaserver) and the web page got MEDIA_ERROR_FORMAT.
#
# 2. Even a plain WAV makes g-media-pipeline ask the resource manager for
#    1 ADEC and 8 VDEC, against a table of 2 each ("no suitable candidate
#    found"). A page that opens two players at once needs twice that. The
#    counts are abstract units, so declare enough that ordinary playback is
#    not refused.
do_install:append() {
    sed -i \
        -e 's|/usr/sbin/reference-media-pipeline|/usr/sbin/g-media-pipeline|' \
        -e '/id = "VDEC";/,/qty/ s/qty = [0-9]*;/qty = 16;/' \
        -e '/id = "ADEC";/,/qty/ s/qty = [0-9]*;/qty = 8;/' \
        ${D}${sysconfdir}/umediaserver/umediaserver_resource_config.txt

    # Fail here, not on a device, if upstream changes the table so that an
    # edit above no longer matches.
    grep -q 'bin  = "/usr/sbin/g-media-pipeline";' \
        ${D}${sysconfdir}/umediaserver/umediaserver_resource_config.txt \
        || bbfatal "umediaserver_resource_config.txt: pipeline binary was not rewritten"
    grep -A3 'id = "VDEC";' ${D}${sysconfdir}/umediaserver/umediaserver_resource_config.txt | grep -q 'qty = 16;' \
        || bbfatal "umediaserver_resource_config.txt: VDEC count was not raised"
    grep -A3 'id = "ADEC";' ${D}${sysconfdir}/umediaserver/umediaserver_resource_config.txt | grep -q 'qty = 8;' \
        || bbfatal "umediaserver_resource_config.txt: ADEC count was not raised"
}

FILES:${PN} += "${webos_frameworksdir}/umediaserver/*"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
