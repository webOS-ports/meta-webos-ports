# Move systemd configuration into own package as we don't want it within our images.
# Having the service files to save/restore the ALSA configuration state casues the android
# based devices to break in different cases. We will pull in the -systemd packages for
# machines like qemu* in our packagegroups.
PACKAGES_prepend = "${PN}-systemd "
FILES_alsa-utils-systemd = "${systemd_unitdir}"
