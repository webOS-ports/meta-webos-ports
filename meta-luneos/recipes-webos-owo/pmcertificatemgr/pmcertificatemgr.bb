# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Provides certificate storage and wrapper API on openssl certificate manipulation functions"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "openssl glib-2.0"
RDEPENDS:${PN} = "ca-certificates"

PV = "2.0.0-29+git"
SRCREV = "d238d4dd6b70b09196bfaed86c34f56bc6ea7ad2"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit update-alternatives

ALTERNATIVE:${PN} = "openssl-cnf2"
ALTERNATIVE_LINK_NAME[openssl-cnf2] = "${sysconfdir}/ssl/openssl.cnf"
ALTERNATIVE_PRIORITY[openssl-cnf2] ?= "10"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=herrie/fixes"
S = "${WORKDIR}/git"

do_install:append() {
    # We ship our own modified openssl configuration and as long as the
    # openssl-misc package is not installed within the same image we don't
    # get a conflict.
    install -d ${D}${sysconfdir}/ssl
    install -m 0644 ${S}/files/conf/ssl/openssl.cnf ${D}${sysconfdir}/ssl
}

# pmcertificatemgr/2.0.0-29+git/git/src/cert_pkcs.c:105:57: error: passing argument 1 of 'EVP_PKEY_type' makes integer from pointer without a cast [-Wint-conversion]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_pkcs.c:105:79: error: passing argument 1 of 'EVP_PKEY_type' makes integer from pointer without a cast [-Wint-conversion]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:360:50: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:411:49: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:417:62: error: passing argument 1 of 'OPENSSL_sk_value' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:682:52: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:686:56: error: passing argument 1 of 'OPENSSL_sk_value' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:778:54: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:783:62: error: passing argument 1 of 'OPENSSL_sk_value' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:843:54: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:848:62: error: passing argument 1 of 'OPENSSL_sk_value' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:944:54: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:951:62: error: passing argument 1 of 'OPENSSL_sk_value' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:1024:32: error: passing argument 1 of 'OPENSSL_sk_num' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_db.c:1028:46: error: passing argument 1 of 'OPENSSL_sk_value' from incompatible pointer type [-Wincompatible-pointer-types]
# pmcertificatemgr/2.0.0-29+git/git/src/cert_utils.c:569:25: error: passing argument 1 of 'EVP_PKEY_type' makes integer from pointer without a cast [-Wint-conversion]
CFLAGS += "-Wno-error=int-conversion -Wno-error=incompatible-pointer-types"
