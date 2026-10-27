# Shared bits for the cloud/file Synergy connectors that live in luneos-synergy-connectors (cloud/).
#
# These are the DOCUMENTS / PHOTO.UPLOAD account types (Dropbox, Box, OneDrive, Google Drive,
# pCloud, Yandex Disk, kDrive, MEGA, Koofr, HiDrive, S3, Flickr). Each one is a node service
# (com.palm.service.<x>, a package.json run-js-service require()s), an account template
# (com.palm.<x>) and, for the ones that do not sign in through the shared OAuth page, a dedicated
# validator app. Every service requires the shared runtime as ../_cloudcore/node, so
# synergy-cloudcore has to be installed as a sibling of every connector service.
#
# The source, and the SRCREV every recipe built from that repository shares, come from
# luneos-synergy-connectors.inc. Not a `require` of purple-synergy.inc: that file is about
# libpurple plugins (pkgconfig, purple-2 .so packaging, dev-so skips) and none of it applies to a
# JS service.

require luneos-synergy-connectors.inc

SECTION = "webos/services"

# All of cloud/ is the repository's own code.
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

PV = "0.9.3+git"

S = "${LSC_SRCDIR}"

# Pure JS, JSON and images: nothing is compiled, so one package serves every machine.
inherit allarch

# webos_filesystem_paths for webos_servicesdir / webos_accttemplatesdir / webos_applicationsdir
# and the luna-service2 directories. webos_app turns each auth app's appinfo.json into its LS2
# role and client-permissions files, from the requiredPermissions each one declares.
# webos_system_bus brings webos_configure_manifest, which writes this recipe's manifest into
# manifests.d -- without one ls-hubd reads none of the files below.
#
# WEBOS_SYSTEM_BUS_SKIP_DO_TASKS: the ACG files are written in do_install, as
# org.webosports.cdav does; there is no tree of legacy pub/prv files to copy.
inherit webos_filesystem_paths
inherit webos_system_bus
inherit webos_app
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

FILES:${PN} += "${webos_servicesdir} ${webos_accttemplatesdir} ${webos_applicationsdir}"

# What every connector needs at runtime. run-js-service (mojoservicelauncher) starts each
# service as a plain node module (its package.json) on node 22, whose fetch makes the network
# calls; no Foundations, mojoservice or curl any more. The sign-in apps are Enyo 1
# (/usr/palm/frameworks/enyo/0.10 and $enyo-lib/accounts), and the credentials they produce go
# to the Accounts service in app-services.
CLOUD_RUNTIME_RDEPENDS = " \
    app-services \
    ca-certificates \
    enyo-1.0 \
    mojoservicelauncher \
    nodejs \
"
