PRINC := "${@int(PRINC) + 3}"

# work around for https://bugzilla.yoctoproject.org/show_bug.cgi?id=3498
# webos has x11 DISTRO_FEATURE included so pulseaudio-module-console-kit is added to
# RDEPENDS in oe-core recipe, one possible solution is to add consolekit to
# packagegroup-webos-ports-extended, but we don't really need it, so remove it here
# RDEPENDS_pulseaudio-server += "\
#         ${@base_contains('DISTRO_FEATURES', 'x11', 'pulseaudio-module-console-kit', '', d)}"
RDEPENDS_pulseaudio-server = " \
    pulseaudio-module-filter-apply \
    pulseaudio-module-filter-heuristics \
    pulseaudio-module-udev-detect \
    pulseaudio-module-null-sink \
    pulseaudio-module-device-restore \
    pulseaudio-module-stream-restore \
    pulseaudio-module-card-restore \
    pulseaudio-module-augment-properties \
    pulseaudio-module-detect \
    pulseaudio-module-alsa-sink \
    pulseaudio-module-alsa-source \
    pulseaudio-module-alsa-card \
    pulseaudio-module-native-protocol-unix \
    pulseaudio-module-default-device-restore \
    pulseaudio-module-intended-roles \
    pulseaudio-module-rescue-streams \
    pulseaudio-module-always-sink \
    pulseaudio-module-suspend-on-idle \
    pulseaudio-module-position-event-sounds \
    pulseaudio-module-role-cork \
    pulseaudio-module-switch-on-port-available"
