FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Checksum of Source/JavaScriptCore/parser/Parser.h has changed
LIC_FILES_CHKSUM = "file://Source/WebCore/rendering/RenderApplet.h;endline=22;md5=fb9694013ad71b78f8913af7a5959680 \
                    file://Source/WebKit/gtk/webkit/webkit.h;endline=21;md5=b4fbe9f4a944f1d071dba1d2c76b3351 \
                    file://Source/JavaScriptCore/parser/Parser.h;endline=23;md5=1bd6945867ba62f5a6405bf97a7ee440"

SRC_URI += " \
    file://0001-Implement-additional-properties-for-QQuickWebView-to.patch \
    file://0002-Add-PalmServiceBridge.patch \
    file://0004-Implement-handling-of-window-management-related-call.patch \
"

SRC_URI_append_arm = " file://0003-Disable-low-level-interpreter.patch"

SRCREV = "1e4dd5844be47323a4869f4b51bb583a09d1a351"
