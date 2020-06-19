LICENSE = "GPL-2.0+ | LGPL-3.0 | The-Qt-Company-Commercial"
LIC_FILES_CHKSUM = " \
    file://vboxtouch.cpp;beginline=1;endline=22;md5=ca51db8f7c0606c77f702dcee4cf31d9 \
    file://evdevmousehandler.cpp;beginline=1;endline=38;md5=e6b661a57e804d0e9c4065e9ea275f33 \
"

inherit webos_ports_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "5c3b346e2c72900158f207f86e62f1f2672f494a"
