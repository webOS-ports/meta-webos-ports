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

RDEPENDS:${PN} = " \
  ${DISTRO_EXTRA_RDEPENDS} \
  \
  luneos-device-config \
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
  org.webosports.app.camera \
  \
  v4l-utils \
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

# Face unlock. luneos-faced pulls frames from com.webos.service.camera2 into
# shared memory with no preview, runs them through libfart (detection,
# MiniFASNet passive anti-spoofing, MobileFaceNet match) and exposes
# com.webos.service.faceunlock for the shell (lockscreen) and the Settings app
# (enrollment). libfart comes in as an RDEPENDS of luneos-faced.
#
# NOT halium-only. luneos-faced talks to com.webos.service.camera2 over luna,
# which is machine-independent and serves the v4l2 path on mainline machines just
# as it serves droid on halium ones - the same reason com.webos.service.camera
# itself ships from packagegroup-webos-extended rather than from here. libfart's
# one Android-shaped dependency, the NNAPI delegate, is off by default since
# FART_ENABLE_NNAPI, precisely so it works where hwservicemanager does not exist.
#
# The gate is 64-bit, not halium. libfart bundles ~66 MB of TFLite models and
# drags in opencv and libtensorflow-lite, which is not worth putting on the armv7
# devices (mako, tenderloin, hammerhead, athene, onyx, halium-arm, mindphone) -
# where none of it has ever been built, let alone run an inference at a sensible
# frame rate. aarch64 covers the halium machines and the mainline pine64 ones
# alike, and does not silently exclude a new machine the way a per-device list
# does.
#
# This has to track REMOVE_FACEUNLOCK_APP_CMD in org.webosports.app.settings-qml:
# ship the panel without the service and the shell just retries
# com.webos.service.faceunlock every three seconds while ls-hubd logs
# LSHUB_NOT_LSTED for a bus name nothing ever registers.
FACEUNLOCK_RDEPENDS = " \
    luneos-faced \
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

# VPN. luneos-vpn-adapter bridges connman-vpnd (net.connman.vpn over gio) onto
# the luna-service2 bus as com.webos.service.vpn, which is what the Settings VPN
# panel calls - getAgentFormFields, getProfileDetails, importProfile,
# importCertificate, uiPromptResponse. Ship the panel without it and every one of
# those answers "Service does not exist: com.webos.service.vpn".
#
# Not gated on a machine, for the same reason printing is not: connman-vpnd is
# pure mainline, there is no Android side to it, and connman-vpn is already in
# the image. The adapter itself is ~150 KB and its runtime deps (connman-vpn,
# glib, libpbnjson, luna-service2) are all present already.
#
# The per-provider tunnel plugins are RRECOMMENDS of the adapter rather than
# listed here, so what getAgents reports as available follows the package that
# actually consumes them.
VPN_RDEPENDS = " \
    luneos-vpn-adapter \
"

# Torch (flashlight). org.webosports.service.torch serves the torch API on the
# luna-service2 bus for the shell's power menu; the shell subscribes to it and
# renders the entry "Unavailable" when nobody answers, which looks exactly like a
# device with no torch at all.
#
# Not gated on a machine: everything device-specific sits behind NYX_DEVICE_LED
# "Torch", which resolves at runtime to whichever nyx module the machine built -
# the sysfs one or the hybris camera-service one - so the service itself only
# needs nyx-lib. Verified on sargo: nyxLedTorch.module is installed and
# /sys/class/leds/led:torch_0 drives the LED.
#
# The client grant the shell needs (torch.operation, which luna-surfacemanager
# does not list for com.webos.surfacemanager*) ships from the component itself,
# so there is nothing to add here for it.
TORCH_RDEPENDS = " \
    org.webosports.service.torch \
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

# Fingerprint-sensor devices only. These machine names come from the LuneOS
# Halium layer; on a tree without them the overrides are simply inert.
RDEPENDS:${PN}:append:sagit = " ${FINGERPRINT_RDEPENDS}"
RDEPENDS:${PN}:append:mido-halium = " ${FINGERPRINT_RDEPENDS}"
RDEPENDS:${PN}:append:tissot-halium = " ${FINGERPRINT_RDEPENDS}"
# The GSI machine can land on any device; the stack is harmless without a
# sensor (the adapter just reports unavailable).
RDEPENDS:${PN}:append:halium-arm64 = " ${FINGERPRINT_RDEPENDS}"

# Face unlock, on every 64-bit machine - halium and mainline alike. Harmless on
# one with no usable camera: luneos-faced just reports available=false.
RDEPENDS:${PN}:append:aarch64 = " ${FACEUNLOCK_RDEPENDS}"

# NFC-capable devices only.
RDEPENDS:${PN}:append:mako = " ${NFC_RDEPENDS}"
RDEPENDS:${PN}:append:hammerhead-halium = " ${NFC_RDEPENDS}"
RDEPENDS:${PN}:append:sagit = " ${NFC_RDEPENDS}"
# The GSI machine can land on any device; the stack is harmless without an
# NFC controller (nfcd just reports unavailable).
RDEPENDS:${PN}:append:halium-arm64 = " ${NFC_RDEPENDS}"

# sargo has a real eUICC and the generic halium images cover it and the rest,
# where an adapter card is the way in. No :sargo line: sargo builds only a boot
# image now (see sargo.conf) and runs this rootfs.
RDEPENDS:${PN}:append:halium-arm64 = " ${ESIM_RDEPENDS}"

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
