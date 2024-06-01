SUMMARY = "Tim Bird's experimental crash_handler program."
HOMEPAGE = "http://elinux.org/Crash_handler"
LICENSE = "Apache-2.0 & GPL-2.0-or-later & PD"
LIC_FILES_CHKSUM = " \
    file://LICENSE.txt;md5=58abc812381b4475c81a522215ed6cde \
    file://LICENSE-APACHE-2.0.txt;md5=9645f39e9db895a4aa6e02cb57294595 \
    file://table-pr-support.c;beginline=1;endline=27;md5=f06e67ccca54abf6c1c640cd2ebc401b \
    file://table-unwind-arm.c;beginline=1;endline=27;md5=8ab295976fef914ca04d5d0843f0849a \
    file://mcternan-unwinder/LICENCE;md5=1b6c91f7832f201dbf0ce618a39abdd0"

PV = "0.6+git"
SRCREV = "3633c18f924c3fc501b5f090ec9a760890002295"

inherit webos_ports_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

CLEANBROKEN = "1"

do_compile() {
    oe_runmake CC="${CC} ${LDFLAGS} ${CFLAGS}"
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/crash_handler ${D}${sbindir}
}

# table-unwind-arm.c:488:55: error: implicit declaration of function 'strerror' [-Wimplicit-function-declaration]
# journal.c:62:19: error: implicit declaration of function 'atoi' [-Wimplicit-function-declaration]
# journal.c:84:17: error: implicit declaration of function 'strptime'; did you mean 'strftime'? [-Wimplicit-function-declaration]
# journal.c:120:17: error: implicit declaration of function 'read'; did you mean 'fread'? [-Wimplicit-function-declaration]
# journal.c:188:17: error: implicit declaration of function 'write'; did you mean 'fwrite'? [-Wimplicit-function-declaration]
# journal.c:207:16: error: implicit declaration of function 'close'; did you mean 'pclose'? [-Wimplicit-function-declaration]
# crash_handler.c:611:16: error: implicit declaration of function 'klogctl' [-Wimplicit-function-declaration]
CFLAGS += "-Wno-error=implicit-function-declaration"

# guess-unwinder.c:95:49: error: 'return' with no value, in function returning non-void [-Wreturn-mismatch]
# crash_handler.c:419:9: error: 'return' with no value, in function returning non-void [-Wreturn-mismatch]
CFLAGS += "-Wno-error=return-mismatch"
