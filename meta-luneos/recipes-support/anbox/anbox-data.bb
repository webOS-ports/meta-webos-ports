SUMMARY = "Android in a Box - Android image"
DESCRIPTION = "Android image file for Anbox"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

SRC_URI_qemux86-64 = "http://build.anbox.io/android-images/2017/07/13/android_3_amd64.img;name=x64"
SRC_URI_arm        = "http://build.anbox.io/android-images/2017/06/12/android_1_armhf.img;name=arm"

SRC_URI[x64.sha256sum] = "20caeb254d716610bab2c94cd360a92353e48860fdc7cb21c16e0eab74bc42d0"
SRC_URI[arm.sha256sum] = "8507f7ac92b4b48983e6069e65f6f9709a27510ed8ef8b19bc8f2369b7c144fd"

do_install() {
    install -d ${D}${localstatedir}/lib/anbox
}

do_install_append_qemux86-64() {
    install -m 0644 ${WORKDIR}/android_3_amd64.img ${D}${localstatedir}/lib/anbox/android.img
}

do_install_append_arm() {
    install -m 0644 ${WORKDIR}/android_1_armhf.img ${D}${localstatedir}/lib/anbox/android.img
}

FILES_${PN} += " ${webos_execstatedir}/anbox"
