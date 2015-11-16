SUMMARY = "Service configurations run Open webOS daemons with systemd"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+gitr${SRCPV}"
SRCREV = "4b53dfa0cbcd63d7bc6c08a42a8fd456e34dc97e"

inherit webos_ports_repo

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " \
    activitymanager.service \
    configurator.service \
    configurator-async.service \
    filecache.service \
    ls-hubd_private.service \
    ls-hubd_public.service \
    LunaSysService.service \
    LunaUniversalSearchMgr.service \
    LunaSysMgr.service \
    LunaAppManager.service \
    org.webosinternals.ipkgservice.service \
    powerd.service \
    sleepd.service \
    webos-connman-adapter.service \
"

do_install() {
    install -d ${D}${systemd_unitdir}/system

    for f in *.service ; do
        install -m 0644 ${S}/$f ${D}${systemd_unitdir}/system
    done

    install -d ${D}${bindir}
    install -m 0755 ${S}/db8-prestart.sh ${D}${bindir}
}

# to catch .service files not listed in SYSTEMD_SERVICE (db8@.service and in some cases webos-telephonyd.service)
FILES_${PN} += "${systemd_unitdir}/system"
