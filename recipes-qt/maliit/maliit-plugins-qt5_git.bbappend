# We only want the nemo keyboard so disable the maliit one
EXTRA_QMAKEVARS_PRE += "CONFIG+=disable-maliit-keyboard"

RDEPENDS_${PN} = "maliit-framework-qt5"
