# Copyright (c) 2018-2025 LG Electronics, Inc.

require webos-open-test-apps.inc

SUMMARY = "webOS OSE test application"
AUTHOR = "VINH VAN LE <vinh5.le@lge.com>"
LIC_FILES_CHKSUM += " \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

PR = "${INC_PR}.5"

SRC_URI += " \
    file://0001-com.webos.app.test.webosose-use-the-media.operation-.patch \
"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
