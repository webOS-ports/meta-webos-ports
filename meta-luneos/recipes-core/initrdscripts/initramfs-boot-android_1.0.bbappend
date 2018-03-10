FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
  git://github.com/Tofee/initramfs-tools-halium.git;branch=halium \
  file://udev-start.sh \
  file://udev-stop.sh \
  file://functions \
"
SRCREV="fd3e8efb3e9832c6f02f1478c9093da227c2ec3b"

do_install_append() {
    install -m 0644 ${WORKDIR}/git/scripts/halium ${D}/halium-boot.sh
    install -m 0644 ${WORKDIR}/udev-start.sh ${D}/udev-start.sh
    install -m 0644 ${WORKDIR}/udev-stop.sh ${D}/udev-stop.sh
    install -m 0644 ${WORKDIR}/functions ${D}/functions
}

FILES_${PN} += "/halium-boot.sh /functions  /udev-start.sh  /udev-stop.sh"

