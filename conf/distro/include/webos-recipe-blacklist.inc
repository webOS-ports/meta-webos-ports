# Copyright (c) 2013 LG Electronics

# A blacklisted recipe is ignored by bitbake. The reason it is useful to be able
# to have bitbake ignore a recipe is because, although PREFERRED_PROVIDER can be
# used to select which recipe is used to fulfill a build-time dependency, bitbake
# does not have a PREFERRED_RPROVIDER to allow specification of which package
# should be used to satisfy a run-time dependency. When two recipes both provide
# common packages, but one recipe provides additional packages that the other
# doesn't, then the only way to guarantee that the first recipe will never be
# used is to blacklist it.

INHERIT += "blacklist"

include conf/distro/include/webos-recipe-blacklist-world.inc

PNBLACKLIST[gstreamer1.0] = "We still have to use gstreamer 0.10"
PNBLACKLIST[gstreamer1.0-plugins-bad] = "We still have to use gstreamer 0.10"
PNBLACKLIST[gstreamer1.0-plugins-base] = "We still have to use gstreamer 0.10"
PNBLACKLIST[gstreamer1.0-plugins-good] = "We still have to use gstreamer 0.10"
PNBLACKLIST[gstreamer1.0-plugins-ugly] = "We still have to use gstreamer 0.10"
