DESCRIPTION = "Basic set of components use by the webOS ports project"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

NOT_COMPATIBLE_WITH_CURRENT_NODEJS = " \
  node-sqlite3 \
"

#LuneOS uses it's own settings app
VIRTUAL-RUNTIME_settingsapp ?= "org.webosports.app.settings-qml"

# Web Speech API support for the browser. Chromium dlopens libspeechd.so.2 and talks to the
# speech-dispatcher daemon; without this, speechSynthesis exists but has no voices and pages that
# speak stay silent. Set to "" in a distro/local conf to drop it (it pulls in espeak and portaudio).
VIRTUAL-RUNTIME_speech_synthesis ?= "speech-dispatcher"

# Atlas is the default browser (VIRTUAL-RUNTIME_com.webos.app.browser) and is also listed below, so it
# ships whichever browser is default. enactbrowser stays installed because run_browser_shell loads its
# pdf.js as a Chromium extension for EVERY browsershell app — drop that package and Atlas loses
# in-browser PDF as well.

# ttf-noto-emoji-color: nothing in the image could draw a codepoint above
# U+FFFF, so every emoji was a tofu box. 9.5MB, and it serves Chromium apps, QML
# apps, the shell and the keyboard at once. Chromium reads the font list once at
# start, so a hand-install needs webapp-mgr restarted to take effect.
RDEPENDS:${PN} = " \
  ${DISTRO_EXTRA_RDEPENDS} \
  \
  luneos-device-config \
  luneos-kernel-log-quirks \
  \
  ttf-noto-emoji-color \
  \
  powertop \
  luneos-power-report \
  luneos-wake-report \
  luneos-remote-wakelock \
  \
  pulseaudio-distro-conf \
  pulseaudio-misc \
  pulseaudio-module-loopback \
  pulseaudio-module-switch-on-connect \
  pulseaudio-module-bluetooth-discover \
  pulseaudio-module-bluetooth-policy \
  pulseaudio-module-bluez5-device \
  pulseaudio-module-bluez5-discover \
  pulseaudio-server \
  \
  alsa-conf \
  ${VIRTUAL-RUNTIME_ofono} \
  tar \
  udev-extraconf \
  umtprd \
  webos-connman-adapter \
  ${VPN_RDEPENDS} \
  webos-telephonyd \
  iw \
  \
  bluez5 \
  \
  ${PRINT_RDEPENDS} \
  \
  imaccountvalidator \
  imlibpurpleservice \
  messaging-accounts \
  \
  com.palm.app.preware2 \
  org.webosports.service.ipkg \
  ${TORCH_RDEPENDS} \
  com.webos.app.enactbrowser \
  \
  ${VIRTUAL-RUNTIME_speech_synthesis} \
  \
  com.palm.app.backup \
  org.webosports.app.atlas \
  org.webosports.app.calculator \
  org.webosports.app.contacts \
  org.webosports.app.filemanager \
  org.webosports.app.firstuse \
  org.webosports.app.maps \
  org.webosports.app.memos \
  org.webosports.app.messaging \
  org.webosports.app.pdf \
  org.webosports.app.phone \
  org.webosports.app.photos \
  ${VIRTUAL-RUNTIME_settingsapp} \
  org.webosports.app.tasks \
  \
  org.webosports.cdav \
  org.webosports.tweaks \
  \
  org.webosports.service.devmode \
  org.webosports.service.licenses \
  org.webosports.service.lumberjack \
  org.webosports.service.messaging \
  org.webosports.service.update \
  \
  \
  ca-certificates \
  certmgrd \
  pmcertificatemgr \
  \
  qtbase-plugins \
  qtmultimedia-plugins \
  qtmultimedia-qmlplugins \
  qtsensors \
  qtsensors-qmlplugins \
  qtwayland \
  qtwayland-plugins \
  qtconnectivity \
  qtsensors-sensorfw-plugin \
  \
  sensorfw \
  \
  luna-appmanager \
  luna-authmanager \
  luna-backupagent \
  luna-displaymanager \
  luna-haptics \
  luna-next-cardshell \
  luna-sysmgr-conf \
  luneos-system-sounds \
  luneos-components \
  qtlocation-luneos-plugin \
  webos-system-update \
  \
  webos-users-groups \
  \
  packagegroup-luneos-audiod \
  com.palm.keymanager \
  mediaindexer \
  media-permission-service \
  webos-systemsounds \
  \
  luneos-default-wallpapers \
  \
  storaged \
  org.webosports.app.messwerk \
  org.mer.app.fingerterm \
  org.webosports.app.terminal \
  \
"

