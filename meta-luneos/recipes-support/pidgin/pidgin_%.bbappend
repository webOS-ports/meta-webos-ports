# We're only interested in the libpurple libraries and not the UI or
# any other features so we keep those things disabled here.
PACKAGECONFIG:remove = "consoleui"

EXTRA_OECONF += " \
    --enable-plugins \
    --enable-debug \
"

# ${PN} package is empty
RDEPENDS:${PN}-dev = ""
