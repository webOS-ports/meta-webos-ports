# Copyright (c) 2012-2013 Hewlett-Packard Development Company, L.P.
# Copyright (c) 2013 LG Electronics, Inc.

require qt4-webos.inc

# XXX We don't depend on qmake-webos-native because we continue to build qmake-palm during
# do_configure() -- see commentary in qmake-webos-native.bb
DEPENDS = "freetype jpeg libpng zlib glib-2.0 icu fontconfig"

inherit webos_public_repo
inherit webos_oe_runmake_no_env_override
inherit webos_submissions
inherit webos_library

inherit webos_machine_dep

QT4_MACHINE_CONFIG_ARCH_LITE_QPA = "-qpa"
QT4_MACHINE_CONFIG_FLAGS = "-xplatform qws/linux-armv6-g++ -no-opengl -no-neon -no-rpath -DPALM_DEVICE -qconfig palm"
QT4_MACHINE_CONFIG_FLAGS_x86 = "-xplatform qws/linux-qemux86-g++ -no-opengl -no-neon -no-rpath -DPALM_DEVICE -qconfig palm"
QT4_MACHINE_CONFIG_FLAGS_armv7a = "-xplatform qws/linux-armv6-g++ -opengl -plugin-gfx-egl -DPALM_DEVICE -qconfig palm"

PALM_BUILD_DIR = "${S}/../qt-build-${MACHINE}"
QT4_STAGING_BUILD_DIR = "${webos_srcdir}/qt4-webos"

# Export the current configuration out so that Qt .pro files can utilize these during
# their configuration
export WEBOS_CONFIG="webos ${MACHINE}"

require recipes-qt/qt4/qt4_arch.inc
do_configure_prepend() {
    # The headers that get installed under QT4_STAGING_BUILD_DIR/build/include are not
    # fixed up to reference the headers under QT4_STAGING_BUILD_DIR/git/src correctly if
    # S doesn't end with "/git". This can happen when building from a local source tree
    # whose root is something else, e.g. "qt" (the repo name). NOTE: The directory must
    # be renamed; adding a symlink git -> qt doesn't work.
    if [ $(basename ${S}) != git ]; then
        bberror 'The value of S (${S}) must end with "/git"'
        return 1
    fi

    set_arch
    set_endian
}

SRC_URI += " \
    file://0013-configure-add-crossarch-option.patch \
    file://keyboard-support-compilation-fix.patch \
    file://disable-webos-qpa-plugins.patch \
    file://0001-Extended-QPlatformWindow-class-to-provide-a-method-t.patch \
"

QT_CONFIG_FLAGS += " \
  ${QT_ENDIAN} \
  -crossarch ${QT_ARCH} \
"

