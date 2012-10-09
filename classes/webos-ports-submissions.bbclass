# NOTE: -wop prefix is here to indicate the source is being modified by the webos-ports project
# and is different to what openwebos provides.
PR_append = "+wop+gitr${SRCPV}"

SRC_URI = "git://github.com/webOS-ports/${PN};branch=webOS-ports/master;protocol=git"
