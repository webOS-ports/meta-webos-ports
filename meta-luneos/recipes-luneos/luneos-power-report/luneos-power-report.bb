SUMMARY = "One-shot power audit for LuneOS devices"
DESCRIPTION = "Snapshots the kernel's power accounting twice across a \
measurement window and prints ranked deltas plus red flags: suspend health, \
cpuidle residency, battery drain, top interrupt and CPU consumers, wakeup \
sources, journal floods and peripheral holds. The standard first step of any \
battery complaint: run it with the screen off, attach the output."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://luneos-power-report"

# ps -eo comes from procps; everything else the script needs is busybox or
# optional (journalctl, pactl, iw are probed and skipped when absent).
RDEPENDS:${PN} = "procps"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/luneos-power-report ${D}${bindir}
}