do_install() {
    # Don't install qmake (already done by qmake-webos-native), but do install mkspecs,
    # since it contains a MACHINE-dependent qconfig.pri (this is because QT_CONFIG_FLAGS
    # is MACHINE-dependent). It's also target-dependent, since configure tries to
    # auto-config many of the settings.
    oe_runmake -C ${PALM_BUILD_DIR} INSTALL_ROOT=${D} install_mkspecs install_subtargets

    # Nova-Main has these additional files installed
    install -v -d ${D}${includedir}/QtOpenGL
    install -v -m 644 ${S}/src/opengl/gl2paintengineex/qglcustomshaderstage_p.h ${D}${includedir}/QtOpenGL
    if [ -d ${D}${prefix}/imports/Qt/labs/shaders ]; then
        # NB. QT_CONFIGURE_IMPORTS_PATH is set in webos_qmake.bbclass
        install -v -d ${D}${QT_CONFIGURE_IMPORTS_PATH}/Qt/labs
        mv -v ${D}${prefix}/imports/Qt/labs/shaders ${D}${QT_CONFIGURE_IMPORTS_PATH}/Qt/labs
    fi

    # The *_location settings in the Qt*.pc files don't make any sense since they
    # refer to native utilities => remove them.
    for pc in ${D}${libdir}/pkgconfig/Qt*.pc; do
        outf=${D}${webos_pkgconfigdir}/$(basename $pc)
        echo "Removing  _location settings from $pc"
        sed -i -e '/^[^_]*_location=/ d' $pc
    done
    
    # work around for building webkit-supplemental without qt4-webos BUILDDIR
    install -d ${D}${QT4_STAGING_BUILD_DIR}/git/src
    cp -ra ${S}/src/gui ${D}${QT4_STAGING_BUILD_DIR}/git/src
    cp -ra ${S}/src/corelib ${D}${QT4_STAGING_BUILD_DIR}/git/src

    # Needed to build palm QPA out of qt build
    install -d ${D}${QT4_STAGING_BUILD_DIR}/git/src/plugins/platforms
    cp -ra ${S}/src/plugins/platforms/fontdatabases ${D}${QT4_STAGING_BUILD_DIR}/git/src/plugins/platforms

    install -d ${D}${QT4_STAGING_BUILD_DIR}/git/src/3rdparty
    cp -ra ${S}/src/3rdparty/harfbuzz ${D}${QT4_STAGING_BUILD_DIR}/git/src/3rdparty
    install -d ${D}${QT4_STAGING_BUILD_DIR}/git/src/3rdparty/webkit/
    cp -ra ${S}/src/3rdparty/webkit/include ${D}${QT4_STAGING_BUILD_DIR}/git/src/3rdparty/webkit/

    install -d ${D}${QT4_STAGING_BUILD_DIR}/build
    install -d ${D}${QT4_STAGING_BUILD_DIR}/build/src
    cp -ra ${PALM_BUILD_DIR}/include/ ${D}${QT4_STAGING_BUILD_DIR}/build
    cp -ra ${PALM_BUILD_DIR}/src/corelib ${D}${QT4_STAGING_BUILD_DIR}/build/src

    # XXX Nova-Main has libqpalm.so under ${libdir} as well as /usr/plugins because that's
    # where the link of luna-sysmgr expects to find it. luna-sysmgr will be changed by
    # [OWEBOS-2617] to look for it under /usr/plugins so that we don't have to install
    # libqpalm.so in two places. (It's a problem if it has to be in ${libdir} because
    # the default FILES_${PN}-dev pick will pick it up from there.)

    # XXX Remove files not installed by Nova-Main. Eventually, figure out how to configure
    # so that they're not installed.
    # WARNING: This assumes that ${D} has a per-component value
    set -x
    rm -rf ${D}${datadir}
    rm -rf ${D}${bindir}
    rm -rf ${D}${libdir}/fonts
    rm -rf ${D}${webos_qtpluginsdir}/bearer
    rm -rf ${D}${webos_qtpluginsdir}/generic
    rm -rf ${D}${webos_qtpluginsdir}/inputmethods

    # XXX Can we get away with not staging /usr/lib/*.prl and ignoring the imports tree
    # (it seems as though we copy what we want from imports into plugins/imports)?
    rm -f ${D}${libdir}/*.prl
    rm -rf ${D}${prefix}/imports
    set +x
}

sysroot_stage_all_append() {
        sysroot_stage_dir ${D}${QT4_STAGING_BUILD_DIR} ${SYSROOT_DESTDIR}${QT4_STAGING_BUILD_DIR}
}

PACKAGES += "${PN}-buildsrc"

FILES_${PN} += "${webos_qtpluginsdir}"
# Yes, qmake-webos IS correct: mkspecs are dependent on the target.
FILES_${PN}-dev += "${libdir}/qmake-webos"
FILES_${PN}-dbg += "${webos_qtpluginsdir}/*/.debug"
# XXX Keep in sync with setting in webos_qmake.bbclass until this recipe is
# modified to inherit from it:
QT_CONFIGURE_IMPORTS_PATH = "${webos_qtpluginsdir}/imports"
FILES_${PN}-dbg += "${QT_CONFIGURE_IMPORTS_PATH}/Qt/labs/shaders/.debug"
FILES_${PN}-buildsrc += "${QT4_STAGING_BUILD_DIR}"

DEPENDS_append_armv7a = " virtual/egl"

# Enable dbus support needed for some components inside webos-ports
DEPENDS += "dbus"
QT_CONFIG_FLAGS += "-dbus"

do_install_append() {
    oe_libinstall -C ${PALM_BUILD_DIR}/lib/ -so libQtDBus ${D}/${libdir}

    cp -ra ${S}/src/opengl ${D}${QT4_STAGING_BUILD_DIR}/git/src
}

do_install_append_armv7a() {
    oe_libinstall -C ${PALM_BUILD_DIR}/lib/ -so libQtOpenGL ${D}/${libdir}

    # NOTE: We can't use accelerated graphics yet but we install QML shader support
    # anyway to have it in place once we can use the PowerVR chip inside the device.
    install -d ${D}/usr/plugins/imports/Qt/labs/shaders
    install -m 555 ${PALM_BUILD_DIR}/imports/Qt/labs/shaders/* ${D}/usr/plugins/imports/Qt/labs/shaders/
}