# qbootctl is listed unconditionally: LIBHYBRIS_RDEPENDS is only ever appended
# for halium machines, one by one, below. The recipe is
# COMPATIBLE_MACHINE = "^halium$" and its unit is conditional on the device
# being A/B at runtime, so a non-A/B halium machine installs it harmlessly.
LIBHYBRIS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_android-system-image} \
    android-property-service \
    android-system \
    halium-udev-rules \
    qbootctl \
    android-system-compat \
    android-tools \
    android-tools-adbd \
    lxc \
    pulseaudio-modules-droid \
    pulseaudio-modules-droid-hidl \
    gst-droid \
    luneos-rtc-engine \
    qt6-qpa-hwcomposer-plugin \
    bluebinder \
    \
    exiv2 \
    libpulse-simple0 \
    nyx-modules-hybris \
    \
    ofono-binder-plugin \
    wlan-suspend-mode \
    wlan-dynamic-start \
"

# Fingerprint stack: biomd talks to the Android biometrics HAL over binder
# through libgbinder, webos-fingerprint-adapter bridges its D-Bus API onto the
# luna-service2 bus for the shell (lockscreen unlock) and the Settings app
# (enrollment). Only added for machines that actually have a fingerprint
# sensor.
#
# This was droidian-fpd, which reaches the same HAL through libhybris and
# dlopens libbiometry_fp_api.so - a shim that has to be built into the device's
# Halium system image, and is not in ours. fpd does not check the dlopen
# result, so it calls through a NULL pointer and segfaults on every start,
# leaving the sensor dead. biomd needs nothing from the Android side.
FINGERPRINT_RDEPENDS = " \
    biomd \
    webos-fingerprint-adapter \
    ${VIRTUAL-RUNTIME_settingsapp}-fingerprint \
"

FACEUNLOCK_RDEPENDS = " \
    luneos-faced \
    ${VIRTUAL-RUNTIME_settingsapp}-faceunlock \
"

# kbdscroll: turns a slide over a touch surface that is not the touchscreen - a
# capacitive keymat, or a trackpad beside the keys - into scrolling, and into
# cursor movement with Alt held.
#
# Two features, for the two shapes the hardware comes in: keyboard-touch (the
# keys themselves are the surface, e.g. athena) and trackpad (a separate pad,
# e.g. q25). Deliberately not gated on keyboard-qwerty/keyboard-t9 - most
# keyboards have no touch surface. An optical pad reporting REL_X/REL_Y is
# already a pointer and is not what this reads.
KEYBOARD_TOUCH_RDEPENDS = " \
    kbdscroll \
"

# Camera, gated on the topology rather than unconditional: face unlock needs a
# FRONT-facing sensor. camera-rear and camera-front are the usual pair;
# camera-swivel is one sensor on a rotating mount, declared instead of both.
# The HAL half lives elsewhere - gst-droid in LIBHYBRIS_RDEPENDS,
# com.webos.service.camera in packagegroup-webos-extended.
CAMERA_RDEPENDS = " \
    org.webosports.app.camera \
    v4l-utils \
    libcamera \
    libcamera-gst \
"

# libcamera is what actually drives a mainline camera, so it belongs here rather
# than in each machine's own MACHINE_EXTRA_RDEPENDS - which is where it lived,
# meaning every new mainline port silently shipped a camera app with nothing
# behind it until someone noticed.
#
# The reason it is needed at all is that the kernel side stops short of
# streaming: qcom-camss, and the other mainline camera subsystems, leave every
# link in their media graph disabled and expect userspace to build the pipeline
# - sensor, CSIPHY, CSID, ISPIF, VFE - before a frame moves. Nothing else does
# that. libcamera also carries the software ISP, which is not optional in
# practice: sensors come up at minimum gain with nothing driving exposure, so
# captures without it are nearly black and have no white balance.
#
# Halium machines are the exception. Their cameras go through the Android HAL
# via gst-droid in LIBHYBRIS_RDEPENDS above, so libcamera would be dead weight -
# it cannot see a camera that only exists behind binder.
CAMERA_RDEPENDS:remove:halium = "libcamera libcamera-gst"

