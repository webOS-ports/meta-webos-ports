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

FILES:${PN} += "${webos_frameworksdir}/umediaserver/*"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
