# Copyright (c) 2014-2015 LG Electronics, Inc.
# Copyright (c) 2017-2019 Herman van Hazendonk <github.com@herrie.org>
SUMMARY = "pytz brings the Olson tz database into Python"

DESCRIPTION="This library allows accurate and cross platform timezone calculations using Python 2.4 or higher"

HOMEPAGE = "https://pypi.python.org/pypi/pytz/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4878a915709225bceab739bdc2a18e8d"

SRC_URI = "https://files.pythonhosted.org/packages/df/d5/3e3ff673e8f3096921b3f1b79ce04b832e0100b4741573154b72b756a681/pytz-${PV}.tar.gz \
"

SRC_URI[md5sum] = "8b2860a161bfb0a6165798b1a2d8c40c"
SRC_URI[sha256sum] = "d747dd3d23d77ef44c6a3526e274af6efeb0a6f1afd5a69ba4d5be4098c8e141"

S="${WORKDIR}/pytz-${PV}"

inherit native
inherit setuptools
