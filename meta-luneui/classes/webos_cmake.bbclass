# Copyright (c) 2012-2013 LG Electronics, Inc.
#
# webos_cmake
#
# This class is to be inherited by every recipe in meta-webos whose component
# uses CMake. It adds a dependency on cmake-modules-webos-native, which will be
# extraneous until the component is converted, but who cares? 
#

# Extra variable is needed to be able to inhibit this dependency in case
# we have some recipe which can reuse this bbclass but without this dependency
WEBOS_CMAKE_DEPENDS = "cmake-modules-webos-native"
DEPENDS_append = " ${WEBOS_CMAKE_DEPENDS}"

inherit cmake

EXTRA_OECMAKE += "-DWEBOS_INSTALL_ROOT:PATH=/"

WEBOS_TARGET_MACHINE_IMPL ?= "emulator"
WEBOS_TARGET_CORE_OS ?= "rockhopper"
EXTRA_OECMAKE += "${@ '-DWEBOS_TARGET_CORE_OS:STRING=${WEBOS_TARGET_CORE_OS}' if bb.data.inherits_class('webos_core_os_dep', d) else '' }"
# XXX Add webos_kernel_dep() to webOS.cmake that adds WEBOS_TARGET_KERNEL_HEADERS to the search path
EXTRA_OECMAKE_KERNEL_HEADERS = "${@ '-DWEBOS_TARGET_KERNEL_HEADERS:STRING=${STAGING_KERNEL_DIR}/include' if bb.data.inherits_class('webos_kernel_dep', d) and not bb.data.inherits_class('native', d) else '' }"
EXTRA_OECMAKE_KERNEL_HEADERS[vardepvalue] = "${EXTRA_OECMAKE_KERNEL_HEADERS}"
EXTRA_OECMAKE += "${EXTRA_OECMAKE_KERNEL_HEADERS}"

EXTRA_OECMAKE_MACHINE = "${@ '-DWEBOS_TARGET_MACHINE:STRING=${MACHINE}' if d.getVar('PACKAGE_ARCH', True) == d.getVar('MACHINE_ARCH', True) and not bb.data.inherits_class('native', d) else '' }"
EXTRA_OECMAKE_MACHINE[vardepvalue] = "${EXTRA_OECMAKE_MACHINE}"
EXTRA_OECMAKE += "${EXTRA_OECMAKE_MACHINE}"

EXTRA_OECMAKE_MACHINE_IMPL = "${@ '-DWEBOS_TARGET_MACHINE_IMPL:STRING=${WEBOS_TARGET_MACHINE_IMPL}' if bb.data.inherits_class('webos_machine_impl_dep', d) and not bb.data.inherits_class('native', d) else '' }"
EXTRA_OECMAKE_MACHINE_IMPL[vardepvalue] = "${EXTRA_OECMAKE_MACHINE_IMPL}"
EXTRA_OECMAKE += "${EXTRA_OECMAKE_MACHINE_IMPL}"

# This information is always useful to have around
EXTRA_OECMAKE += "-Wdev"

# Fixup in case CMake files don't recognize the new value i586 for
# CMAKE_SYSTEM_PROCESSOR (e.g. nodejs)
do_generate_toolchain_file_append() {
    sed '/CMAKE_SYSTEM_PROCESSOR/ s/i586/i686/' -i ${WORKDIR}/toolchain.cmake
}

# Record how cmake was invoked
do_configure_append() {
    # Keep in sync with how cmake_do_configure() invokes cmake
    echo $(which cmake) \
      ${OECMAKE_SITEFILE} \
      ${S} \
      -DCMAKE_INSTALL_PREFIX:PATH=${prefix} \
      -DCMAKE_INSTALL_SO_NO_EXE=0 \
      -DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain.cmake \
      -DCMAKE_VERBOSE_MAKEFILE=1 \
      ${EXTRA_OECMAKE} \
      -Wno-dev > ${WORKDIR}/cmake.status
}

# Used in webOS.cmake _webos_set_from_env
export webos_bootdir
export webos_browserpluginsdir
export webos_defaultconfdir
export webos_execstatedir
export webos_firmwaredir
export webos_homedir
export webos_logdir
export webos_mediadir
export webos_mntdir
export webos_pkgconfigdir
export webos_preservedtmpdir
export webos_qtpluginsdir
export webos_runtimeinfodir
export webos_srcdir
export webos_udevscriptsdir
export webos_upstartconfdir
export webos_prefix
export webos_localstatedir
export webos_sysconfdir
export webos_accttemplatesdir
export webos_applicationsdir
export webos_frameworksdir
export webos_keysdir
export webos_pluginsdir
export webos_servicesdir
export webos_soundsdir
export webos_sysmgrdir
export webos_db8datadir
export webos_filecachedir
export webos_preferencesdir
export webos_sysbus_pubservicesdir
export webos_sysbus_prvservicesdir
export webos_sysbus_pubrolesdir
export webos_sysbus_prvrolesdir
export webos_sysbus_dynpubservicesdir
export webos_sysbus_dynprvservicesdir
export webos_sysbus_dynpubrolesdir
export webos_sysbus_dynprvrolesdir
export webos_sysbus_devpubservicesdir
export webos_sysbus_devprvservicesdir
export webos_sysbus_devpubrolesdir
export webos_sysbus_devprvrolesdir
export webos_sysmgr_datadir
export webos_sysmgr_localstatedir
export webos_cryptofsdir
export webos_browserstoragedir
export webos_downloadeddir
export webos_downloaded_applicationsdir
export webos_downloaded_frameworksdir
export webos_downloaded_pluginsdir
export webos_downloaded_servicesdir
export webos_persistentstoragedir
export webos_db8mediadir
export webos_mountablestoragedir
