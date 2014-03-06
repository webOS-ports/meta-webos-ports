# Backport fix to build without x11 DISTRO_FEATURE
PACKAGECONFIG ?= "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,libxcb"
