# Copyright (c) 2015-2023 LG Electronics, Inc.

EXTENDPRAUTO:append = "webos5"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PACKAGECONFIG[pmlog] = "-Dpmlog=true,-Dpmlog=false,pmloglib"
PACKAGECONFIG:append:class-target = " pmlog"

SRC_URI += " \
    file://0001-server-Fix-crash-when-accessing-client-which-is-alre.patch \
    file://0002-client-Exit-on-a-fatal-connection-error.patch \
    file://0003-Enhance-Wayland-debug-print.patch \
"

# http://gecko.lge.com:8000/Errors/Details/819466
# wayland-1.22.0/src/wayland-util.c:456:64: error: implicit declaration of function 'getpid' [-Wimplicit-function-declaration]
# wayland-1.22.0/src/wayland-util.c:456:74: error: implicit declaration of function 'pthread_self' [-Wimplicit-function-declaration]
# caused by 0003-Enhance-Wayland-debug-print.patch
CFLAGS += "-Wno-error=implicit-function-declaration"
