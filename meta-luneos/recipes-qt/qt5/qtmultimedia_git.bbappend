LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv21;md5=58a180e1cf84c756c29f782b3a485c29 \
    file://LICENSE.LGPLv3;md5=c4fe8c6de4eef597feec6e90ed62e962 \
    file://LGPL_EXCEPTION.txt;md5=9625233da42f9e0ce9d63651a9d97654 \
"

PACKAGECONFIG += "gstreamer"

SRCREV = "9fbb4444fddb1b5c7bc58775a19266fe84627f6a"

SRC_URI = "git://github.com/foolab/qtmultimedia.git;protocol=git;branch=mer-stable"
S = "${WORKDIR}/git"
