SUMMARY = "Google Chat protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01f1269ee11920646864ee3a2f4c5ed3"

require purple-connectors.inc

# Built from luneos-synergy-connectors, the version ended in its commit alone (1.0+git0+d22c6be58f).
# With the upstream commit now in front it can sort lower, so bump the epoch to keep package feeds
# moving forward.
PE = "1"

SRC_URI += " \
    git://github.com/EionRobb/purple-googlechat.git;branch=master;protocol=https;name=upstream;destsuffix=purple-googlechat \
    file://0001-Makefile-Drop-the-hardcoded-I-usr-include-protobuf-c.patch \
"
SRCREV_upstream = "7bb81bc4c40a2da6069d897f2c24d272a08fcd43"

# protobuf-c comes from meta-openembedded. The -native side supplies protoc-c, which the Makefile
# runs to regenerate the .pb-c.c sources.
DEPENDS = "pidgin json-glib glib-2.0 zlib protobuf-c protobuf-c-native"

S = "${UNPACKDIR}/purple-googlechat"

# Upstream's Makefile has CRLF line endings, and so does the patch against it. Keep the .patch
# byte-exact; an editor that normalises line endings will make it stop applying.
#
# PROTOC_C is passed explicitly: upstream falls back to protoc when protoc-gen-c and protoc are
# both on PATH, and the explicit path keeps the build predictable.
EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    PROTOC_C='${STAGING_BINDIR_NATIVE}/protoc-c' \
"

do_compile() {
    oe_runmake
}

# Installed by hand rather than with `make install`, so no Pidgin pixmaps are shipped -- see
# purple-connectors.inc.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${S}/libgooglechat.so ${D}${libdir}/purple-2/libgooglechat.so

    # Account template, so Google Chat can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/googlechat/account/com.palm.googlechat ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/googlechat/apps/com.palm.app.googlechat ${D}${webos_applicationsdir}/
}
