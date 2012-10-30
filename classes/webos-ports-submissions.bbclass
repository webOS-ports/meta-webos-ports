# NOTE: -wop prefix is here to indicate the source is being modified by the webos-ports project
# and is different to what openwebos provides.
PR_append = "+wop+gitr${SRCPV}"

COMPONENT_NAME ?= "${PN}"

SRC_URI = "git://github.com/webOS-ports/${COMPONENT_NAME};branch=webOS-ports/master;protocol=git"
