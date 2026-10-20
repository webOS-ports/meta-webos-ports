# Where the Synergy connectors come from: webOS-ports/luneos-synergy-connectors, one repository for
# the libpurple messaging plug-ins (im/), the cloud/file connectors (cloud/) and the music
# streaming backend (music/), each with its account templates and validator apps.
#
# Every recipe built from it requires this file, so they all fetch the same SRCREV and bitbake
# shares the one git clone between them; each recipe only sets S to its own subdirectory
# (LSC_SRCDIR is the root of the checkout).
#
# The commit is pinned in luneos-synergy-connectors-srcrev.inc, which the libpurple plug-in
# recipes share.
#
# destsuffix=${BB_GIT_DEFAULT_DESTSUFFIX} is the git fetcher's own default, so it changes nothing
# for most recipes -- but it is stated explicitly because go.bbclass's go_do_unpack REWRITES the
# destsuffix when a git URL does not set one, relocating the checkout to
# ${WORKDIR}/<basename S>/src/${GO_IMPORT} for a GOPATH layout. purple-combined and the music
# backend are Go, so without this their sources land somewhere else entirely and S points at an
# empty directory tree.
#
# Licence: the repository's own code -- the cloud connectors, the music backend, the account
# templates and the validator apps -- is GPL-3.0-or-later (LICENSE at its root). The vendored
# plug-ins and libraries under im/*/plugin/ keep their own licences, which their recipes declare
# against their own licence files, so this file sets no LICENSE.

# ?= so the vendored-library recipes keep their upstream HOMEPAGE.
HOMEPAGE ?= "https://github.com/webOS-ports/luneos-synergy-connectors"

SRC_URI = "git://github.com/webOS-ports/luneos-synergy-connectors.git;branch=main;protocol=https;destsuffix=${BB_GIT_DEFAULT_DESTSUFFIX}"
require recipes-luneos/synergy/luneos-synergy-connectors-srcrev.inc
SRCREV = "${LSC_SRCREV}"

LSC_SRCDIR = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"