# NFC stack: nfcd talks to the Android NFC HAL over binder, webos-nfc-adapter
# bridges its D-Bus API onto the luna-service2 bus for apps and the shell.
# Only added for machines that actually have an NFC controller.
NFC_RDEPENDS = " \
    nfcd \
    nfcd-tools \
    nfcd-binder-plugin \
    webos-nfc-adapter \
    ${VIRTUAL-RUNTIME_settingsapp}-nfc \
"

# Infrared transmitter: irblasterd puts it on the bus (Samsung's sec_ir FPGA or
# /dev/lirc0), and Remote turns the tablet or phone into a universal remote.
# Only added for machines that have one: the "ir-blaster" feature, not the
# standard "irda", which is OE's name for the IrDA serial data link.
IR_BLASTER_RDEPENDS = " \
    org.webosports.service.ir \
    org.webosports.app.remote \
"

# Printing. luneos-print-adapter serves the webOS print API (com.palm.printmgr,
# what the enyo print dialog in Atlas and Email call) and the flatter
# org.webosports.service.print the Settings Print Manager page calls, both on
# top of CUPS. Not gated on a machine: anything with a network can print, and
# discovery is mDNS, so there is no hardware to be present or absent.
#
# cups-filters is what converts PDF to the Apple Raster and PWG Raster that many
# AirPrint printers accept instead of PDF. Without it printing only works where
# the printer takes PDF or JPEG as-is - measured, not assumed: a driverless
# queue rejected a PDF with client-error-document-format-not-supported until it
# was installed. It brings ghostscript, poppler and qpdf - about 40MB of ipks.
# Drop it with BAD_RECOMMENDATIONS if an image would rather have the space than
# the printer coverage; cups alone still prints to anything taking PDF or JPEG.
PRINT_RDEPENDS = " \
    luneos-print-adapter \
    cups \
    cups-filters \
    avahi-daemon \
"

VPN_RDEPENDS = " \
    luneos-vpn-adapter \
"

TORCH_RDEPENDS = " \
    org.webosports.service.torch \
    org.webosports.app.torch \
"

# Hardware privacy switches. The shell's status-bar indicator, its alert and the
# camera app all key off what this daemon reports. A machine declaring the
# feature must also ship conf/killswitchd.conf.<machine> in the service's own
# repo; without it the unit's ConditionPathExists keeps systemd from starting it.
KILLSWITCH_RDEPENDS = " \
    org.webosports.service.killswitch \
"

# cfg80211 regulatory database. Only linux-firmware-pine64 pulled it in before,
# so every phone ran without one - and with no regulatory.db the kernel stays in
# the built-in "world" domain, where every 5 GHz channel is NO-IR/PASSIVE-SCAN so
# the radio never probes there at all (radon: 25 usable 5 GHz channels in the
# PHY, none visible to connman).
#
# -static is the variant the kernel loads directly from /lib/firmware; the
# non-static package is the CRDA layout nothing has used since 4.15, and the two
# RCONFLICT. Needs wens.hex in the kernel's net/wireless/certs on a tree built
# with CONFIG_CFG80211_REQUIRE_SIGNED_REGDB, or the db is loaded then rejected.
# cfg80211-regdb-reload is the other half; see its recipe.
WIRELESS_REGDB_RDEPENDS = " \
    wireless-regdb-static \
    cfg80211-regdb-reload \
"

# eSIM: lpac is the LPA (SGP.22 profile download/management), luneos-esim-adapter
# bridges it and ofono's EuiccManager onto the luna-service2 bus for the
# Settings app. Useful on any device whose modem can open logical channels -
# an eSIM adapter card in the SIM slot counts, so this is not restricted to
# machines with a soldered eUICC.
# Settings pages that only make sense with a modem: cell broadcast, and Network Settings (mobile data,
# roaming, APNs, SIMs). webos-telephonyd itself stays on every machine, other components query it.
MODEM_SETTINGS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_settingsapp}-cellbroadcast \
    ${VIRTUAL-RUNTIME_settingsapp}-networksettings \
"

ESIM_RDEPENDS = " \
    lpac \
    luneos-esim-adapter \
    gstreamer1.0-plugins-bad-zbar \
    ${VIRTUAL-RUNTIME_settingsapp}-esim \
"

# (Optional?) work for Qt6:
#     qtscenegraph-adaptation

