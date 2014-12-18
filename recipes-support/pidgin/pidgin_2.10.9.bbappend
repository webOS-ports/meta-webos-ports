# We're only interested in the libpurple libraries and not the UI or
# any other features so we keep those things disabled here.
EXTRA_OECONF += " \
    --with-x=no \
    --disable-gtkui \
    --disable-gstreamer \
    --disable-farsight \
    --disable-vv \
"

# Remove various dependencies we don't need
DEPENDS = "python avahi gnutls virtual/libintl dbus intltool-native libidn"
