SUMMARY = "Atlas Web browser"
DESCRIPTION = "The Atlas Web browser app (Enyo 1) running on the Chromium browser_shell. \
The same source tree also targets legacy webOS, where it drives a WPE WebKit engine over NPAPI; \
which engine it uses is decided at runtime by source/engine/AtlasHost.js, so only packaging differs."
AUTHOR = "WebOS Ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_system_bus
inherit webos_filesystem_paths
inherit webos_app

PV = "0.9.10+git"
SRCREV = "b4ed45a8f3ab05492a4769eb28f061b518bc49b8"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# The repo carries its own LS2 role/permission/manifest in sysbus/; install them below rather than
# through the class's own tasks (they need the .app.json naming the hub expects for an application).
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/sysbus"
# The app ships a finished manifest; without PASS the class also generates one of its own and two
# manifests for the same id end up in manifests.d.
WEBOS_SYSTEM_BUS_MANIFEST_TYPE = "PASS"

APP_ID = "org.webosports.app.atlas"
APP_DIR = "${webos_applicationsdir}/${APP_ID}"

# There is no build step: the app is Enyo 1, loaded from source by the framework at runtime.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Two things have to differ from the legacy webOS packaging, and both are done on a copy in ${WORKDIR}
# BEFORE do_install, so the installed files are created fresh (and owned by root) rather than edited in
# place under ${D}. Nothing here changes what a legacy ipk of the same commit produces.
#
#   appinfo type   LuneOS must launch the app through browser_shell (SAM starts WAM otherwise, and WAM
#                  has no way to embed web content: no NPAPI, and the <webview> tag is inert because it
#                  runs with --disable-extensions).
#
#   http/https     Atlas is the default browser here (VIRTUAL-RUNTIME_com.webos.app.browser), so it has
#                  to claim the web schemes. It deliberately does NOT claim them in the tree: on legacy
#                  webOS that would make it fight the stock browser for the default-browser role, which
#                  0.9.7 explicitly stopped doing. Both spellings are registered because both managers
#                  are in play — urlPattern for the legacy applicationManager that Atlas itself calls,
#                  scheme/intentFilters for SAM and the intent service.
#
#   kind ownership the kinds are owned by com.palm.app.browser, the legacy stock browser. That service
#                  does not exist here and db8 will not register a kind for an unknown owner — the
#                  configurator reports success and nothing appears — so ownership moves to the app.
#
#   db8 caller     the permissions name the app exactly, but the hub gives a browsershell app an
#                  instance-suffixed name (org.webosports.app.atlas-1) that an exact caller never
#                  matches. Wildcard callers are the norm on this platform.
ADAPTED = "${WORKDIR}/luneos-adapted"

python do_luneos_adapt() {
    import json
    import os
    import shutil

    src = d.getVar('S')
    out = d.getVar('ADAPTED')
    app_id = d.getVar('APP_ID')

    shutil.rmtree(out, ignore_errors=True)
    os.makedirs(out)

    with open(os.path.join(src, 'appinfo.json')) as f:
        info = json.load(f)
    info['type'] = 'native_browsershell'
    info['nativeLifeCycleInterfaceVersion'] = 2
    info.setdefault('handlesRelaunch', True)

    mimetypes = info.setdefault('mimeTypes', [])
    for entry in [{'urlPattern': '^https?:'}, {'scheme': 'http'}, {'scheme': 'https'}]:
        if entry not in mimetypes:
            mimetypes.append(entry)
    filters = info.setdefault('intentFilters', [])
    view_web = {'actions': ['view'], 'uris': ['http://', 'https://']}
    if view_web not in filters:
        filters.append(view_web)
    bb.note('appinfo: registered http/https as the default browser')
    with open(os.path.join(out, 'appinfo.json'), 'w') as f:
        json.dump(info, f, indent=2)
    bb.note('appinfo type -> native_browsershell')

    shutil.copytree(os.path.join(src, 'db'), os.path.join(out, 'db'))

    kinds = os.path.join(out, 'db', 'kinds')
    for name in sorted(os.listdir(kinds)):
        path = os.path.join(kinds, name)
        with open(path) as f:
            kind = json.load(f)
        if kind.get('owner') == 'com.palm.app.browser':
            kind['owner'] = app_id
            with open(path, 'w') as f:
                json.dump(kind, f, indent=4)
            bb.note('kind %s owner -> %s' % (name, app_id))

    perms = os.path.join(out, 'db', 'permissions')
    for name in sorted(os.listdir(perms)):
        path = os.path.join(perms, name)
        with open(path) as f:
            entries = json.load(f)
        changed = False
        for entry in entries:
            if entry.get('caller') == app_id:
                entry['caller'] = app_id + '*'
                changed = True
        if changed:
            with open(path, 'w') as f:
                json.dump(entries, f, indent=4)
            bb.note('permission %s caller -> %s*' % (name, app_id))
}
addtask luneos_adapt after do_patch before do_install

do_install() {
    install -d ${D}${APP_DIR}
    # Ship what the app loads, not the repo furniture: packaging/ is the legacy-webOS ipk tooling,
    # test/ is a test harness, and the .psd files are icon sources. Plain cp (no -a): preserving the
    # source's ownership would carry the build user's uid into the package.
    for item in index.html depends.js icon.png icon-48x48.png icon-64x64.png icon-256x256.png \
                icon-1024x1024.png css images source db; do
        if [ -e ${S}/$item ]; then
            cp -rf ${S}/$item ${D}${APP_DIR}/
        fi
    done
    install -v -m 644 ${ADAPTED}/appinfo.json ${D}${APP_DIR}/appinfo.json

    # LS2 access. Without these every db8, download-manager and settings call the app makes is refused
    # by the hub. Note the .app.json naming the hub expects for an application role.
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_manifestsdir}
    install -v -m 644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${APP_ID}.role.json \
        ${D}${webos_sysbus_rolesdir}/${APP_ID}.app.json
    install -v -m 644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${APP_ID}.perm.json \
        ${D}${webos_sysbus_permissionsdir}/${APP_ID}.app.json
    install -v -m 644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${APP_ID}.manifest.json \
        ${D}${webos_sysbus_manifestsdir}/${APP_ID}.json

    # db8 kinds for bookmarks, history, preferences, saved logins and autofill. LuneOS has never had the
    # com.palm.browser* kinds — on a real webOS device they belong to the stock browser — so without
    # these the app starts fine and then silently fails to store anything. The configurator installs
    # them from here at boot, the same way core-apps ships its kinds.
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${ADAPTED}/db/kinds/* ${D}${webos_sysconfdir}/db/kinds
    install -v -m 644 ${ADAPTED}/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

FILES:${PN} += " \
    ${webos_applicationsdir} \
    ${webos_sysconfdir}/db \
"
