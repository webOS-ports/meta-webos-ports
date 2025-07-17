# Copyright (c) 2020-2025 LG Electronics, Inc.

require webos-open-test-apps.inc

PR = "${INC_PR}.0"

SUMMARY = "WebRTC"
AUTHOR = "Sujeet Nayak <sujeet.nayak@lge.com>"

EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
