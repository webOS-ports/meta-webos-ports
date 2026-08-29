FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Boost.System has been header-only since Boost 1.69, and Boost 1.90 no longer
# builds a compiled boost_system at all -- so b2 generates no CMake config for
# it, even though oe-core still lists "system" in BOOST_LIBS. Every consumer
# that does find_package(Boost ... COMPONENTS system) then fails to configure:
#
#   Could not find a package configuration file provided by "boost_system"
#
# Here that is db8, bootd, sam and com.webos.service.memorymanager. The webOS
# OSE components are dormant upstream, so patching each CMakeLists would mean
# carrying a patch per repo for what is really a packaging gap. Supply the
# config Boost no longer generates instead: an INTERFACE target holding the
# include path and nothing to link, which is precisely what Boost.System is.
#
# Guarded so that a future Boost which ships its own config wins instead of
# colliding with ours.
SRC_URI += " \
    file://boost_system-config.cmake \
    file://boost_system-config-version.cmake \
"

do_install:append() {
    if [ ! -e ${D}${libdir}/cmake/boost_system-${PV}/boost_system-config.cmake ]; then
        install -d ${D}${libdir}/cmake/boost_system-${PV}
        install -m 0644 ${UNPACKDIR}/boost_system-config.cmake \
            ${D}${libdir}/cmake/boost_system-${PV}/boost_system-config.cmake
        sed -e 's|@PV@|${PV}|g' ${UNPACKDIR}/boost_system-config-version.cmake \
            > ${D}${libdir}/cmake/boost_system-${PV}/boost_system-config-version.cmake
        chmod 0644 ${D}${libdir}/cmake/boost_system-${PV}/boost_system-config-version.cmake
    fi
}
