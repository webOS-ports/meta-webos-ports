SUMMARY = "Configuration file parser library"
DESCRIPTION = "libdotconf is a simple, powerful configuration file parser. It is only here \
because speech-dispatcher's server requires it (PKG_CHECK_MODULES([DOTCONF], [dotconf >= 1.3])) \
and no other layer in this build provides it."
HOMEPAGE = "https://github.com/williamh/dotconf"
SECTION = "libs"

LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=155b66f2dc258f05886f9886a60fd870"

DEPENDS = "glib-2.0"

# Upstream publishes tags but no release tarballs. Fetch over git rather than the
# GitHub tag archive: those archives are generated on demand and GitHub has changed
# their byte content before now, which breaks the recorded sha256. That is what
# do_recipe_qa's src-uri-bad check rejects.
#
# branch=1.4.x, not master: v1.4.1 lives on the release branch and master has since
# diverged (2 ahead, 4 behind), so the revision is not an ancestor of master and
# bitbake's "revision in branch" check would reject it.
SRC_URI = "git://github.com/williamh/dotconf.git;protocol=https;branch=1.4.x"
SRCREV = "7c3e0515c29efce45de6aae29befb2ab4396bd60"

S = "${WORKDIR}/git"

# Ships configure.ac but no configure, so autotools has to regenerate.
inherit autotools pkgconfig

BBCLASSEXTEND = "native nativesdk"
