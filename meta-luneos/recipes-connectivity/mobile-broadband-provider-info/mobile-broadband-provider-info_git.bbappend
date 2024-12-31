#Mer uses their own version of MBPI which has some elements added. So we want to use their version only in case we're using their oFono version. In other cases where we use upstream oFono we want to use the regular MBPI from Yocto.

SRC_URI:halium  = " \
                    git://github.com/sailfishos/mobile-broadband-provider-info.git;protocol=https;branch=master \
                    file://meson.build \
"

S:halium = "${WORKDIR}/git/${PN}"

SRCREV:halium = "fe500f1b19e8525d09655a38ac111a0fe127b5f9"
LIC_FILES_CHKSUM:halium = "file://COPYING;md5=87964579b2a8ece4bc6744d2dc9a8b04"
