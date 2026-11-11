FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://change-before-dependencies.conf \
    file://stubbed-services-tangorpro \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/android-system.service.d
    install -m 0644 ${UNPACKDIR}/change-before-dependencies.conf \
        ${D}${systemd_unitdir}/system/android-system.service.d

    # The shared Halium rootfs is selected at runtime by 50-stub-services.
    # Keep this in a per-codename list: other devices need their storage proxy.
    install -d ${D}${localstatedir}/lib/lxc/android/stubbed-services.d
    install -m 0644 ${UNPACKDIR}/stubbed-services-tangorpro \
        ${D}${localstatedir}/lib/lxc/android/stubbed-services.d/tangorpro
}

FILES:${PN} += "${systemd_unitdir}/system/android-system.service.d"
