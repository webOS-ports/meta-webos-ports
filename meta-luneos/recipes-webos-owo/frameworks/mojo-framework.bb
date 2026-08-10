# Copyright (c) 2026 Herman van Hazendonk

SUMMARY = "Mojo 1.0 JavaScript application framework"
DESCRIPTION = "Palm's Mojo framework (submission 506, as shipped in webOS 3.0.5), \
adapted to run on WebAppMgr's Chromium instead of LunaSysMgr's WebKit. Provides \
/usr/palm/frameworks/mojo for applications whose index.html carries \
<script src=\"/usr/palm/frameworks/mojo/mojo.js\" x-mojo-version=\"1\">."
SECTION = "webos/frameworks"

# Mojo was never open-sourced. Open webOS released Enyo; Mojo stayed
# proprietary to Palm/HP, so this recipe cannot fetch it the way enyo-1.0.bb
# fetches from git - the framework is extracted from a webOS image the builder
# already holds a licence to use. Nothing Palm-authored is redistributed by
# this layer: the tarball is supplied locally, and our own changes ship as a
# patch plus two new files.
LICENSE = "CLOSED"

PV = "1.0-506"
PR = "r0"

inherit webos_filesystem_paths
inherit allarch

# Produce the tarball with extract-mojo-framework.sh (next to this recipe).
# A bare "file://name" is looked up on FILESPATH, which only covers the
# recipe's own directories - and nothing Palm-authored belongs in the layer -
# so the path has to be absolute. It defaults to DL_DIR, where the extraction
# script suggests putting it; override either half in local.conf.
MOJO_FRAMEWORK_TARBALL ?= "mojo-framework-${PV}.tar.gz"
MOJO_FRAMEWORK_DIR ?= "${DL_DIR}"

FILESEXTRAPATHS:prepend := "${THISDIR}/mojo-framework:"

SRC_URI = " \
    file://${MOJO_FRAMEWORK_DIR}/${MOJO_FRAMEWORK_TARBALL} \
    file://0001-mojo.js-load-the-framework-builtins-from-disk.patch;patchdir=${S}/mojo;striplevel=1 \
    file://mojo-compat.js \
    file://denativize.py \
    file://extract-mojo-framework.sh \
"

S = "${UNPACKDIR}/mojo-framework-${PV}"

# denativize.py is a build-time tool only; nothing needs python at runtime.
DEPENDS = "python3-native"

# Prototype 1.6.0.3 replaces the builtin Prototype rewrite, and mojo.core is
# reached through mojoloader.
RDEPENDS:${PN} += "mojoloader"

do_compile() {
    # The builtin blobs were compiled into LunaSysMgr's WebKit as V8 natives.
    # Their trailers call %SetProperty(global, ...), a privileged intrinsic
    # that makes Chromium reject the whole file at parse time. Rewrite the
    # trailer into a plain global assignment; a classic script publishes the
    # declaration anyway.
    for blob in ${S}/mojo/builtins/palmInitFramework*.js; do
        [ -e "$blob" ] || continue
        bbnote "de-nativizing $(basename $blob)"
        python3 ${UNPACKDIR}/denativize.py "$blob" "$blob.new"
        mv "$blob.new" "$blob"
    done
}

do_install() {
    install -d ${D}${webos_frameworksdir}

    # -a: the submission's templates and images symlink into ../mojocommon
    # with relative paths that only resolve if both land side by side.
    cp -a ${S}/mojo ${D}${webos_frameworksdir}/
    cp -a ${S}/mojocommon ${D}${webos_frameworksdir}/
    cp -a ${S}/mojo.core ${D}${webos_frameworksdir}/
    cp -a ${S}/prototype ${D}${webos_frameworksdir}/
    install -m 0644 ${S}/mojo-core.js ${D}${webos_frameworksdir}/mojo-core.js

    # Compensates for behaviour Mojo relied on in 2011 WebKit: the missing
    # border-style on every -webkit-border-image rule, and the PalmSystem
    # members WebAppMgr does not implement. Loaded by the patched mojo.js.
    install -m 0644 ${UNPACKDIR}/mojo-compat.js ${D}${webos_frameworksdir}/mojo/mojo-compat.js

    # cp -a keeps the uid/gid the tarball was created with, which is whoever
    # unpacked the webOS image. Those ids mean nothing on the target and make
    # do_package fail its host-contamination check.
    chown -R root:root ${D}${webos_frameworksdir}
}

FILES:${PN} += "${webos_frameworksdir}"

# The framework blobs are minified JavaScript with very long lines; nothing
# here is ELF, so the usual QA passes have nothing to inspect.
INSANE_SKIP:${PN} += "arch"

python do_fetch:prepend() {
    import os
    path = os.path.join(d.getVar("MOJO_FRAMEWORK_DIR"), d.getVar("MOJO_FRAMEWORK_TARBALL"))
    if not os.path.exists(path):
        bb.fatal(
            "%s is missing.\n"
            "Mojo was never open-sourced, so it has to be extracted from a webOS\n"
            "image you hold a licence to use:\n\n"
            "    meta-luneos/recipes-webos-owo/frameworks/mojo-framework/"
            "extract-mojo-framework.sh \\\n"
            "        /path/to/unpacked-webos-rootfs %s\n\n"
            "Set MOJO_FRAMEWORK_DIR / MOJO_FRAMEWORK_TARBALL in local.conf to put it "
            "elsewhere." % (path, d.getVar("MOJO_FRAMEWORK_DIR")))
}
