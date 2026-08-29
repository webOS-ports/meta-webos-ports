SUMMARY = "Small utility probing the current key state on an input device"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "0.0.1+git"
SRCREV = "6dbd96bc949f02220fc418bcd5a1b2993b8d1e11"

# inherit webos_ports_repo
inherit cmake

# Upstream still declares cmake_minimum_required(VERSION 2.8), which CMake 4 in
# wrynose refuses. webos_cmake.bbclass sets this centrally for the recipes that
# inherit it, but this one inherits plain cmake so it needs its own copy.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"

SRC_URI = "git://github.com/Tofee/key-state.git;protocol=https;branch=main"
