FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# For mainline kernel on tenderloin, surface-manager needs to wait for DRM device
# due to deferred probe of the display driver
SRC_URI:append:tenderloin = " \
    file://surface-manager-wait-for-drm.conf \
"

do_install:append:tenderloin() {
    install -d ${D}${sysconfdir}/systemd/system/surface-manager.service.d
    install -m 0644 ${WORKDIR}/surface-manager-wait-for-drm.conf ${D}${sysconfdir}/systemd/system/surface-manager.service.d/wait-for-drm.conf
}

FILES:${PN}:append:tenderloin = " ${sysconfdir}/systemd/system/surface-manager.service.d"
