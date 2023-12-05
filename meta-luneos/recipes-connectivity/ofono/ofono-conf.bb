DESCRIPTION = "Machine specific configuration for ofono"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

PV = "1.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \ 
    file://environment.conf \
    file://ril_subscription.conf \
    "

SRC_URI:append:halium = "\ 
    file://binder.conf \
"

# We currently only need this for mido-halium and tissot, not pretty but it works.
SRC_URI:append:mido-halium = "\ 
    file://dual-sim.conf \
"

SRC_URI:append:tissot = "\ 
    file://dual-sim.conf \
"

do_install() {
    install -d ${D}${sysconfdir}/ofono
    install -m 0644 ${WORKDIR}/environment.conf ${D}${sysconfdir}/ofono/
    install -m 0644 ${WORKDIR}/ril_subscription.conf ${D}${sysconfdir}/ofono/
}

# For Halium we're using SFOS oFono fork, which requires binder.conf and optional dual-sim.conf configuration files for Android 9+ ports

do_install:append:halium() {
    install -m 0644 ${WORKDIR}/binder.conf ${D}${sysconfdir}/ofono/
}

# We currently only need this for mido, not pretty but it works.
do_install:append:mido-halium() {
    # dual-sim.conf only exists for dual sim devices, so we need to check for it's existence before trying to install it.
    if [ -f ${WORKDIR}/dual-sim.conf ]
    then
        install -d ${D}${sysconfdir}/ofono/binder.d/
        install -m 0644 ${WORKDIR}/dual-sim.conf ${D}${sysconfdir}/ofono/binder.d/
    fi
}

do_install:append:tissot() {
    # dual-sim.conf only exists for dual sim devices, so we need to check for it's existence before trying to install it.
    if [ -f ${WORKDIR}/dual-sim.conf ]
    then
        install -d ${D}${sysconfdir}/ofono/binder.d/
        install -m 0644 ${WORKDIR}/dual-sim.conf ${D}${sysconfdir}/ofono/binder.d/
    fi
}
