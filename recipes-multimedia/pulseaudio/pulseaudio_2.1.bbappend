PRINC := "${@int(PRINC) + 5}"

# drop ${@base_contains('DISTRO_FEATURES', 'x11', 'virtual/libx11 libxtst libice libsm libxcb gtk+', '', d)}"
DEPENDS = "libatomics-ops liboil avahi libsamplerate0 libsndfile1 libtool"
# pulseaudio_2.1 has few more DEPENDS:
DEPENDS += "libjson gdbm speex libxml-parser-perl-native"

# disable-gtk2/disable-x11 even with x11 in DISTRO_FEATURES
# ${@base_contains('DISTRO_FEATURES', 'x11', '--enable-x11', '--disable-x11 --disable-gtk2', d)}
EXTRA_OECONF = "\
                --disable-lynx \
                ${@base_contains('DISTRO_FEATURES', 'bluetooth', '--enable-bluez', '--disable-bluez', d)} \
                --disable-polkit \
                --disable-x11 --disable-gtk2 \
                --without-jack \
                --with-glib \
                --with-alsa \
                --with-oss \
                --without-hal \
                --disable-hal \
                --disable-orc \
                --enable-tcpwrap=no \
                --with-access-group=audio \
                --disable-openssl \
"

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

DEFAULT_CONF = "${D}${sysconfdir}/pulse/default.pa"
DEFAULT_CONF_TMP = "${DEFAULT_CONF}.temp"

do_install_append() {
    # Modify configuration to load android alsa plugin instead of default one
    grep -v "^load-module module-alsa" ${DEFAULT_CONF} > ${DEFAULT_CONF_TMP}
    echo "load-module module-alsa-sink device=android alternate_rate=16000 control=\"Master\"" >> ${DEFAULT_CONF_TMP}
    echo "load-module module-alsa-source device=android alternate_rate=16000 channels=1" >> ${DEFAULT_CONF_TMP}
    mv ${DEFAULT_CONF_TMP} ${DEFAULT_CONF}
}
