FEATURE_PACKAGES_luneos-extended = "packagegroup-luneos-extended"
FEATURE_PACKAGES_luneos-development = "packagegroup-luneos-development"

LUNEOS_IMAGE_DEFAULT_FEATURES = "allow-empty-password empty-root-password allow-root-login post-install-logging ssh-server-openssh package-management luneos-extended"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

inherit webos_image
inherit ${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'webos_smack_labeling', '', d)}

PR:append = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'smack1', '', d)}"

IMAGE_INSTALL:append = " \
  ${MACHINE_EXTRA_RDEPENDS} \
"

SRC_URI = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'file://smack_labels_default', '', d)}"

SMACK_LABELS_FILE = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', '${WORKDIR}/smack_labels_default', '', d)}"

do_rootfs[prefuncs] += "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'do_fetch do_unpack', '', d)}"

ROOTFS_POSTPROCESS_COMMAND += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'smack', ' do_smack_labeling ; ', '', d)} \
"
