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

# An emoji is only text if something can draw it, and nothing here could: the
# image ships Prelude and Liberation Mono, neither of which covers a single
# codepoint above U+FFFF, so every emoji came out as a tofu box. That is why the
# Messaging app carries bundled EmojiOne images for the ones it receives, and why
# there has been no emoji input to offer. ttf-noto-emoji-color fixes it for every
# consumer at once - Chromium apps, QML apps, the shell and the keyboard - at
# 9.5MB, which is what a colour bitmap font costs.
#
# Note for anyone testing by hand: Chromium reads the font list once at start, so
# installing this on a running device does nothing until webapp-mgr restarts.
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
  org.webosports.app.preware \
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
"

FACEUNLOCK_RDEPENDS = " \
    luneos-faced \
"

# A touch surface that is NOT the touchscreen: the capacitive keymat of a
# BlackBerry KEYone/KEY2/Passport, a trackpad beside the keys. kbdscroll turns a
# slide over it into scrolling and, with Alt held, into cursor movement - the
# behaviour that hardware has on its stock firmware and that nothing in a
# Wayland stack provides.
#
# Gated on its own feature, NOT on keyboard-qwerty/keyboard-t9: a physical
# keyboard says nothing at all about whether there is a touch surface behind it,
# and most of ours have none. Of the machines with hardware keys today:
#
#   athena   keyboard-touch  the keymat IS the surface ("touch_keypad")
#   q25      trackpad        a capacitive pad below the keys
#   mp01     neither         full QWERTY, plain mechanical keys
#   mindphone neither        T9 keypad, plain keys
#
# so the two names describe the two shapes the hardware comes in:
#
#   keyboard-touch  the keys themselves are a touch surface
#   trackpad        a touch pad separate from the keys
#
# A machine declares one of them once someone has run "kbdscroll --list" on the
# device and seen the surface come up as a second touch device. An optical pad
# that reports REL_X/REL_Y is already a pointer to the compositor and is not what
# this reads - see the comment at the top of kbdscroll.c.
KEYBOARD_TOUCH_RDEPENDS = " \
    kbdscroll \
"

# Camera. The app and v4l-utils used to be installed unconditionally, which is
# wrong on a device with no camera at all and says nothing about which cameras
# a device HAS - and that matters, because face unlock needs a FRONT-facing one.
#
# Three features, because the topology genuinely varies:
#
#   camera-rear     back-facing sensor
#   camera-front    user-facing sensor
#   camera-swivel   ONE sensor on a rotating mount that serves both roles
#                   (Asus ZenFone 6 / 7 style). Declare this INSTEAD of the
#                   other two: it is front-capable, so face unlock works, but
#                   there is only one camera to enumerate.
#
# A device with none of these gets no camera app and no v4l-utils.
#
# The HAL side is not here: gst-droid rides in LIBHYBRIS_RDEPENDS for every
# halium machine, and com.webos.service.camera ships from
# packagegroup-webos-extended. This is only the user-visible half.
CAMERA_RDEPENDS = " \
    org.webosports.app.camera \
    v4l-utils \
"

# NFC stack: nfcd talks to the Android NFC HAL over binder, webos-nfc-adapter
# bridges its D-Bus API onto the luna-service2 bus for apps and the shell.
# Only added for machines that actually have an NFC controller.
NFC_RDEPENDS = " \
    nfcd \
    nfcd-tools \
    nfcd-binder-plugin \
    webos-nfc-adapter \
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

# Hardware privacy switches: the daemon that reads them and tells the shell.
#
# Gated on a machine feature rather than listed per machine, because whether a
# device has switches is a fact about the hardware, and the shell's status bar
# indicator, its on-screen alert and the camera app's notice all key off what
# this daemon reports. A machine that declares the feature must also ship a
# conf/killswitchd.conf.<machine> in the service's own repo saying which
# switches exist and how to read each one - without it the daemon installs,
# finds nothing to report and exits, and the unit's ConditionPathExists stops
# systemd starting it at all.
KILLSWITCH_RDEPENDS = " \
    org.webosports.service.killswitch \
"

