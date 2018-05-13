SUMMARY = "Android in a Box - Android image"
DESCRIPTION = "Android image file for Anbox"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

SRC_URI_qemux86-64 = "http://build.anbox.io/android-images/2017/07/13/android_3_amd64.img;name=x64"
SRC_URI_armv7a     = "http://build.anbox.io/android-images/2017/06/12/android_1_armhf.img;name=armv7a"
SRC_URI_aarch64    = "http://build.anbox.io/android-images/2017/08/04/android_1_arm64.img;name=aarch64"

SRC_URI[x64.sha256sum]     = "20caeb254d716610bab2c94cd360a92353e48860fdc7cb21c16e0eab74bc42d0"
SRC_URI[armv7a.sha256sum]  = "8507f7ac92b4b48983e6069e65f6f9709a27510ed8ef8b19bc8f2369b7c144fd"
SRC_URI[aarch64.sha256sum] = "e52da14ab5ee6f5274a102193d7e92382a1a7b5b87154f8cf280037bfa7ddb0b"

do_install() {
    install -d ${D}${localstatedir}/lib/anbox
}

do_install_append_qemux86-64() {
    install -m 0644 ${WORKDIR}/android_3_amd64.img ${D}${localstatedir}/lib/anbox/android.img
}

do_install_append_armv7a() {
    install -m 0644 ${WORKDIR}/android_1_armhf.img ${D}${localstatedir}/lib/anbox/android.img
}

do_install_append_aarch64() {
    install -m 0644 ${WORKDIR}/android_1_arm64.img ${D}${localstatedir}/lib/anbox/android.img
}

FILES_${PN} += " ${webos_execstatedir}/anbox"
