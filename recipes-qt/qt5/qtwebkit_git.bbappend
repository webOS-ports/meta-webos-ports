FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Checksum of Source/JavaScriptCore/parser/Parser.h has changed
LIC_FILES_CHKSUM = "file://Source/WebCore/rendering/RenderApplet.h;endline=22;md5=fb9694013ad71b78f8913af7a5959680 \
                    file://Source/WebKit/gtk/webkit/webkit.h;endline=21;md5=b4fbe9f4a944f1d071dba1d2c76b3351 \
                    file://Source/JavaScriptCore/parser/Parser.h;endline=23;md5=1bd6945867ba62f5a6405bf97a7ee440"

DEPENDS += "luna-service2"

SRC_URI = "git://github.com/webOS-ports/qtwebkit;branch=webOS-ports/master;protocol=git"
SRCREV = "185683d256e6fe5b0af1a4efa3724959965e4be9"