# The cfg80211 regulatory database. Nothing else in this tree installs it: the
# only reference was linux-firmware-pine64's RDEPENDS, so the three Pine
# machines had it and the other seventeen - every phone - did not.
#
# Without /lib/firmware/regulatory.db the kernel cannot load any regulatory
# domain, so cfg80211 stays on the built-in "world" domain no matter what the
# driver, wpa_supplicant or "iw reg set" ask for. In the world domain every
# 5 GHz channel is NO-IR/PASSIVE-SCAN, which does not merely restrict transmit
# power - it stops the radio ever probing there. Measured on radon (MT6877, 25
# usable 5 GHz channels in the PHY): country 00, "iw reg set NL" silently
# ignored, and the 5 GHz AP invisible to connman, so the device was pinned to
# 2.4 GHz.
#
# -static is the variant that installs regulatory.db + regulatory.db.p7s under
# /lib/firmware for the kernel to load directly; the non-static package is the
# CRDA/udev-helper layout, which no kernel since 4.15 uses. They RCONFLICT, so
# ask for exactly one.
#
# The signature matters: since 2023 upstream signs the db with the "wens" key
# rather than "sforshee". A kernel built with CONFIG_CFG80211_REQUIRE_SIGNED_REGDB
# and CONFIG_CFG80211_USE_KERNEL_REGDB_KEYS trusts only the certs shipped in
# net/wireless/certs, so on an older tree carrying sforshee.hex alone the db is
# loaded and then rejected. Check for wens.hex there before assuming this works
# on a given kernel - FuriLabs' 4.19 has both, so radon is fine.
#
# Gated on the "wifi" MACHINE_FEATURE rather than added unconditionally: it is
# ~5 KB, but a machine with no wireless has no cfg80211 to feed it.
# cfg80211-regdb-reload is the other half. Shipping the file is not enough on
# a kernel with cfg80211 built in: it asks for regulatory.db during its own
# initcalls, before any rootfs exists, and reg.c latches that failure in a
# static error pointer no later request_firmware clears - so the db sits on
# disk unread and "iw reg set" returns 0 while changing nothing. The service
# issues NL80211_CMD_RELOAD_REGDB once at startup, which clears it. Its own
# recipe carries the full reasoning.
WIRELESS_REGDB_RDEPENDS = " \
    wireless-regdb-static \
    cfg80211-regdb-reload \
"

# eSIM: lpac is the LPA (SGP.22 profile download/management), luneos-esim-adapter
# bridges it and ofono's EuiccManager onto the luna-service2 bus for the
# Settings app. Useful on any device whose modem can open logical channels -
# an eSIM adapter card in the SIM slot counts, so this is not restricted to
# machines with a soldered eUICC.
ESIM_RDEPENDS = " \
    lpac \
    luneos-esim-adapter \
    gstreamer1.0-plugins-bad-zbar \
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
# These used to be per-machine RDEPENDS:append lines. That put the hardware
# question in the wrong place - a packagegroup in meta-luneos had to know which
# devices had a fingerprint sensor - and it got answered wrong: radon shipped
# nfcd and biomd for hardware the FLX1s does not have. Its vendor partition
# carries no NFC HAL and no biometrics HAL at all (26 HAL services in
# /vendor/bin/hw, none matching), there is no NFC node in its device tree, and
# FuriLabs' own OS logs "Could not find
# android.hardware.biometrics.fingerprint@2.1::IBiometricsFingerprint/default".
# A daemon with no HAL behind it is dead weight in the image and one more thing
# failing in the journal.
#
# The machine declares what it has; this file only maps that to packages.
#
#   nfc              standard OE feature, already used by the machines that have it
#   fingerprint      LuneOS-specific
#   esim             LuneOS-specific
#   faceunlock       LuneOS-specific; needs a usable camera
#   keyboard-touch   LuneOS-specific; a touch surface on the keys themselves
#   trackpad         LuneOS-specific; a touch pad beside the keys
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'fingerprint', ' ${FINGERPRINT_RDEPENDS}', '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'nfc',         ' ${NFC_RDEPENDS}',         '', d)}"
RDEPENDS:${PN}:append = "${@bb.utils.contains('MACHINE_FEATURES', 'esim',        ' ${ESIM_RDEPENDS}',        '', d)}"
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
# No :mindphone line: mindphone.conf's MACHINEOVERRIDES carries "halium-arm",
# so it already picks up the :halium-arm line below the same way it picks up
# COMPATIBLE_MACHINE:halium-arm in waydroid.bb.
RDEPENDS:${PN}:append:halium-arm = " waydroid"
RDEPENDS:${PN}:append:halium-arm64 = " waydroid"
RDEPENDS:${PN}:append:mido-halium = " waydroid"
RDEPENDS:${PN}:append:pinephone = " waydroid"
RDEPENDS:${PN}:append:pinephonepro = " waydroid"
RDEPENDS:${PN}:append:pinetab2 = " waydroid"
RDEPENDS:${PN}:append:qemux86-64 = " waydroid"
RDEPENDS:${PN}:append:rpi = " waydroid"
RDEPENDS:${PN}:append:tissot-halium = " waydroid"

RDEPENDS:${PN}:append:halium-arm = " waydroid-sensors"
RDEPENDS:${PN}:append:halium-arm64 = " waydroid-sensors"
RDEPENDS:${PN}:append:mido-halium = " waydroid-sensors"
RDEPENDS:${PN}:append:pinephone = " waydroid-sensors"
RDEPENDS:${PN}:append:pinephonepro = " waydroid-sensors"
RDEPENDS:${PN}:append:pinetab2 = " waydroid-sensors"
RDEPENDS:${PN}:append:qemux86-64 = " waydroid-sensors"
RDEPENDS:${PN}:append:tissot-halium = " waydroid-sensors"

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
