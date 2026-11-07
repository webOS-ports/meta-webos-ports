FEATURE_PACKAGES_luneos-extended = "packagegroup-luneos-extended"
FEATURE_PACKAGES_luneos-development = "packagegroup-luneos-development"

# No passwordless root by default. Every image used to get
# allow-empty-password, empty-root-password and allow-root-login, so root had
# an empty password and sshd accepted it on every interface, in the release
# image as well as the developer one. Without them oe-core locks root
# (zap_empty_root_password) and sshd_config keeps PermitRootLogin
# prohibit-password and PermitEmptyPasswords no (openssh_%.bbappend): SSH, once
# developer mode starts it, takes a key, and root's password from Settings only
# over the local link (luneos_ssh_local_password below).
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

# Root's password over the local link only, as Sailfish OS does it. sshd is
# key-only (PasswordAuthentication no; KbdInteractiveAuthentication stays no, so
# PAM, where there is one, cannot ask either), except for connections to one of
# these addresses, where root may log in with the password set in Settings >
# Developer Mode. Until one is set root's shadow entry is "*", so there is no
# password to take.
#
# The device's end of its USB network: 172.16.42.2 on the Halium and mainline
# devices (meta-smartphone's android-gadget-setup and initramfs
# setup_usb_network) and the PinePhone; 172.16.43.2, .44.2, .45.2 for the
# PinePhone Pro, the PineTab 2 and the other pine64 machines
# (meta-pine64-luneos luneos-usb-gadget.sh). On qemu and VirtualBox images also
# the NAT guest address 10.0.2.15, which the host reaches only through its own
# port forward. A machine with another address appends it, and one that
# overrides LUNEOS_USB_NET in /etc/default/luneos-usb-gadget has to as well.
#
# These are addresses, not interfaces: sshd cannot match an interface. Port 22
# is open only on usb+/rndis+ (iptables.rules), so the address can be reached
# from elsewhere only once SSH over Wi-Fi is on (or with LUNEOS_DEBUG_ACCESS)
# and a Wi-Fi network hands the device one of these very addresses. On qemu the
# NAT interface is eth0, which is likewise reachable only through SSH over
# Wi-Fi's LUNEOS-DEVMODE rule, not through the USB rules.
#
# With LUNEOS_DEBUG_ACCESS PasswordAuthentication is left as it was: the empty
# root password logs in through it. An empty list leaves out the Match block
# (key-only everywhere).
LUNEOS_SSH_PASSWORD_ADDRESSES ??= "172.16.42.2 172.16.43.2 172.16.44.2 172.16.45.2"
LUNEOS_SSH_PASSWORD_ADDRESSES:append:qemuall = " 10.0.2.15"

python luneos_ssh_local_password() {
    import webos.luneos_ssh
    rootfs = d.getVar('IMAGE_ROOTFS')
    addresses = (d.getVar('LUNEOS_SSH_PASSWORD_ADDRESSES') or '').split()
    debug = d.getVar('LUNEOS_DEBUG_ACCESS') == '1'
    for config in ('sshd_config', 'sshd_config_readonly'):
        path = os.path.join(rootfs, d.getVar('sysconfdir').lstrip('/'), 'ssh', config)
        if not os.path.exists(path):
            continue
        with open(path) as f:
            text = f.read()
        try:
            text = webos.luneos_ssh.sshd_config_local_password(text, addresses, debug)
        except webos.luneos_ssh.SshError as e:
            bb.fatal('%s: %s' % (config, e))
        with open(path, 'w') as f:
            f.write(text)
}
ROOTFS_POSTPROCESS_COMMAND += "luneos_ssh_local_password;"

# Root's SSH keys built into the image, for a developer's own builds:
#     LUNEOS_SSH_AUTHORIZED_KEYS = "/home/me/.ssh/id_ed25519.pub /home/me/.ssh/work.pub"
# Public key FILES on the build host, separated by spaces, each holding one or
# more key lines. They go to root's home as the image's /etc/passwd has it
# (/root), .ssh 0700 and authorized_keys 0600, root's; every line is checked as
# org.webosports.service.devmode checks a key added in Settings. Empty by
# default: an image that leaves your desk must not carry anyone's key.
LUNEOS_SSH_AUTHORIZED_KEYS ??= ""

python luneos_ssh_authorized_keys() {
    import webos.luneos_ssh
    paths = (d.getVar('LUNEOS_SSH_AUTHORIZED_KEYS') or '').split()
    for p in paths:
        if not os.path.isabs(p):
            bb.fatal('LUNEOS_SSH_AUTHORIZED_KEYS: %s is not an absolute path' % p)
    try:
        n = webos.luneos_ssh.install_authorized_keys(d.getVar('IMAGE_ROOTFS'), paths)
    except webos.luneos_ssh.SshError as e:
        bb.fatal('LUNEOS_SSH_AUTHORIZED_KEYS: %s' % e)
    bb.note('LUNEOS_SSH_AUTHORIZED_KEYS: %d key(s) for root' % n)
}
ROOTFS_POSTPROCESS_COMMAND += "${@'luneos_ssh_authorized_keys;' if (d.getVar('LUNEOS_SSH_AUTHORIZED_KEYS') or '').split() else ''}"
do_rootfs[file-checksums] += "${@' '.join('%s:True' % p for p in (d.getVar('LUNEOS_SSH_AUTHORIZED_KEYS') or '').split())}"

# QA: no .ssh or authorized_keys in any home of the image that another user may
# read or change, or that is not its user's (postmarketOS once shipped root's
# world-readable). sshd's StrictModes would refuse such a file at login anyway;
# this refuses the image.
python luneos_ssh_modes_qa() {
    import webos.luneos_ssh
    problems = webos.luneos_ssh.ssh_mode_problems(d.getVar('IMAGE_ROOTFS'))
    if problems:
        bb.fatal('SSH files open to others in the image:\n  ' + '\n  '.join(problems))
}
ROOTFS_POSTPROCESS_COMMAND += "luneos_ssh_modes_qa;"

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
