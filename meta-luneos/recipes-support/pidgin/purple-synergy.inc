# Shared bits for the libpurple protocol plugins that live in luneos-synergy-connectors (im/).
#
# They are all forks carrying webOS-specific work (LS2 call services, media glue, UI quirks), so
# they are built from that repository rather than from each upstream. Where it comes from, and the
# pinned SRCREV every recipe shares, is luneos-synergy-connectors.inc; each recipe here only sets S
# to its own plugin directory.

require recipes-luneos/synergy/luneos-synergy-connectors.inc

SECTION = "webos/services"

PV = "1.0+git"

inherit pkgconfig

# webos_filesystem_paths is for webos_accttemplatesdir and webos_applicationsdir. Each plugin
# installs two things from the repository besides its .so: the account template from
# im/<protocol>/account/, which makes the protocol an addable account type, and the
# validator app from im/<protocol>/apps/, which is the UI the template's
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
