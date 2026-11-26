# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "QML widgets and runtime framework for webOS apps"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=9e100013a76448cbe7c14134b0974453 \
"

DEPENDS = "qt-features-webos qtdeclarative qtwayland-webos pmloglib luna-service2 qttools-native"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION')[0] == '6' else '' }"
RDEPENDS:${PN} = "qt5compat-qmlplugins"

RPROVIDES:${PN}-examples = " \
    eos.bare \
    eos.widgetgallery \
"

SRCREV = "0a8e10135db25281bb74404bddc50240fa393b8c"

PV = "1.0.0-172"

PR = "r46"

inherit webos_qmake6
inherit pkgconfig
inherit webos_app_generate_security_files
inherit webos_filesystem_paths
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Perform extra QML validation
#WEBOS_QMLLINT_EXTRA_VALIDATION = "1"

FILES:${PN} += "${OE_QMAKE_PATH_QML}/Eos/*"

# The example apps' role and client-permission files, which
# webos_app_generate_security_files writes for every app under
# webos_applicationsdir, go with the apps. In ${PN} they were installed without
# them, keyed on executables that are not (do_validate_ls2_wiring:
# ROLE_EXE_MISSING). PACKAGE_BEFORE_PN, not PACKAGES +=, so -examples comes
# before ${PN}, which would otherwise take them, and still after -dbg.
QML_WEBOS_FRAMEWORK_EXAMPLE_LS2 = "com.webos.exampleapp.*.app.json eos.*.app.json"
PACKAGE_BEFORE_PN += "${PN}-examples"
FILES:${PN}-examples += " \
    ${webos_applicationsdir}/* \
    ${datadir}/qml/locales/${BPN}/ \
    ${@' '.join('%s/%s %s/%s' % (d.getVar('webos_sysbus_rolesdir'), p, d.getVar('webos_sysbus_permissionsdir'), p) for p in d.getVar('QML_WEBOS_FRAMEWORK_EXAMPLE_LS2').split())} \
    ${webos_sysbus_manifestsdir}/${BPN}-examples.manifest.json \
"

# ...and so do their manifest entries. The package manifest lists every ls2
# file in ${D}, and ls-hubd rejects a whole manifest when one file it names
# cannot be read (ManifestData::ProcessManifest), which would take the
# booster's and qmlrunner's roles down with the examples' when -examples is not
# installed. Move the examples' entries to a manifest of their own.
python do_configure_manifest:append() {
    import fnmatch, json, os

    man_dir = d.getVar("D") + d.getVar("webos_sysbus_manifestsdir")
    main_file = os.path.join(man_dir, d.getVar("BPN") + ".manifest.json")
    if not os.path.exists(main_file):
        return
    with open(main_file) as f:
        main = json.load(f)

    patterns = d.getVar("QML_WEBOS_FRAMEWORK_EXAMPLE_LS2").split()
    def is_example(path):
        return any(fnmatch.fnmatch(os.path.basename(path), p) for p in patterns)

    examples = {"id": d.getVar("BPN") + "-examples", "version": main["version"]}
    for key in list(main.keys()):
        if not isinstance(main[key], list):
            continue
        moved = [p for p in main[key] if is_example(p)]
        if not moved:
            continue
        examples[key] = moved
        main[key] = [p for p in main[key] if not is_example(p)]
        if not main[key]:
            del main[key]
    if len(examples) == 2:
        return

    for name, manifest in ((main_file, main),
                           (os.path.join(man_dir, examples["id"] + ".manifest.json"), examples)):
        with open(name, "w") as f:
            json.dump(manifest, f, indent=4, sort_keys=False)
            f.write("\n")
}

# unit-tests
PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests += "${datadir}/booster/tests/*"

# SDK tools
PACKAGES += "${PN}-tools"
FILES:${PN}-tools += "${webos_sdkdir}/*"

# we don't provide cmake tests
EXTRA_QMAKEVARS_POST += "CONFIG-=create_cmake"

# The QTestLib binaries carry QT_TESTCASE_BUILDDIR, the build directory baked
# in by qmake's testlib_defines.prf for QFINDTESTDATA, which these tests do not
# use. Drop them, as sensorfw does, rather than mute buildpaths QA; the stress
# script stays in ${PN}-tests.
do_install:append() {
    rm -f ${D}${datadir}/booster/tests/tst_*
}
