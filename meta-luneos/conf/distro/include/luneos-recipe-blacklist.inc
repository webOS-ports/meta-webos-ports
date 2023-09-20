# Copyright (c) 2013 LG Electronics

# A blacklisted recipe is ignored by bitbake. The reason it is useful to be able
# to have bitbake ignore a recipe is because, although PREFERRED_PROVIDER can be
# used to select which recipe is used to fulfill a build-time dependency, bitbake
# does not have a PREFERRED_RPROVIDER to allow specification of which package
# should be used to satisfy a run-time dependency. When two recipes both provide
# common packages, but one recipe provides additional packages that the other
# doesn't, then the only way to guarantee that the first recipe will never be
# used is to blacklist it.

SKIP_RECIPE[vboxguestdrivers] = "vboxguest is also in the kernel and we don't really need vboxsf"
SKIP_RECIPE[qtwebengine] = "qtwebengine is now replaced by WebOS OSE webruntime"
SKIP_RECIPE[qtwebview] = "depends on blacklisted qtwebengine"
