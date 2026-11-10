SUMMARY = "Discord protocol plug-in for libpurple"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

require purple-connectors.inc

# Built from luneos-synergy-connectors, the version ended in its commit alone (1.0+git0+d22c6be58f).
# With the upstream commit now in front it can sort lower, so bump the epoch to keep package feeds
# moving forward.
PE = "5"

# One feature per patch. 0001 is LuneOS-only: it lets the plugin id be set at build time, which
# upstream declined (EionRobb/purple-discord#498). 0002 and 0003 are generic fixes submitted
# upstream (#503 and #504); the rest adapt the plug-in to imlibpurpletransport and the webOS
# Messaging app.
SRC_URI += " \
    git://github.com/EionRobb/purple-discord.git;branch=master;protocol=https;name=upstream;destsuffix=purple-discord \
    file://0001-Allow-the-plugin-id-to-be-set-at-build-time.patch \
    file://0002-Make-file-uploads-survive-a-briefly-unavailable-conn.patch \
    file://0003-Send-Ogg-Opus-uploads-as-voice-messages.patch \
    file://0004-webOS-deduplicate-messages-and-bridge-message-ids-re.patch \
    file://0005-webOS-add-DM-partners-to-the-buddy-list-and-backfill.patch \
    file://0006-webOS-sign-in-with-a-token-password-or-QR-code-and-r.patch \
    file://0007-webOS-Add-gateway-and-channel-list-diagnostics.patch \
    file://0008-webOS-edit-and-delete-sent-messages-from-the-Messagi.patch \
    file://0009-webOS-create-add-to-and-leave-group-DMs-from-the-Mes.patch \
    file://0010-webOS-Send-the-mentions-the-Messaging-app-picked.patch \
    file://0011-webOS-Edit-and-delete-received-messages-in-place.patch \
    file://0012-webOS-rename-group-DMs-and-take-people-out-of-them-a.patch \
    file://0013-webOS-Say-who-pinned-a-message-not-the-message-again.patch \
"
SRCREV_upstream = "33f21e5af74c1489f6c6e2db0b84b30d15783596"

DEPENDS = "pidgin json-glib glib-2.0 zlib qrencode openssl"

S = "${UNPACKDIR}/purple-discord"

# QR remote-auth with upstream's OpenSSL backend; USE_OPENSSL_CRYPTO=1 makes the Makefile
# take the RSA code from openssl rather than NSS, and pull in libqrencode, both via pkg-config.
#
# imlibpurpleservice and the account template know the protocol as "prpl-discord", not upstream's
# "prpl-eionrobb-discord", so 0001 lets the id be set at build time. It is defined in a header that
# every compile force-includes, rather than as -DDISCORD_PLUGIN_ID="..." in CFLAGS: the string's
# quotes do not survive bitbake's export of CFLAGS and make's shell intact.
do_configure() {
    echo '#define DISCORD_PLUGIN_ID "prpl-discord"' > ${B}/luneos-plugin-id.h
}
CFLAGS:append = " -include ${B}/luneos-plugin-id.h"

EXTRA_OEMAKE = " \
    CC='${CC}' \
    PKG_CONFIG='${STAGING_BINDIR_NATIVE}/pkg-config' \
    USE_OPENSSL_CRYPTO=1 \
"

do_compile() {
    oe_runmake
}

# Installed by hand rather than with `make install`, so no Pidgin pixmaps are shipped -- see
# purple-connectors.inc. Here `make install` cannot run at all anyway: it depends on discord16.png
# and friends, which the Makefile renders from discord-alt-logo.svg with ImageMagick.
#
# Animated stickers are rendered through librlottie, which upstream dlopen()s at runtime; without
# it installed they are simply shown as static images.
do_install() {
    install -d ${D}${libdir}/purple-2
    install -m 0755 ${S}/libdiscord.so ${D}${libdir}/purple-2/libdiscord.so

    # Account template, so Discord can actually be added as an account.
    install -d ${D}${webos_accttemplatesdir}
    cp -rf ${CONNECTORS_DIR}/im/discord/account/com.palm.discord ${D}${webos_accttemplatesdir}/

    # Validator app named by the template's validator.customUI.appId. Only com.palm.app.discord:
    # apps/com.palm.app.discordqr is the superseded full-screen QR card, which validator.js now
    # renders inline instead.
    install -d ${D}${webos_applicationsdir}
    cp -rf ${CONNECTORS_DIR}/im/discord/apps/com.palm.app.discord ${D}${webos_applicationsdir}/
}
