PRINC := "${@int(PRINC) + 1}"

# Config files from ${sysconfdir} should go into ${PN}-conf package and not
# into ${PN}-server so we fix this here until oe-core version we're using
# contains the relevant patch for this.
PACKAGES =+ " ${PN}-conf"
FILES_${PN}-server = "${bindir}/pulseaudio ${bindir}/start-* ${bindir}/pactl ${base_libdir}/udev/rules.d/*.rules"
FILES_${PN}-conf = "${sysconfdir}/pulse"
CONFFILES_pulseaudio-server = ""
CONFFILES_pulseaudio-conf = " \
  ${sysconfdir}/pulse/default.pa \
  ${sysconfdir}/pulse/daemon.conf \
  ${sysconfdir}/pulse/client.conf"
