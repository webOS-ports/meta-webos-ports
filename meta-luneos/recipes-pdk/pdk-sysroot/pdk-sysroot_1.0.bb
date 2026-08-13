SUMMARY = "Soft-float ARM sysroot for legacy webOS PDK applications"
DESCRIPTION = "Installs the ARM soft-float userland that PDK applications run \
inside into /opt/pdk/sysroot. By default the tree is built from source by the \
pdk-armel multiconfig; alternatively point PDK_SYSROOT_TARBALL at one produced \
by tools/mk-sysroot.sh. See docs/yocto.md."
HOMEPAGE = "https://github.com/webOS-ports/pdk-luneos"

LICENSE = "Apache-2.0 & MIT & Zlib"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# The content is ARM soft-float regardless of what MACHINE this image is for. It
# is data as far as this build is concerned - nothing here links against it, and
# nothing executes it except through pdk-run.
INHIBIT_DEFAULT_DEPS = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

PDK_PREFIX = "/opt/pdk"

# --- mode 1 (default): build the userland from source in the pdk-armel multiconfig
#
# Where that multiconfig deposits its rootfs. Must match TMPDIR and MACHINE in
# conf/multiconfig/pdk-armel.conf.
PDK_ARMEL_TMPDIR ?= "${TOPDIR}/tmp-pdk-armel"
PDK_ARMEL_MACHINE ?= "pdk-armel"
PDK_ARMEL_ROOTFS ?= "${PDK_ARMEL_TMPDIR}/deploy/images/${PDK_ARMEL_MACHINE}/pdk-sysroot-image-${PDK_ARMEL_MACHINE}.rootfs.tar.xz"

# --- mode 2: consume a tarball built outside BitBake by tools/mk-sysroot.sh
#
# That script assembles the tree from Debian armel packages plus an optional
# proprietary Palm overlay. It is the configuration the 81% compatibility figure
# was measured against, and it is the only way to get the legacy libraries that
# some titles need but that cannot be built or redistributed.
#
#     PDK_SYSROOT_TARBALL = "file:///srv/pdk/pdk-sysroot.tar.xz"
#
PDK_SYSROOT_TARBALL ?= ""

SRC_URI = "${PDK_SYSROOT_TARBALL}"
SRC_URI[sha256sum] ?= "${PDK_SYSROOT_TARBALL_SHA256}"
PDK_SYSROOT_TARBALL_SHA256 ?= ""

# Only take the multiconfig dependency in mode 1 - in mode 2 there is nothing to
# build and requiring the multiconfig would be a hard error for no reason.
do_install[mcdepends] = "${@'mc::pdk-armel:pdk-sysroot-image:do_image_complete' if not d.getVar('PDK_SYSROOT_TARBALL') else ''}"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${PDK_PREFIX}/sysroot

    if [ -n "${PDK_SYSROOT_TARBALL}" ]; then
        # do_unpack has already expanded the tarball into UNPACKDIR.
        if [ ! -d "${UNPACKDIR}/sysroot" ]; then
            bbfatal "pdk-sysroot: PDK_SYSROOT_TARBALL must contain a top-level sysroot/ directory"
        fi
        cp -a ${UNPACKDIR}/sysroot/. ${D}${PDK_PREFIX}/sysroot/
    else
        if [ ! -f "${PDK_ARMEL_ROOTFS}" ]; then
            bbfatal "pdk-sysroot: ${PDK_ARMEL_ROOTFS} not found.\n\
Is BBMULTICONFIG = \"pdk-armel\" set in conf/local.conf? See docs/yocto.md."
        fi
        tar -xJf ${PDK_ARMEL_ROOTFS} -C ${D}${PDK_PREFIX}/sysroot
    fi

    # Nothing in the guest tree should carry setuid bits into the host image - it
    # is only ever read through qemu-user or exec'd via its own loader.
    find ${D}${PDK_PREFIX}/sysroot -type f -perm /6000 -exec chmod a-s {} +

    # Device nodes likewise: the guest uses the host's /dev through the jail.
    find ${D}${PDK_PREFIX}/sysroot -type b -o -type c | xargs -r rm -f
}

FILES:${PN} = "${PDK_PREFIX}/sysroot"

# Everything below is expected: this package is a foreign-architecture filesystem
# tree, not a set of libraries for this machine.
INSANE_SKIP:${PN} += "arch libdir file-rdeps dev-so staticdev already-stripped \
                      ldflags textrel dev-elf libdir installed-vs-shipped"
EXCLUDE_FROM_SHLIBS = "1"
PRIVATE_LIBS:${PN} = "*"
SKIP_FILEDEPS:${PN} = "1"

PACKAGES = "${PN}"
