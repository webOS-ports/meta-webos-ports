# Copyright (c) 2012-2013 LG Electronics, Inc.
#
# webos_cmake
#
# This class is to be inherited by every recipe in meta-webos whose component
# uses CMake. It adds a dependency on cmake-modules-webos-native, which will be
# extraneous until the component is converted, but who cares? 
#
# Expects that webos_submissions or webos_enhanced_submissions will also be
# inherited (for WEBOS_COMPONENT_VERSION).

# Extra variable is needed to be able to inhibit this dependency in case
# we have some recipe which can reuse this bbclass but without this dependency
WEBOS_CMAKE_DEPENDS = "cmake-modules-webos-native"
DEPENDS_append = " ${WEBOS_CMAKE_DEPENDS}"

inherit cmake

WEBOS_PKGCONFIG_BUILDDIR = "${B}"

EXTRA_OECMAKE += "-DWEBOS_INSTALL_ROOT:PATH=/"

WEBOS_TARGET_MACHINE_IMPL ?= "emulator"
WEBOS_TARGET_CORE_OS ?= "rockhopper"
# XXX Should error if WEBOS_COMPONENT_VERSION is unset
EXTRA_OECMAKE += "-DWEBOS_COMPONENT_VERSION:STRING=${WEBOS_COMPONENT_VERSION}"
EXTRA_OECMAKE += "${@ '-DWEBOS_TARGET_CORE_OS:STRING=${WEBOS_TARGET_CORE_OS}' if bb.data.inherits_class('webos_core_os_dep', d) else '' }"
# XXX Add webos_kernel_dep() to webOS.cmake that adds WEBOS_TARGET_KERNEL_HEADERS to the search path
EXTRA_OECMAKE_KERNEL_HEADERS = "${@ '-DWEBOS_TARGET_KERNEL_HEADERS:STRING=${STAGING_KERNEL_DIR}/include' if bb.data.inherits_class('webos_kernel_dep', d) and not bb.data.inherits_class('native', d) else '' }"
EXTRA_OECMAKE += "${EXTRA_OECMAKE_KERNEL_HEADERS}"

EXTRA_OECMAKE_MACHINE = "${@ '-DWEBOS_TARGET_MACHINE:STRING=${MACHINE}' if bb.data.inherits_class('webos_machine_dep', d) and not bb.data.inherits_class('native', d) else '' }"
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
