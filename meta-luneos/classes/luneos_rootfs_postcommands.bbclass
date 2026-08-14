# Overrides for oe-core's rootfs-postcommands.bbclass.
#
# image.bbclass pulls rootfs-postcommands in through "inherit_defer ${IMGCLASSES}",
# which is resolved at the very end of parsing, so a plain function override in
# luneos_image.bbclass loses to it. luneos_image.bbclass appends this class to
# IMGCLASSES instead, so it is inherited immediately after rootfs-postcommands and
# its definitions win, while ROOTFS_POSTPROCESS_COMMAND keeps its original
# contents and ordering.

# LuneOS deliberately masks a number of systemd units: webos-initscripts ships
# /etc/systemd/system/<unit> as a symlink to /dev/null for getty@tty1.service,
# syslog.socket, sntp.service, run-postinsts.service and others, because LuneOS
# owns the display and provides its own replacements for those services.
#
# systemd 259 reports "Unit <x> is masked" and exits 1 when the preset policy
# wants to enable a unit we have masked, which fails do_rootfs. scarthgap never
# hit this: OE shipped its own Python reimplementation of systemctl there, and it
# did not run preset-all at all.
#
# preset-all still applies every other preset before returning non-zero -- that
# was checked by re-running it with the masks moved aside, which exits 0 and
# creates exactly one extra symlink, enabling the getty@tty1 we masked on
# purpose. So the masked-unit failure is safe to ignore, but nothing else is.
#
# Keep the body in sync with rootfs-postcommands.bbclass; only the two
# preset-all invocations are wrapped.
systemd_handle_machine_id() {
    if ${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs", "true", "false", d)}; then
        # Create machine-id
        # 20:12 < mezcalero> koen: you have three options: a) run systemd-machine-id-setup at install time, b) have / read-only and an empty file there (for stateless) and c) boot with / writable
        touch ${IMAGE_ROOTFS}${sysconfdir}/machine-id
    fi
    # In order to be backward compatible with the previous OE-core specific (re)implementation of systemctl
    # we need to touch machine-id when handling presets and when the rootfs is NOT stateless
    if ${@ 'true' if not bb.utils.contains('IMAGE_FEATURES', 'stateless-rootfs', True, False, d) else 'false'}; then
        touch ${IMAGE_ROOTFS}${sysconfdir}/machine-id
        if [ -e ${IMAGE_ROOTFS}${root_prefix}/lib/systemd/systemd ]; then
            luneos_preset_all
            luneos_preset_all --global
        fi
    fi
}

# Run preset-all, tolerating a non-zero exit caused only by units we masked.
luneos_preset_all() {
    preset_log="${T}/luneos-preset-all.log"
    if systemctl --root="${IMAGE_ROOTFS}" $@ --preset-mode=enable-only preset-all > "$preset_log" 2>&1; then
        cat "$preset_log"
        return 0
    fi
    cat "$preset_log"
    if ! grep -qi "Failed" "$preset_log"; then
        bbfatal "systemctl preset-all $@ failed without reporting a reason"
    fi
    if grep -i "Failed" "$preset_log" | grep -qv "is masked"; then
        bbfatal "systemctl preset-all $@ failed for a reason other than a masked unit"
    fi
    bbnote "systemctl preset-all $@ only failed on units LuneOS masks on purpose"
}
