# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Enyo 1.0 JavaScript application framework"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0-128.2+git"
SRCREV = "2f02364b761f98ba58732fcc87eba3709ae2568e"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit allarch

DEPENDS += " nodejs-native "

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    node ${S}/support/enyo-compress/enyo-compress.js ${S}/framework/source --no-color --make-enyo -o ${S}/framework/build

    install -d ${D}${webos_frameworksdir}/enyo/0.10/framework
    cp -vrf ${S}/framework/* ${D}${webos_frameworksdir}/enyo/0.10/framework

    # Bundle the shared libraries as well.
    #
    # The call above only compresses framework/source into enyo-build.js. The
    # libraries under framework/lib (accounts, addressing, contactsui, printdialog,
    # ...) still ship as loose files, and apps pull them in via $enyo-lib/... -- about
    # 45 separate script loads per document. accounts is loaded by both the headless
    # and the GUI document of every app that uses it, so bundling it pays twice.
    #
    # This is the bigger half of the win. Measured on mindphone, on top of the
    # app-side bundling in core-apps.bb: Calendar 5.77s -> 5.02s, Email 6.52s ->
    # 5.70s, renders byte-identical. For Email it is the *only* half that helps.
    for LIB in ${D}${webos_frameworksdir}/enyo/0.10/framework/lib/*/; do
        [ -f "$LIB/depends.js" ] || continue
        bbnote "enyo-compress lib: $LIB"
        node ${S}/support/enyo-compress/enyo-compress.js \
             "$LIB" --no-colors --inplace --overwrite-depends --no-delete
    done

    # Create symlink for enyo/1.0 (points to enyo/0.10)
    ln -vs 0.10 ${D}${webos_frameworksdir}/enyo/1.0
    # Create symlink for tellurium (so the inspector doesn't give errors)
    ln -vs enyo/0.10/framework/build/palm/tellurium ${D}${webos_frameworksdir}/tellurium
}

FILES:${PN} += "${webos_frameworksdir}"
