require pulseaudio.inc

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/${BP}.tar.xz \
           file://volatiles.04_pulse \
"
SRC_URI[md5sum] = "b691e83b7434c678dffacfa3a027750e"
SRC_URI[sha256sum] = "b50640e0b80b1607600accfad2e45aabb79d379bf6354c9671efa2065477f6f6"

do_compile_prepend() {
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}
