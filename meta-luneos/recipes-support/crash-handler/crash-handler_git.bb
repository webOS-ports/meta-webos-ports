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

do_compile() {
    oe_runmake CC="${CC} ${LDFLAGS}"
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/crash_handler ${D}${sbindir}
}
