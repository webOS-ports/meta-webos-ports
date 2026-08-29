# Trim target qemu down to what the PDK layer actually needs.
#
# pdk-tools RDEPENDS on qemu-user-arm on non-ARM32 machines, which otherwise
# drags in a full qemu built for fifteen architectures with SDL, KVM and virgl.
# The only thing PDK applications need is user-mode ARM emulation.
#
# Opt in from conf/local.conf:
#
#     PDK_QEMU_USERMODE_ONLY = "1"
#
# Deliberately NOT keyed on a DISTRO_FEATURE. DISTRO_FEATURES is part of the task
# signature of a large share of recipes, so adding one to an existing build
# invalidates sstate broadly and triggers a big rebuild for what is, here, a
# single recipe's configuration. A private variable costs nobody anything.
#
# Done in an anonymous python function rather than with inline expansion so that
# when the option is off, QEMU_TARGETS and PACKAGECONFIG are left entirely alone -
# a bbappend that restated oe-core's default QEMU_TARGETS list as its fallback
# would silently go stale the next time upstream changed it.

PDK_QEMU_USERMODE_ONLY ??= "0"

python () {
    if d.getVar('PDK_QEMU_USERMODE_ONLY') != '1':
        return

    d.setVar('QEMU_TARGETS', 'arm')

    # System emulation, its display backends and its accelerators are all dead
    # weight for user-mode ARM.
    drop = {'sdl', 'kvm', 'xen', 'virglrenderer', 'epoxy', 'seccomp', 'fdt'}
    cfg = (d.getVar('PACKAGECONFIG') or '').split()
    d.setVar('PACKAGECONFIG', ' '.join(c for c in cfg if c not in drop))
}
