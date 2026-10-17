SUMMARY = "Documents: reads Word, OpenDocument, Excel, PowerPoint, CSV, text and Markdown files"
SECTION = "webos/apps"
# The app is GPL-3.0-only. It bundles docx-preview and SheetJS (Apache-2.0)
# and JSZip (MIT, of its MIT/GPLv3 choice); see the app's NOTICE.
LICENSE = "GPL-3.0-only & Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=db37b6ead2dec60580595736ff983452 \
                    file://lib/docx-preview/LICENSE;md5=f20203bc1a46617a965cb84980b55e15 \
                    file://lib/sheetjs/LICENSE;md5=7c69b1572be9cb62ed3c29722d422430 \
                    file://lib/jszip/LICENSE.markdown;md5=28c4d6943c892a3c61ac2aaa5d781861 \
"

PV = "0.1.0+git"
SRCREV = "71e42215034ca2f857375533d2013d17029d447e"

SERVICE_NAME = "org.webosports.app.documents.service"

# The app's repository has no bus files for its service: installed as an
# ipk, appinstalld writes them. These are the same, for the image.
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://${SERVICE_NAME}.service \
    file://${SERVICE_NAME}.role.json \
    file://${SERVICE_NAME}.api.json \
    file://${SERVICE_NAME}.groups.json \
"

inherit webos_ports_repo
inherit allarch
inherit webos_system_bus
inherit webos_filesystem_paths
inherit webos_app

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

# An Enyo 1 app and a node service: nothing to build.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # The app, as tools/build-ipk.sh packs it: no tests, tools or desktop
    # entry point.
    install -d ${D}${webos_applicationsdir}/${BPN}
    cd ${S}
    cp -R --no-dereference --preserve=mode,links \
        appinfo.json framework_config.json depends.js index.html icon.png icon-256x256.png \
        source css images lib LICENSE NOTICE \
        ${D}${webos_applicationsdir}/${BPN}/

    # Its service, which writes "Save a Copy" to a new file
    install -d ${D}${webos_servicesdir}
    cp -R --no-dereference --preserve=mode,links services/${SERVICE_NAME} ${D}${webos_servicesdir}/

    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -v -m 0644 ${UNPACKDIR}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/
    install -v -m 0644 ${UNPACKDIR}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/
    install -v -m 0644 ${UNPACKDIR}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/
    install -v -m 0644 ${UNPACKDIR}/${SERVICE_NAME}.groups.json ${D}${webos_sysbus_groupsdir}/
}

# webos_app writes the app's client permissions from appinfo.json; the app
# also calls its own service, which appinstalld would add for an ipk.
python do_configure_security:append() {
    import json, os
    perm = d.getVar("D") + d.getVar("webos_sysbus_permissionsdir") + "/" + d.getVar("BPN") + ".app.json"
    with open(perm) as f:
        permission = json.load(f)
    for key in permission:
        group = d.getVar("SERVICE_NAME") + ".group"
        if group not in permission[key]:
            permission[key].append(group)
    with open(perm, "w") as f:
        json.dump(permission, f, indent=4)
        f.write("\n")
}

FILES:${PN} += " \
    ${webos_applicationsdir}/${BPN} \
    ${webos_servicesdir}/${SERVICE_NAME} \
"

# The Enyo 1 framework it loads from /usr/palm/frameworks; its service runs
# under run-js-service and talks to the bus with palmbus.
RDEPENDS:${PN} = " \
    enyo-1.0 \
    nodejs-module-webos-sysbus \
"
