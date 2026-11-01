FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# start-android-hals.sh is ours, on top of the recipe's PR.
PR:append = ".1"

SRC_URI += " \
    file://change-before-dependencies.conf \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/android-system.service.d
    install -m 0644 ${UNPACKDIR}/change-before-dependencies.conf \
        ${D}${systemd_unitdir}/system/android-system.service.d
}

FILES:${PN} += "${systemd_unitdir}/system/android-system.service.d"
