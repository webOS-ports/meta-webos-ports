SUMMARY = "webOS Ports First Use application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

WEBOS_VERSION = "0.3.0-2_32c8039276503365bff3fa20e5263fd3d82916e3"

DEPENDS += "qtbase qtdeclarative"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_public_repo
inherit webos_application

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

INSANE_SKIP_${PN} = "libdir"

FILES_${PN} += "${datadir}/luneos-license-agreements"
FILES_${PN}-dbg += "${webos_applicationsdir}/${PN}/firstuse/.debug"
