FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
  file://halium-boot.sh \
  file://udev-start.sh \
  file://udev-stop.sh \
  file://functions \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/halium-boot.sh ${D}/halium-boot.sh
    install -m 0644 ${WORKDIR}/udev-start.sh ${D}/udev-start.sh
    install -m 0644 ${WORKDIR}/udev-stop.sh ${D}/udev-stop.sh
    install -m 0644 ${WORKDIR}/functions ${D}/functions
}

FILES_${PN} += "/halium-boot.sh /functions  /udev-start.sh  /udev-stop.sh"

