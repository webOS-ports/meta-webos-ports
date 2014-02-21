SUMMARY = "Service configurations run Open webOS daemons with systemd"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+gitr${SRCPV}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "git://github.com/webOS-ports/webos-systemd-services.git;branch=master;protocol=git"
SRCREV = "148ab4ce36794321db3f38d462ee6c1166caf4c0"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " \
    activitymanager.service \
    configurator.service \
    configurator-async.service \
    db8.service \
    filecache.service \
    ls-hubd_private.service \
    ls-hubd_public.service \
    LunaSysService.service \
    LunaSysMgr.service \
    org.webosinternals.ipkgservice.service \
    PmKLogDaemon.service \
    PmLogDaemon.service \
    powerd.service \
    sleepd.service \
    webos-connman-adapter.service \
    ${@base_contains('MACHINE_FEATURES', 'phone', 'webos-telephonyd.service', '',d)} \
"

do_install() {
    install -d ${D}${systemd_unitdir}/system

    for f in *.service ; do
        install -m 0644 ${S}/$f ${D}${systemd_unitdir}/system
    done

    install -d ${D}${bindir}
    install -m 0755 ${S}/db8-prestart.sh ${D}${bindir}
}
