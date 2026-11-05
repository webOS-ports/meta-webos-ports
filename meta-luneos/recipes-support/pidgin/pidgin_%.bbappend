# We're only interested in the libpurple libraries and not the UI or
# any other features so we keep those things disabled here.
PACKAGECONFIG:remove = "consoleui"

# SASL for prpl-irc: IRC networks such as Libera.Chat log a registered nick in with SASL. Without
# cyrus-sasl, irc.c registers no "sasl" account option at all (it is under HAVE_CYRUS_SASL), so
# the IRC sign-in app's SASL setting would be dropped and the password sent as a server password.
# prpl-jabber then authenticates through Cyrus SASL as well (auth_cyrus.c, USE_CYRUS_SASL).
# cyrus-sasl ships its mechanism plug-ins (PLAIN among them) in its main package, which libpurple
# picks up as a shared-library dependency.
PACKAGECONFIG:append = " cyrus-sasl"

EXTRA_OECONF += " \
    --enable-plugins \
    --enable-debug \
"

# ${PN} package is empty
RDEPENDS:${PN}-dev = ""