# qtubuntu-camera, libqtubuntu-media-signals and qtvideo-node were the Ubuntu
# Touch camera stack, dropped in the Qt5 -> Qt6 migration. Their replacement
# is gst-droid (in LIBHYBRIS_RDEPENDS above): droidcamsrc/droiddec reach the
# vendor camera HAL and codecs through the droidmedia services in the Android
# container, and Qt 6 Multimedia's gstreamer backend sits on top.
#
# The service that sits in front of it, com.webos.service.camera, is NOT here:
# it is machine-independent and ships from packagegroup-webos-extended next to
# com.webos.app.camera. Its droid HAL and notifier plugins link nothing but
# GStreamer and reach gst-droid through gst_element_factory_make("droidcamsrc"),
# so the same package serves the v4l2 path on mainline machines.

# Every Halium machine gets the stack through the "halium" override that
# meta-android-halium.inc already puts in MACHINEOVERRIDES, rather than through
# one line per device. The per-device form silently excluded any new machine -
# halium-arm64 built a complete image with no container, no HAL bridges and no
# device-config service, because nothing had added it to the list yet.
RDEPENDS:${PN}:append:halium = " ${LIBHYBRIS_RDEPENDS}"

# hammerhead-halium brings Bluetooth up with hciattach on /dev/ttyHS99 (see
# systemd-machine-units). bluebinder asks the Android Bluetooth HAL to open that
# same UART, and cannot succeed on this kernel: the vendor library looks for the
# bluesleep /proc entries and an rfkill power switch, which the backports-based
# Bluetooth stack does not have, so the HAL never answers HCI_Reset. It then
# restarts forever (115 times in one session) and fights hciattach for the port.
# With it left out hci0 comes up with an address and bluez works.
RDEPENDS:${PN}:remove:hammerhead-halium = "bluebinder"

# tenderloin-halium too: its CSR BlueCore is attached on the host by tenderloin-bluetooth-utilities (BCSP on
# /dev/ttyHS0) and there is no Android Bluetooth HAL in its vendor at all. bluebinder's ExecStartPre then
# waits for that HAL for ever, and bluetooth.service, ordered after it, never starts: hci0 stays down and
# nothing can scan (7 Oct 2026).
RDEPENDS:${PN}:remove:tenderloin-halium = "bluebinder"

# sm-t520 also leaves bluebinder out (see the end of this file, where its remove list is).

RDEPENDS:${PN}:append:hammerhead = " alsa-utils-systemd mesa-megadriver rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:tenderloin = " alsa-utils-systemd rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:tenderloin71 = " alsa-utils-systemd rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:tenderloin3g = " alsa-utils-systemd rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:mido = " alsa-utils-systemd mesa-megadriver rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:tissot = " alsa-utils-systemd mesa-megadriver rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:rosy = " alsa-utils-systemd mesa-megadriver rmtfs qrtr rpmsgexport"
RDEPENDS:${PN}:append:a3-2015 = " alsa-utils-systemd mesa-megadriver rmtfs qrtr rpmsgexport"

RDEPENDS:${PN}:append:tissot = " wcnss-filter-fixup"
RDEPENDS:${PN}:append:tissot-halium = " wcnss-filter-fixup"

# Hardware-gated stacks, driven by MACHINE_FEATURES.
#
# These were per-machine RDEPENDS:append lines, which put the hardware question
# in the wrong place and got it wrong - radon shipped nfcd and biomd for hardware
# the FLX1s has no HAL for. The machine declares what it has; this only maps that
# to packages. nfc is the standard OE feature; fingerprint, esim, faceunlock,
# ir-blaster, keyboard-touch and trackpad are LuneOS-specific.
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'fingerprint', ' ${FINGERPRINT_RDEPENDS}', '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'nfc',         ' ${NFC_RDEPENDS}',         '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'esim',        ' ${ESIM_RDEPENDS}',        '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'ir-blaster',  ' ${IR_BLASTER_RDEPENDS}',  '', d)}"
# These settings pages need a modem; the machine says so with "phone".
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'phone',       ' ${MODEM_SETTINGS_RDEPENDS}', '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'wifi',        ' ${WIRELESS_REGDB_RDEPENDS}', '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'killswitch',   ' ${KILLSWITCH_RDEPENDS}', '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains_any('MACHINE_FEATURES', 'keyboard-touch trackpad', ' ${KEYBOARD_TOUCH_RDEPENDS}', '', d)}"
# ...and unconditionally on the generic rootfs, because that machine cannot see
# the feature. athena declares keyboard-touch and q25 declares trackpad, but
# neither builds a rootfs of its own - they build a boot image and run
# halium-arm64's - so the line above evaluates against halium-arm64's
# MACHINE_FEATURES, which has neither, and kbdscroll never reached either
# device. Same shape as 70-q25.rules and the luneos-device-config adaptations:
# ship it on the generic image and let the runtime decide.
#
# Safe because that is already how kbdscroll behaves: with no touch surface
# besides the touchscreen it logs "nothing to do" and exits 0, deliberately, so
# systemd does not restart it. Its own comment says the package is expected on
# machines that have no surface.
RDEPENDS:${PN}:append:halium-arm64 = " ${KEYBOARD_TOUCH_RDEPENDS}"
RDEPENDS:${PN}:append:halium-arm = " ${KEYBOARD_TOUCH_RDEPENDS}"
RDEPENDS:${PN}:append = "${@bb.utils.contains_any('MACHINE_FEATURES', 'camera-front camera-rear camera-swivel', ' ${CAMERA_RDEPENDS}', '', d)}"

