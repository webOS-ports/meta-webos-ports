# Copyright (c) 2014-2015 LG Electronics, Inc.
# Copyright (c) 2017 Herman van Hazendonk <github.com@herrie.org>
SUMMARY = "pytz brings the Olson tz database into Python"

DESCRIPTION="This library allows accurate and cross platform timezone calculations using Python 2.4 or higher"

HOMEPAGE = "https://pypi.python.org/pypi/pytz/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=39ea92752a35cf67d8a885d8e3af3c69"

SRC_URI = "https://pypi.python.org/packages/a4/09/c47e57fc9c7062b4e83b075d418800d322caa87ec0ac21e6308bd3a2d519/pytz-${PV}.zip \
"

SRC_URI[md5sum] = "f89bde8a811c8a1a5bac17eaaa94383c"
SRC_URI[sha256sum] = "f5c056e8f62d45ba8215e5cb8f50dfccb198b4b9fbea8500674f3443e4689589"

S="${WORKDIR}/pytz-${PV}"

inherit native
inherit setuptools
