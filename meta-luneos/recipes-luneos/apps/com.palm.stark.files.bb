SUMMARY = "LuneOS Explorer: a file manager with archives, a text editor and network folders"
SECTION = "webos/apps"
# Topaz Explorer, which runs on LuneOS as "LuneOS Explorer". The prebuilt
# 7za and topaz-net in service/bin (and their p7zip, libssh2, OpenSSL and
# libsmb2 licences) are for legacy webOS and are not installed.
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5b4473596678d62d9d83096273422c8c"

PV = "0.3.0+git"
SRCREV = "90f16a093e01eda6fe424224c9c75d2682eb094e"

SERVICE_NAME = "com.palm.stark.files.service"

SRC_URI = "git://github.com/Starkka15/topaz-explorer-webos.git;protocol=https;branch=main"

inherit allarch
inherit webos_system_bus
inherit webos_filesystem_paths
inherit webos_app

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

# An Enyo 1 app and a node service: nothing to build.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # The app and its service, as the app's tools/pack-luneos.sh packs them
    install -d ${D}${webos_applicationsdir}/${BPN}
    cp -R --no-dereference --preserve=mode,links ${S}/app/. ${D}${webos_applicationsdir}/${BPN}/

    install -d ${D}${webos_servicesdir}/${SERVICE_NAME}
    cp -R --no-dereference --preserve=mode,links ${S}/service/. ${D}${webos_servicesdir}/${SERVICE_NAME}/
    # Built for legacy webOS's soft-float ARM; LuneOS has its own 7-Zip, and
    # the service says SFTP and SMB are not available there.
    rm -rf ${D}${webos_servicesdir}/${SERVICE_NAME}/bin
    # The Google Drive account template: the service leaves it out on LuneOS
    rm -rf ${D}${webos_servicesdir}/${SERVICE_NAME}/account

    # Its name on LuneOS, and trusted: LuneOS's web runtime shows files from
    # /media only to trusted apps (/etc/wam/security_policy.conf).
    python3 -I - ${D}${webos_applicationsdir}/${BPN}/appinfo.json "LuneOS Explorer" <<'EOF'
import json, sys
appinfo, title = sys.argv[1:]
with open(appinfo) as f:
    a = json.load(f)
old = a["title"]
a["title"] = title
a["keywords"] = [title if k == old else k for k in a.get("keywords", [])]
a["trustLevel"] = "trusted"
with open(appinfo, "w") as f:
    json.dump(a, f, indent="\t")
EOF
    sed -i "s/<title>.*<\/title>/<title>LuneOS Explorer<\/title>/" ${D}${webos_applicationsdir}/${BPN}/index.html

    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.groups.json ${D}${webos_sysbus_groupsdir}/
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/
}

# webos_app writes the app's client permissions from appinfo.json; the app
# also calls its own service, which appinstalld would add for an ipk.
python do_configure_security:append() {
    import json
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
# under run-js-service with the foundations library. Archives go through
# 7-Zip (/usr/bin/7z), FTP sites through curl.
RDEPENDS:${PN} = " \
    7zip \
    curl \
    enyo-1.0 \
    foundation-frameworks \
    nodejs-module-webos-sysbus \
"
