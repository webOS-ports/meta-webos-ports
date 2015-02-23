DEPENDS += "sensorfw"

EXTRA_QMAKEVARS_PRE = "CONFIG+=sensorfw"

do_install_append() {
    mv ${D}/etc/xdg ${D}${OE_QMAKE_PATH_SETTINGS}
}
