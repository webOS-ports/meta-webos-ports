FEATURE_PACKAGES_luneos-extended = "packagegroup-luneos-extended"
FEATURE_PACKAGES_luneos-development = "packagegroup-luneos-development"

# No passwordless root by default. Every image used to get
# allow-empty-password, empty-root-password and allow-root-login, so root had
# an empty password and sshd accepted it on every interface, in the release
# image as well as the developer one. Without them oe-core locks root
# (zap_empty_root_password) and sshd_config keeps PermitRootLogin
# prohibit-password and PermitEmptyPasswords no (openssh_%.bbappend): SSH, once
# developer mode starts it, takes a key or the password set in Settings.
#
# LUNEOS_DEBUG_ACCESS = "1" in local.conf puts the old behaviour back for a
# local developer build (see luneos-features.inc for all it changes). Never set
# it for an image that leaves your desk.
LUNEOS_DEBUG_ACCESS_FEATURES = "allow-empty-password empty-root-password allow-root-login"

LUNEOS_IMAGE_DEFAULT_FEATURES = "post-install-logging ssh-server-openssh package-management luneos-extended \
    ${@oe.utils.conditional('LUNEOS_DEBUG_ACCESS', '1', d.getVar('LUNEOS_DEBUG_ACCESS_FEATURES'), '', d)} \
"

# LUNEOS_DEBUG_ACCESS also lets SSH in on every interface, not only USB, as it
# did before the firewall: the flag is what luneos-devmode-firewall ssh-wifi on
# leaves behind (iptables_%.bbappend). It still needs developer mode, which
# luneos-dev-image turns on.
luneos_debug_access_ssh_wifi() {
    install -d ${IMAGE_ROOTFS}${localstatedir}/luna
    touch ${IMAGE_ROOTFS}${localstatedir}/luna/ssh-wifi-enabled
}
ROOTFS_POSTPROCESS_COMMAND += "${@oe.utils.conditional('LUNEOS_DEBUG_ACCESS', '1', 'luneos_debug_access_ssh_wifi;', '', d)}"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

inherit webos_image
inherit luneos-image-qa
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

# Inherited after oe-core's rootfs-postcommands (which image.bbclass pulls in via
# "inherit_defer ${IMGCLASSES}", resolved at the end of parsing) so that our
# systemd_handle_machine_id override actually wins. See the class for why.
IMGCLASSES += "luneos_rootfs_postcommands"

# Debuggers, valgrind and the Qt/libdrm example binaries are opt-in for any image:
# EXTRA_IMAGE_FEATURES += "luneos-development-debug"
FEATURE_PACKAGES_luneos-development-debug = "packagegroup-luneos-development-debug"
