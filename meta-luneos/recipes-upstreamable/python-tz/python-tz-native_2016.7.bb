# Copyright (c) 2014-2015 LG Electronics, Inc.

SUMMARY = "pytz brings the Olson tz database into Python"

DESCRIPTION="This library allows accurate and cross platform timezone calculations using Python 2.4 or higher"

HOMEPAGE = "https://pypi.python.org/pypi/pytz/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=22b38951eb857cf285a4560a914b7cd6"

SRC_URI = "https://pypi.python.org/packages/62/de/3ec428a9a656c4568f8a01b93bda4aff43c3fadfa50356048a62de9ee3b7/pytz-${PV}.tar.gz \
"
#    file://fix.for.tzdata-2015e.patch 

SRC_URI[md5sum] = "8660ba7c3c0abd23e6e4efa493b02966"
SRC_URI[sha256sum] = "8787de03f35f31699bcaf127e56ad14c00647965ed24d72dbaca87c6e4f843a3"

S="${WORKDIR}/pytz-${PV}"

inherit native
inherit setuptools
