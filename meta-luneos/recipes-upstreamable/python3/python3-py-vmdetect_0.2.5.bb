DESCRIPTION = "Python virtual machine detection tool detects virtual enviroment - VMWare, XEN, FreeBSD jail eg"
HOMEPAGE = "https://github.com/kepsic/py_vmdetect"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=624fbcb17f69e175d29441a505373bb5"

SRC_URI[sha256sum] = "e97d247840d5504814efb893f3f271cfa51b00677dc093426a0ec8d31f5f917e"

SRC_URI_append = " \
    file://0001-Remove-including-sys-sysctl.h-on-glibc-based-systems.patch \
    "

PYPI_PACKAGE = "py_vmdetect"

RDEPENDS:${PN} = "python3-pycparser python3-click python3-setuptools python3-cffi"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"
