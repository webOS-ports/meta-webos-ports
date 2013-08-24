FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Checksum of Source/JavaScriptCore/parser/Parser.h has changed
LIC_FILES_CHKSUM = "file://Source/WebCore/rendering/RenderApplet.h;endline=22;md5=fb9694013ad71b78f8913af7a5959680 \
                    file://Source/WebKit/gtk/webkit/webkit.h;endline=21;md5=b4fbe9f4a944f1d071dba1d2c76b3351 \
                    file://Source/JavaScriptCore/parser/Parser.h;endline=23;md5=1bd6945867ba62f5a6405bf97a7ee440"

SRC_URI = " \
    git://github.com/webOS-ports/webkit.git;protocol=git;branch=master \
    file://0001-Implement-additional-properties-for-QQuickWebView-to.patch \
    file://0002-Add-missing-.qmake.conf-which-contains-the-required-.patch \
"
# JIT compiler is causing problems currently on ARM so disable it
SRC_URI_append_arm = " file://0003-Disable-the-JIT-compiler.patch"

SRCREV = "7e1211827bf300da3197cc0cb9322b7c802f183c"
