# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Universal remote for devices with an infrared transmitter"
SECTION = "webos/apps"
# The app is GPL-3.0-only; the code database it ships is Flipper-IRDB, CC0-1.0
LICENSE = "GPL-3.0-only & CC0-1.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891 \
                    file://${COMMON_LICENSE_DIR}/CC0-1.0;md5=0ceb3372c9595f0a8067e55da801e4a1"
PV = "1.0.0+git"

# Pinned, never AUTOREV: see org.webosports.service.torch.bb.
SRCREV = "d54b61085ea3c47d7a841494ef009e261b5dfa75"
# Flipper-IRDB, converted into the app's JSON at build time by the app's
# tools/build-irdb.py; bump this to pick up newer codes.
SRCREV_irdb = "d126fb1b6f1e114c52b4a8c19839ea65e3a9c24d"
SRCREV_FORMAT = "default_irdb"

RDEPENDS:${PN} = "luna-next-cardshell org.webosports.service.ir"

inherit webos_ports_repo
inherit cmake webos_cmake webos_application webos_filesystem_paths
inherit python3native

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
           git://github.com/Lucaslhm/Flipper-IRDB.git;protocol=https;branch=main;name=irdb;destsuffix=flipper-irdb \
"

EXTRA_OECMAKE += "-DIRDB_SOURCE_DIR=${UNPACKDIR}/flipper-irdb"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.remote \
    ${datadir}/luna-service2/roles.d/org.webosports.app.remote.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.remote.manifest.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.remote.perm.json \
"
