SUMMARY = "Service configurations run Open webOS daemons with systemd"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+gitr${SRCPV}"

SRC_URI = "git://github.com/webOS-ports/webos-systemd-services.git;branch=master;protocol=git"
SRCREV = "2ff87680a7ddd70aff5eb9952ce85bdd4d000ea9"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " \
    activitymanager.service \
    configurator.service \
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
    webos-telephonyd.service \
    webos-base.target \
    webos-essential.target \
"

# disabled cause of https://github.com/openwebos/mojoservicelauncher/commit/7780a480155e018d1c42b8599b962aaea871720d
# node_fork_server.service

do_install() {
    install -d ${D}${systemd_unitdir}/system

    for f in *.target *.service ; do
        install -m 0644 ${S}/$f ${D}${systemd_unitdir}/system
    done
}
