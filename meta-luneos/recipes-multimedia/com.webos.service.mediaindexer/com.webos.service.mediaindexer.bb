# Copyright (c) 2020-2021 LG Electronics, Inc.

SUMMARY = "Media indexer service"
AUTHOR = "Jaehoon Lee <jaehoon85.lee@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.0-16+git${SRCPV}"
SRCREV = "09b2b7d3b8d9feb9dcd8f654eb4cbbb42b2410d4"

inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_public_repo

# third party libraries
DEPENDS = "glib-2.0 libmtp gupnp libedit gstreamer1.0 gstreamer1.0-plugins-base taglib libpng libjpeg-turbo gdk-pixbuf jpeg libexif giflib"
# webos dependencies
DEPENDS += "luna-service2 pmloglib libpbnjson"
# webos runtime dependencies
VIRTUAL-RUNTIME_pdm ?= "com.webos.service.pdm"
RDEPENDS:${PN} = "${VIRTUAL-RUNTIME_pdm} db8"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Fix-app-permissions.patch \
    file://0001-Fix-build-with-gcc-13.patch \
"
S = "${WORKDIR}/git"

# uncomment next line to use mediaindexer in shell/interactive mode
#EXTRA_OECMAKE += " -DSTANDALONE=1"

# configure the maximum number of parallel meta data extractions
#EXTRA_OECMAKE += " -DPARALLEL_META_EXTRACTION=10"

# configure the folders on local storage which shall be scanned from
# storage plugin, the format is <path>,<name>,<description>;...
#EXTRA_OECMAKE += " -DSTORAGE_DEVS:string='/media/local,Media,Local Media Storage'"

# media indexer client library
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/*.so"

