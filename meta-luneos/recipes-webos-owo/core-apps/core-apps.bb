# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Core applications that are part of Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.0.0-2+git"
SRCREV = "f16783f55678122a48e2ead77cb9571348cb96ac"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_app

# Bundle each app's JavaScript at build time. These Enyo 1 apps load their sources
# as hundreds of individual document.write-injected <script> tags; enyo-compress
# collapses each window into one build.js + build.css. Measured on mindphone:
# Calendar 6.31s -> 5.77s cold start, 193 -> 127 requests, render byte-identical.
DEPENDS += "nodejs-native enyo-compress-native"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    # WEBOS_INSTALL_WEBOS_COREAPPSDIR
    install -d ${D}${webos_applicationsdir}
    #INSTALL DB/KINDS
    install -d ${D}${webos_sysconfdir}/db/kinds
    #INSTALL DB/PERSMISSIONS
    install -d ${D}${webos_sysconfdir}/db/permissions
    #INSTALL ACTIVITIES
    install -d ${D}${webos_sysconfdir}/activities

    for COREAPPS in `ls -d1 ${S}/com.palm.app*` ; do
        COREAPPS_DIR=`basename $COREAPPS`
        install -d ${D}${webos_applicationsdir}/$COREAPPS_DIR/
        cp -vrf $COREAPPS/* ${D}${webos_applicationsdir}/$COREAPPS_DIR/

        if [ -d $COREAPPS/configuration/db/kinds ]; then
            install -v -m 644 $COREAPPS/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds
        fi

        if [ -d $COREAPPS/configuration/db/permissions ]; then
            install -v -m 644 $COREAPPS/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
        fi

        if [ -d $COREAPPS/configuration/activities ]; then
            cp -vrf $COREAPPS/configuration/activities/* ${D}${webos_sysconfdir}/activities/
        fi
    done
    # Drop the Contacts, Notes & Calculator applications, we ship our own Enyo 2 variant
    # We keep all the db kinds & permissions because other apps can use these too.
    #
    # This has to happen BEFORE the enyo-compress pass below: com.palm.app.contacts
    # has a depends.js referencing app/Ringtones.js, which does not exist in the
    # repo, and the builder (correctly) fails on it.
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.notes
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.calculator
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.contacts

    # Compress every "window root": a directory holding both a depends.js and an
    # HTML file that loads enyo.js. That is 3 for Calendar (root, app/,
    # app/reminders/), 5 for Email, 1 for Clock. Each is built independently and its
    # depends.js rewritten to pull a single build.js + build.css.
    #
    # --inplace, not the README's `-o build` two-pass flow: that flow deletes
    # app/shared/*.js while building the first window, and the second window then
    # fails with ENOENT because it depends on those same files. --inplace with
    # --no-delete is order-independent and safe for sources shared between windows.
    for APPDIR in ${D}${webos_applicationsdir}/*; do
        [ -d "$APPDIR" ] || continue
        for DEPDIR in `find $APPDIR -name depends.js -not -path '*/spec/*' -not -path '*/tests/*' -printf '%h\n' | sort -u`; do
            ls $DEPDIR/*.html >/dev/null 2>&1 || continue
            grep -lq 'enyo\.js' $DEPDIR/*.html 2>/dev/null || continue
            bbnote "enyo-compress: $DEPDIR"
            node ${STAGING_DIR_NATIVE}/opt/enyo-compress/enyo-compress.js \
                 "$DEPDIR" --no-colors --inplace --overwrite-depends --no-delete
        done
    done
}

FILES:${PN} += "${webos_applicationsdir} ${webos_sysconfdir}"

RDEPENDS:${PN} = "bash"
