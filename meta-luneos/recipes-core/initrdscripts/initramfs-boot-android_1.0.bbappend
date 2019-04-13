FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

S = "${WORKDIR}/git"

SRC_URI += " \
    git://github.com/Tofee/initramfs-tools-halium.git;branch=tofe/ab-scheme \
    file://functions \
"

SRC_URI_append_tenderloin = " \
    file://0001-tenderloind-Fix-userdata-mount-options.patch \
"

SRCREV="37e2c80265e646169c8aee07acb1a9a3785ed699"

RDEPENDS_${PN} += "busybox-mdev"

do_install_append() {
    install -m 0644 ${WORKDIR}/git/scripts/halium ${D}/halium-boot.sh
    install -m 0644 ${WORKDIR}/functions ${D}/functions
}

FILES_${PN} += "/halium-boot.sh /functions"

