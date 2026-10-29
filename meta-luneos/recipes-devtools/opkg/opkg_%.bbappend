# Copyright (c) 2013 LG Electronics, Inc.

# We're running opkg-postinst on first boot a little bit earlier to be able to run
# different needed hardware initilization after this which needs things only available
# after postinst-phase is done (e.g. android binaries).
POSTINSTALL_INITPOSITION = "80"

# Feed signature checking. NOT ENABLED: needs decision D6 (who holds the feed
# signing key, and how its public key reaches the device) first.
#
# LuneOS feeds are not signed today, so opkg is built without GPGME and
# opkg.conf leaves "option check_signature" commented out: any feed index
# fetched over plain HTTP is trusted. Setting LUNEOS_FEED_SIGNED = "1" (in the
# distro or local.conf) builds opkg with gpg and turns check_signature on.
# Only do that once the feeds are signed (sign_package_feed.bbclass with
# PACKAGE_FEED_GPG_NAME / PACKAGE_FEED_GPG_PASSPHRASE_FILE) and the public key
# is installed on the device for opkg; with check_signature on and no key,
# every "opkg update" fails.
#
# This only covers indexes opkg fetches itself ("opkg update"). The Preware
# service (org.webosports.service.ipkg) downloads Packages and packages with
# curl and installs local files, which check_signature does not cover.
LUNEOS_FEED_SIGNED ?= "0"

PACKAGECONFIG:append:class-target = "${@' gpg' if d.getVar('LUNEOS_FEED_SIGNED') == '1' else ''}"

do_install:append:class-target () {
	if [ "${LUNEOS_FEED_SIGNED}" = "1" ]; then
		sed -i -e 's/^# *option check_signature 1$/option check_signature 1/' ${D}${sysconfdir}/opkg/opkg.conf
		grep -q '^option check_signature 1$' ${D}${sysconfdir}/opkg/opkg.conf || \
			bbfatal "LUNEOS_FEED_SIGNED = 1 but check_signature could not be enabled in opkg.conf"
	fi
}