# Face unlock needs a camera pointing AT the user. A rear-only device must not
# get luneos-faced: it would enumerate the only camera it has and try to
# authenticate against whatever that is aimed at. camera-swivel counts as
# front-capable - the sensor rotates to face the user.
#
# "faceunlock" remains accepted as an explicit opt-in for machines that have not
# yet declared their camera topology; drop it once they do.
RDEPENDS:${PN}:append = "${@bb.utils.contains_any('MACHINE_FEATURES', 'camera-front camera-swivel faceunlock', ' ${FACEUNLOCK_RDEPENDS}', '', d)}"

# Keep this list in step with COMPATIBLE_MACHINE in waydroid.bb: that only
# decides whether the recipe may build, and nothing else pulls waydroid into an
# image. A machine enabled there but missing here builds the ipk and then ships
# an image without it - which is how mindphone, halium-arm64 and rpi all went
# out with no container despite being wired up for one.
#
# ":halium" covers every machine with an Android container, which is waydroid's
# actual requirement; the mainline machines that can run it are named explicitly.
# rpi gets waydroid but not waydroid-sensors, as before.
RDEPENDS:${PN}:append:halium = " waydroid waydroid-sensors"
RDEPENDS:${PN}:append:pinephone = " waydroid waydroid-sensors"
RDEPENDS:${PN}:append:pinephonepro = " waydroid waydroid-sensors"
RDEPENDS:${PN}:append:pinetab2 = " waydroid waydroid-sensors"
RDEPENDS:${PN}:append:qemux86-64 = " waydroid waydroid-sensors"
RDEPENDS:${PN}:append:rpi = " waydroid"

# Waydroid needs a kernel of 3.18 or newer (binder, ashmem and the namespaces its
# LineageOS container uses), and the four Halium machines on a 3.4 vendor kernel
# (sm-t520 is the fourth, the Exynos 5420 tablet) have none of that. The ":halium" line above would otherwise pull waydroid and its
# 18.1 system image into them; for mako-halium, which is 32-bit ARM, that fetch
# does not even work, because the checksums in waydroid-data are the arm64 builds.
RDEPENDS:${PN}:remove:hammerhead-halium = "waydroid waydroid-sensors"
RDEPENDS:${PN}:remove:tenderloin-halium = "waydroid waydroid-sensors"
RDEPENDS:${PN}:remove:mako-halium = "waydroid waydroid-sensors"
# bluebinder as well: the tablet's Android Bluetooth HAL aborts with "Unimplemented packet type 12"
# after opening /dev/ttySAC0, so bluebinder only times out, and bcm-hciattach.service in
# systemd-machine-units brings the controller up instead, as on hammerhead-halium.
RDEPENDS:${PN}:remove:sm-t520 = "waydroid waydroid-sensors bluebinder"

QEMU_RDEPENDS = " \
    alsa-utils-systemd \
    mesa-megadriver \
    kernel-module-snd-intel8x0 \
    phonesim \
    qt-plugin-generic-vboxtouch \
    rng-tools \
    vmwgfx-layout \
"

RDEPENDS:${PN}:append:qemux86 = " ${QEMU_RDEPENDS}"
RDEPENDS:${PN}:append:qemux86-64 = " ${QEMU_RDEPENDS}"

RDEPENDS:${PN}:append:arm = " \
    crash-handler \
"
