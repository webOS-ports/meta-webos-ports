FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
  git://github.com/Tofee/initramfs-tools-halium.git;branch=tofe/ab-scheme \
  file://udev-start.sh \
  file://udev-stop.sh \
  file://functions \
"
SRCREV="45a48291070344e6dba5821c3bfcd0ff974dc8b3"

do_install_append() {
    install -m 0644 ${WORKDIR}/git/scripts/halium ${D}/halium-boot.sh
    install -m 0644 ${WORKDIR}/udev-start.sh ${D}/udev-start.sh
    install -m 0644 ${WORKDIR}/udev-stop.sh ${D}/udev-stop.sh
    install -m 0644 ${WORKDIR}/functions ${D}/functions
}

FILES_${PN} += "/halium-boot.sh /functions  /udev-start.sh  /udev-stop.sh"

