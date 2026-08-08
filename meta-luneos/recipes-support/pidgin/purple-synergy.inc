# Shared bits for the libpurple protocol plugins that live in webos-synergy-revival.
#
# They are all forks carrying webOS-specific work (LS2 call services, media glue, UI quirks), so
# they are built from the monorepo rather than from each upstream. One clone serves them all --
# bitbake shares the git fetch across recipes with the same SRC_URI and SRCREV -- and each recipe
# only sets S to its own subdirectory.
#
# SRCREV is pinned deliberately, like purple-matrix_git.bb. Bump it when you want the new work;
# AUTOREV would make every LuneOS build depend on whatever the branch happened to be that day.

HOMEPAGE = "https://github.com/Herrie82/webos-synergy-revival"
SECTION = "webos/services"

SRC_URI = "git://github.com/Herrie82/webos-synergy-revival.git;branch=herrie/telegram-tdlib-1.8.66;protocol=https"
SRCREV = "6984a0a27f65bf49be1ad829a85a02b1494af17a"

PV = "1.0+git"

inherit pkgconfig

# Every one of these is an EionRobb-style plugin or a close relative: a plain Makefile whose
# install target reads plugindir out of pkg-config purple. Nothing needs configure.
FILES:${PN} += "${libdir}/purple-2 ${datadir}/pixmaps"

# The plugins are dlopen'd by libpurple, so they carry no SONAME and OE's default packaging
# would otherwise route the .so into ${PN}-dev and leave the runtime package empty.
FILES:${PN}-dev = "${includedir}"
INSANE_SKIP:${PN} += "dev-so"
