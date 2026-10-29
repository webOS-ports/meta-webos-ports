SUMMARY = "GN at the revision Chromium 151 pins, for webruntime on non-x86_64 build hosts"
HOMEPAGE = "https://gn.googlesource.com/gn/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

# The chromium151 repo carries GN only as the x86_64 CIPD binary
# src/buildtools/linux64/gn ("gn/gn/linux-amd64"), which an aarch64 build host
# cannot execute, so webruntime-repo_151.inc swaps in this one there.
#
# oe-core's gn recipe (9d19a787, 2026-02-17) is 107 commits older than the
# 'gn_version' Chromium 151 pins in src/DEPS (1d86777e, 2026-06-28), and those
# commits add language features (expand_directory, ...) that 151's build files
# may use. Build exactly the pin instead. Keep SRCREV equal to gn_version in
# src/DEPS at the webruntime SRCREV.
#
# Checked on the aarch64 build VM: this revision builds with the host's
# g++ 13.3 (-std=c++23) and reports "2435 (1d86777e7f25)".

inherit native

DEPENDS = "ninja-native"

SRC_URI = "git://gn.googlesource.com/gn;protocol=https;branch=main"
SRCREV = "1d86777e7f2562a86ecea77d1809ac4f82bb5bfe"
PV = "0+git"

# gen.py derives the version (last_commit_position.h) from git history.
BB_GIT_SHALLOW = ""

B = "${WORKDIR}/build"

do_configure[cleandirs] += "${B}"
do_configure() {
    python3 ${S}/build/gen.py \
        --platform=linux \
        --out-path=${B} \
        --no-static-libstdc++ \
        --no-strip \
        --allow-warnings
}

do_compile[progress] = "outof:^\[(\d+)/(\d+)\]\s+"
do_compile() {
    ninja -C ${B} --verbose gn
}

# Installed under its own name so it can never be mistaken for, or collide
# with, oe-core's gn-native.
do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/gn ${D}${bindir}/gn-webruntime
}
