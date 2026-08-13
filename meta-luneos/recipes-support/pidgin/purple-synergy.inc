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

# destsuffix=${BB_GIT_DEFAULT_DESTSUFFIX} is the git fetcher's own default, so it changes nothing for most of these -- but
# it is stated explicitly because go.bbclass's go_do_unpack REWRITES the destsuffix when a git URL
# does not set one, relocating the checkout to ${WORKDIR}/<basename S>/src/${GO_IMPORT} for a
# GOPATH layout. purple-combined inherits go-mod, so without this its sources land somewhere else
# entirely and S points at an empty directory tree.
SRC_URI = "git://github.com/Herrie82/webos-synergy-revival.git;branch=herrie/telegram-tdlib-1.8.66;protocol=https;destsuffix=${BB_GIT_DEFAULT_DESTSUFFIX}"
SRCREV = "23e8193c5ff9c83a7a9f275af2947d868d3d2ce2"

PV = "1.0+git"

inherit pkgconfig

# webos_filesystem_paths is for webos_accttemplatesdir and webos_applicationsdir. Each plugin
# installs two things from the monorepo besides its .so: the account template from
# messaging/<protocol>/account/, which makes the protocol an addable account type, and the
# validator app from messaging/<protocol>/apps/, which is the UI the template's
# validator.customUI.appId names. Both belong with the plugin that implements the protocol rather
# than in messaging-accounts, which carries the legacy org.webosports.messaging.* set from a
# different repository and only ships the shared org.webosports.app.imvalidator.
#
# webos_app is what turns those appinfo.json files into LS2 role and permission files at build
# time. The validator apps declare requiredPermissions (accountsservice.*, networkconnection.query)
# and would otherwise be installed with no bus access at all.
inherit webos_filesystem_paths
inherit webos_app

# Every one of these is an EionRobb-style plugin or a close relative: a plain Makefile, nothing
# needing configure. Each recipe installs its .so by hand rather than running `make install`,
# because that target also installs Pidgin protocol pixmaps into ${datadir}/pixmaps/pidgin --
# a directory only Pidgin's GTK UI reads, and there is no Pidgin here. libpurple is loaded
# headless by imlibpurpletransport, and webOS takes account icons from the account template.
# So no pixmaps path is packaged: nothing installs one.
FILES:${PN} += "${libdir}/purple-2 ${webos_accttemplatesdir} ${webos_applicationsdir}"

# The plugins are dlopen'd by libpurple, so they carry no SONAME and OE's default packaging
# would otherwise route the .so into ${PN}-dev and leave the runtime package empty.
FILES:${PN}-dev = "${includedir}"
INSANE_SKIP:${PN} += "dev-so"
