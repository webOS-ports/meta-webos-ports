SUMMARY = "Launcher and install tooling for legacy webOS PDK applications"
DESCRIPTION = "pdk-run, which executes a PDK application against the soft-float \
sysroot with the Wayland, audio and GL environment it expects."
HOMEPAGE = "https://github.com/webOS-ports/pdk-luneos"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Plain git fetch rather than meta-luneos's webos_ports_repo class, so this layer
# parses standalone without meta-webos-ports present.
PDK_GIT_REPO ?= "git://github.com/webOS-ports/pdk-luneos.git"
SRC_URI = "${PDK_GIT_REPO};protocol=https;branch=main"
SRCREV = "e29478d25ed5a300251f659e25c9520371163e6b"
PV = "1.0.0+git"

# Shell scripts only. Without these, the default tasks run against the repository
# root - which has a Makefile, so base do_compile calls oe_runmake and tries to
# cross-build the ARM shims with a clang invocation meant for a standalone
# checkout:  make: clang: No such file or directory
inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

PDK_PREFIX = "/opt/pdk"

do_install() {
    install -d ${D}${PDK_PREFIX}
    install -m 0755 ${S}/tools/pdk-run ${D}${PDK_PREFIX}/pdk-run

    # install-games.sh is not shipped. It runs on a development host, unpacks
    # IPKs and pushes them to a target over ssh - it is not a device-side tool,
    # and being bash (it uses mapfile) it would drag bash into the image for
    # nothing:
    #   QA Issue: /opt/pdk/install-games.sh ... requires /bin/bash, but no
    #             providers found in RDEPENDS:pdk-tools

    # On ARMv7 targets the emulator is unnecessary - a hard-float CPU executes
    # soft-float code natively; only the calling convention differs, not the
    # instruction set. pdk-run notices the absence of /opt/pdk/qemu-arm and execs
    # the binary directly.
    install -d ${D}${sysconfdir}/profile.d
    cat > ${D}${sysconfdir}/profile.d/pdk.sh <<EOF
export PATH="\$PATH:${PDK_PREFIX}"
EOF
}

FILES:${PN} = "${PDK_PREFIX} ${sysconfdir}/profile.d/pdk.sh"

RDEPENDS:${PN} = "pdk-sysroot"

# The qemu-user-arm dependency lives in packagegroup-luneos-pdk, not here.
# allarch.bbclass sets TARGET_ARCH = "allarch", so TRANSLATED_TARGET_ARCH is
# "allarch" too and architecture overrides (:x86-64, :aarch64) silently never
# match in this recipe - the dependency would be dropped on every machine.

# luna-send ships inside luna-service2; there is no luna-send package.
RDEPENDS:${PN} += "luna-service2"
