SUMMARY = "PhoneGap is a web platform that exposes native mobile device apis and data to JavaScript."
HOMEPAGE = "http://phonegap.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aa80bcec645b0f6ec37d5954ee6dc056"

PR = "r0"

SRC_URI = "git://github.com/phonegap/phonegap.git;protocol=git;tag=${PV}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_frameworksdir}/cordova/
    install -m 0644 ${S}/lib/webos/lib/cordova.webos.js ${D}${webos_frameworksdir}/cordova/cordova.js
}

PACKAGES = "${PN}"
FILES_${PN} = "${webos_frameworksdir}/cordova"
