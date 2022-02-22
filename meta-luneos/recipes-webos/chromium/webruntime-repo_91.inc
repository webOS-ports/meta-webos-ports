# Copyright (c) 2021 LG Electronics, Inc.

LIC_FILES_CHKSUM += "file://oss-pkg-info.yaml;md5=c31f1ae166739be9ca186deacf4b32e4"

WEBOS_REPO_NAME = "chromium91"
WEBOS_VERSION = "91.0.4472.114-3_f64b405ebd97e0c382ccf1fc40fc4c3a96e937e7"

SRCREV_main = "7deca0fa8aa01ec414cf689448b76b4f828a100e"

WEBOS_REPO_NAME_V8 = "chromium-v8"
WEBOS_GIT_PARAM_BRANCH_V8 = "@chromium91"
WEBOS_VERSION_V8 = "8.7.220.29-chromium91.1_89712e7f20774931327815ca5dfea0723ab39e82"
SRCREV_v8 = "57d88e7607e88c7bbb942d993de261ed7cd6c5a4"

SRC_URI += " \
    file://0001-op-ups-build-abseil-cpp-fix-build-with-glibc-2.34.patch \
    file://0002-op-upsable-build-breakpad-fix-build-with-glibc-2.34.patch \
"
