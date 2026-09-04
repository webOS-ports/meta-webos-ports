# Copyright (c) 2026 Herman van Hazendonk

SUMMARY = "Mojo JavaScript application frameworks (Mojo 1 and Mojo 2)"
DESCRIPTION = "Palm's Mojo frameworks as shipped in webOS 3.0.5 - Mojo 1 \
(submission 506) and Mojo 2 (submission 205) - adapted to run on WebAppMgr's \
Chromium instead of LunaSysMgr's WebKit. Provides /usr/palm/frameworks/mojo \
for applications whose index.html carries \
<script src=\"/usr/palm/frameworks/mojo/mojo.js\" x-mojo-version=\"1\">, and \
/usr/palm/frameworks/mojo2 for those loading mojo2/mojo.js."
SECTION = "webos/frameworks"

# Mojo was never open-sourced - Open webOS released Enyo; Mojo stayed
# proprietary to Palm/HP. The webOS-ports/mojo-framework repository carries
# the framework as extracted from a webOS 3.0.5 image (see its pristine-506
# tag and extract-mojo-framework.sh next to this recipe), with the LuneOS
# adaptations - loading the builtins from disk, de-nativized builtin blobs,
# mojo-compat.js - applied on top as reviewable commits.
LICENSE = "CLOSED"

# Versioned by the webOS release the payload comes from, now that the
# package spans two frameworks (Mojo 1 submission 506, Mojo 2 submission 205).
PV = "3.0.5+git"
SRCREV = "0cc0b56cea5e1455f1aa0488d19845a81f782d12"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# Prototype 1.6.0.3 replaces the builtin Prototype rewrite, and mojo.core is
# reached through mojoloader. Mojo 2's builtin blob additionally requires the
# underscore and foundations loadable frameworks at initialisation.
RDEPENDS:${PN} += "mojoloader underscore foundation-frameworks"

do_install() {
    install -d ${D}${webos_frameworksdir}

    # -a: the submission's templates and images symlink into ../mojocommon
    # with relative paths that only resolve if both land side by side.
    cp -a ${S}/mojo ${D}${webos_frameworksdir}/
    cp -a ${S}/mojo2 ${D}${webos_frameworksdir}/
    cp -a ${S}/mojocommon ${D}${webos_frameworksdir}/
    cp -a ${S}/mojo.core ${D}${webos_frameworksdir}/
    cp -a ${S}/prototype ${D}${webos_frameworksdir}/
    install -m 0644 ${S}/mojo-core.js ${D}${webos_frameworksdir}/mojo-core.js
}

FILES:${PN} += "${webos_frameworksdir}"

# The framework blobs are minified JavaScript with very long lines; nothing
# here is ELF, so the usual QA passes have nothing to inspect.
INSANE_SKIP:${PN} += "arch"
