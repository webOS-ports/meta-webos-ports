# Shared bits for the libpurple protocol plugins built from their own upstream.
#
# Each recipe fetches its plugin from upstream, pinned to a SRCREV, and carries LuneOS changes as
# patches next to it. The webOS side of each protocol -- the account template and the validator
# app -- is not part of any upstream and comes from luneos-synergy-connectors, fetched here as a
# second git source into ${CONNECTORS_DIR}. bitbake shares that clone across all the recipes that
# use the same URL and SRCREV.
#
# A recipe adds its upstream to SRC_URI with name=upstream and destsuffix=<plugin>, sets
# SRCREV_upstream, and points S at ${UNPACKDIR}/<plugin>.
#
# Each patch series is split in two, so it is clear at a glance what can go upstream:
#
#   generic   plain subject, Upstream-Status Pending (or Submitted/Backport once sent), and
#             numbered first. Bug fixes and features any libpurple UI could use; these are the
#             ones to send upstream, one pull request at a time.
#   webOS     subject starting "webOS:", Upstream-Status Inappropriate [reason], numbered after
#             the generic ones. They adapt a plug-in to imlibpurpletransport, the webOS apps or
#             LuneOS services, and are not meant for upstream as they are.
#
# A new patch goes in the half it belongs to. When a generic patch is sent or merged, update its
# Upstream-Status; when it is merged, drop it with the SRCREV bump that brings it in.
#
# SRCREVs are pinned deliberately, like purple-matrix_git.bb. Bump them when you want the new
# work; AUTOREV would make every LuneOS build depend on whatever the branch happened to be that day.
# The connectors commit is the one luneos-synergy-connectors-srcrev.inc pins for the cloud and
# music recipes too.

HOMEPAGE = "https://github.com/webOS-ports/luneos-synergy-connectors"
SECTION = "webos/services"

SRC_URI = "git://github.com/webOS-ports/luneos-synergy-connectors.git;branch=main;protocol=https;name=connectors;destsuffix=connectors"
require recipes-luneos/synergy/luneos-synergy-connectors-srcrev.inc
SRCREV_connectors = "${LSC_SRCREV}"
SRCREV_FORMAT = "upstream_connectors"

CONNECTORS_DIR = "${UNPACKDIR}/connectors"

PV = "1.0+git"

inherit pkgconfig

# webos_filesystem_paths is for webos_accttemplatesdir and webos_applicationsdir. Each plugin
# installs two things from luneos-synergy-connectors besides its .so: the account template from
# im/<protocol>/account/, which makes the protocol an addable account type, and the validator app
# from im/<protocol>/apps/, which is the UI the template's validator.customUI.appId names.
#
# webos_app is what turns those appinfo.json files into LS2 role and permission files at build
# time. The validator apps declare requiredPermissions (accountsservice.*, networkconnection.query)
# and would otherwise be installed with no bus access at all.
inherit webos_filesystem_paths
inherit webos_app

# Each recipe installs its .so by hand rather than running `make install`, because that target
# also installs Pidgin protocol pixmaps into ${datadir}/pixmaps/pidgin -- a directory only Pidgin's
# GTK UI reads, and there is no Pidgin here. libpurple is loaded headless by imlibpurpletransport,
# and webOS takes account icons from the account template.
FILES:${PN} += "${libdir}/purple-2 ${webos_accttemplatesdir} ${webos_applicationsdir}"

# The plugins are dlopen'd by libpurple, so they carry no SONAME and OE's default packaging
# would otherwise route the .so into ${PN}-dev and leave the runtime package empty.
FILES:${PN}-dev = "${includedir}"
INSANE_SKIP:${PN} += "dev-so"
