SUMMARY = "Video servers for Videos: Jellyfin, Emby, Plex, PeerTube, UPnP/DLNA media servers and M3U playlists"
DESCRIPTION = "org.webosports.service.videos, the node service that gives Videos the video \
servers of the accounts (capability Video, subtype STREAMING): Jellyfin, Emby, PeerTube instances and \
the UPnP/DLNA media servers on the network. With its account templates and the shared sign-in \
app org.webosports.app.videoauth."

# The repository's video/ family: one service with a provider module per connector
# (video/<name>/provider -> providers/<name>.js), the templates (video/<name>/account) and the
# sign-in app. One recipe for all of them, as they are one service: one manifest, one bus name.
require cloud-synergy.inc

# The Accounts service only sees a new or removed template after a restart; see there.
require synergy-accounts-reload.inc

VIDEO_SERVICE = "org.webosports.service.videos"
# emby is Jellyfin's provider with Emby's differences: providers/emby.js requires providers/jellyfin.js.
VIDEO_PROVIDERS = "emby jellyfin m3u peertube plex upnp"

do_install() {
    src=${S}/video

    # The service. Its core.js requires the shared runtime as ../_cloudcore/node, which
    # synergy-cloudcore installs into the same directory.
    install -d ${D}${webos_servicesdir}/${VIDEO_SERVICE}/providers
    cp -R --no-preserve=ownership $src/_core/service/${VIDEO_SERVICE}/. ${D}${webos_servicesdir}/${VIDEO_SERVICE}/
    for p in ${VIDEO_PROVIDERS}; do
        install -m 0644 $src/$p/provider/index.js ${D}${webos_servicesdir}/${VIDEO_SERVICE}/providers/$p.js
        t=org.webosports.video.$p
        install -d ${D}${webos_accttemplatesdir}/$t
        cp -R --no-preserve=ownership $src/$p/account/$t/. ${D}${webos_accttemplatesdir}/$t/
        grep -q '"appId"[[:space:]]*:[[:space:]]*"org.webosports.app.videoauth"' ${D}${webos_accttemplatesdir}/$t/$t.json || \
            bbfatal "$t.json does not name org.webosports.app.videoauth as its validator app"
    done

    install -d ${D}${webos_applicationsdir}
    cp -R --no-preserve=ownership $src/_core/apps/org.webosports.app.videoauth ${D}${webos_applicationsdir}/

    # ACG, from the family's own files.
    sysbus=$src/_core/files/sysbus
    install -d ${D}${webos_sysbus_servicedir} ${D}${webos_sysbus_rolesdir} \
               ${D}${webos_sysbus_apipermissionsdir} ${D}${webos_sysbus_groupsdir} \
               ${D}${webos_sysbus_permissionsdir}
    install -m 0644 $sysbus/${VIDEO_SERVICE}.service     ${D}${webos_sysbus_servicedir}/
    install -m 0644 $sysbus/${VIDEO_SERVICE}.role.json   ${D}${webos_sysbus_rolesdir}/
    install -m 0644 $sysbus/${VIDEO_SERVICE}.api.json    ${D}${webos_sysbus_apipermissionsdir}/
    install -m 0644 $sysbus/${VIDEO_SERVICE}.groups.json ${D}${webos_sysbus_groupsdir}/
    install -m 0644 $sysbus/${VIDEO_SERVICE}.perm.json   ${D}${webos_sysbus_permissionsdir}/
}

RDEPENDS:${PN} = "synergy-cloudcore"
