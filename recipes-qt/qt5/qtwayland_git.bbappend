FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/webOS-ports/qtwayland;branch=webOS-ports/master;protocol=git"
SRCREV = "0ddf2fe2699a388af3ba1ea1b8176b5197fc821e"

FILES_${PN} += "${libdir}/qt5/plugins/wayland-graphics-integration"
FILES_${PN}-dbg += "${libdir}/qt5/plugins/wayland-graphics-integration/*/.debug"
