SUMMARY = "Standalone torch (flashlight) app for webOS Ports"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"
PV = "1.0.0+git"

# Pinned, never AUTOREV - see org.webosports.service.torch for why: the class
# resolves AUTOREV with a git ls-remote at PARSE time, so an unreachable repo
# halts parsing for the whole layer rather than failing this one recipe.
SRCREV = "2ddb37fd8195e36288a558fce26e509145b28af4"

RDEPENDS:${PN} = "luna-next-cardshell"

inherit webos_ports_repo
inherit cmake webos_cmake webos_application webos_filesystem_paths

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.torch \
    ${datadir}/luna-service2/roles.d/org.webosports.app.torch.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.torch.manifest.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.torch.perm.json \
"
