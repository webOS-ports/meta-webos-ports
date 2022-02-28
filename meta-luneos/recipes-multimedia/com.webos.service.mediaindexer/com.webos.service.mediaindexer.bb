# Copyright (c) 2020-2021 LG Electronics, Inc.

SUMMARY = "Media indexer service"
AUTHOR = "Jaehoon Lee <jaehoon85.lee@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.0-16+git${SRCPV}"
SRCREV = "5d93468757bb75e9d8b29599cda0decc124cedb8"

inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_ports_ose_repo

# third party libraries
DEPENDS = "glib-2.0 libmtp gupnp libedit gstreamer1.0 gstreamer1.0-plugins-base taglib libpng libjpeg-turbo gdk-pixbuf jpeg libexif giflib"
# webos dependencies
DEPENDS += "luna-service2 pmloglib libpbnjson"
# webos runtime dependencies
VIRTUAL-RUNTIME_pdm ?= "com.webos.service.pdm"
RDEPENDS:${PN} = "${VIRTUAL-RUNTIME_pdm} db8"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
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

