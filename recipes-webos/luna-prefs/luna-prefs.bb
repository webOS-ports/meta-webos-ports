# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS preferences manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 cjson sqlite3 glib-2.0"

RDEPENDS_${PN} = "luna-prefs-data"

# corresponds to tag submissions/1.01
SRCREV = "1b8182fe4373d29fa728345825c8988db138b23b"
# NOTE please keep version in sync with luna-prefs-data!
PV = "2.0.0-1.01"

#inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_program
inherit webos_library
inherit webos_system_bus

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # CFISH-930: remove "other" perms granted by pmmakefiles (aka palmmake):
    chmod o-rwx ${D}${bindir}/luna-prefs-service
    chmod o-rwx ${D}${bindir}/lunaprop

    install -d ${D}${sysconfdir}/prefs/properties

    # Let's not require a submission process to add to the whitelist
    cat > ${D}${sysconfdir}/prefs/public_properties <<EOF
com.palm.properties.nduid
EOF
    chmod 644 ${D}${sysconfdir}/prefs/public_properties
}

