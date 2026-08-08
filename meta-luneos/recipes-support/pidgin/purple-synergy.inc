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

# destsuffix=git is the git fetcher's own default, so it changes nothing for most of these -- but
# it is stated explicitly because go.bbclass's go_do_unpack REWRITES the destsuffix when a git URL
# does not set one, relocating the checkout to ${WORKDIR}/<basename S>/src/${GO_IMPORT} for a
# GOPATH layout. purple-combined inherits go-mod, so without this its sources land somewhere else
# entirely and S points at an empty directory tree.
SRC_URI = "git://github.com/Herrie82/webos-synergy-revival.git;branch=herrie/telegram-tdlib-1.8.66;protocol=https;destsuffix=git"
SRCREV = "6984a0a27f65bf49be1ad829a85a02b1494af17a"

PV = "1.0+git"

inherit pkgconfig

# Every one of these is an EionRobb-style plugin or a close relative: a plain Makefile, nothing
# needing configure. Each recipe installs its .so by hand rather than running `make install`,
# because that target also installs Pidgin protocol pixmaps into ${datadir}/pixmaps/pidgin --
# a directory only Pidgin's GTK UI reads, and there is no Pidgin here. libpurple is loaded
# headless by imlibpurpletransport, and webOS takes account icons from the account template.
# So no pixmaps path is packaged: nothing installs one.
FILES:${PN} += "${libdir}/purple-2"

# The plugins are dlopen'd by libpurple, so they carry no SONAME and OE's default packaging
# would otherwise route the .so into ${PN}-dev and leave the runtime package empty.
FILES:${PN}-dev = "${includedir}"
INSANE_SKIP:${PN} += "dev-so"
