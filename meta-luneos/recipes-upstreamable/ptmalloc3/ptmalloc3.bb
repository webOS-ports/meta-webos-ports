# Copyright (c) 2012-2022 LG Electronics, Inc.

SECTION = "libs"
DESCRIPTION = "Multi-thread malloc implementation"
HOMEPAGE = "http://www.malloc.de/en/"
LICENSE = "LGPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=5c8ad593874e48b27ded5334b58f1e0c \
    file://README;md5=f97028d3ccde7cc91d7277d5e715173d \
"

PR = "r4"

SRC_URI = "http://www.malloc.de/malloc/ptmalloc3-current.tar.gz \
           file://ptmalloc3-current-webos.patch "

SRC_URI[md5sum] = "c0b9dd5f16f8eae979166dc74b60015c"
SRC_URI[sha256sum] = "f353606f24a579597a1ff5b51009a45d75da047b3975d82c3f613f85bcf312db"

S = "${UNPACKDIR}/${BPN}"

do_compile () {
    make -f Makefile.palm CC="$CC"
}

do_install() {
    #oenote instaling ptmalloc3
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 555 -p ${S}/libptmalloc3.so    ${D}${libdir}
    install -m 666 ${S}/mmap_dev_heap.h ${D}/${includedir}
    install -m 666 ${S}/malloc-2.8.3.h  ${D}/${includedir}
    install -m 666 ${S}/malloc_utils.h  ${D}/${includedir}
    cp -R --no-dereference --preserve=mode,links -v ${S}/sysdeps         ${D}/${includedir}
    rm ${D}/${includedir}/sysdeps/generic/atomic.h
}

FILES:${PN} = " ${libdir}/lib*.so"
FILES:${PN}-dev = "${includedir}"
