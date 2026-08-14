# webos-initscripts ships /etc/systemd/system/run-postinsts.service as a symlink
# to /dev/null, so the service is masked and run-postinsts can never run on
# target: LuneOS requires every postinstall to succeed at do_rootfs time rather
# than deferring any of them to first boot.
#
# oe-core still installs this package during do_rootfs (image.bbclass puts it in
# ROOTFS_BOOTSTRAP_INSTALL) and only removes it again at the end when nothing was
# deferred, so its postinst runs and asks systemd to enable the masked unit.
# systemd 259 (wrynose) treats enabling a masked unit as a hard error where 255
# (scarthgap) tolerated it, so the postinst returns 1 and fails do_rootfs with
# "Postinstall scriptlets of ['run-postinsts'] have failed".
#
# Clearing SYSTEMD_SERVICE drops that enable from the postinst. oe-core's
# bootstrap/removal dance is untouched, and the generic "a postinst failed" check
# keeps protecting every other package.
#
# systemd.bbclass is also what would normally add the unit to FILES, so package
# it explicitly: leaving it uninstalled would trip the unshipped-files QA check,
# and oe/rootfs.py looks for the unit to decide whether to uninstall the
# bootstrap package again once do_rootfs is done.
SYSTEMD_SERVICE:${PN} = ""
FILES:${PN} += "${systemd_system_unitdir}/run-postinsts.service"
