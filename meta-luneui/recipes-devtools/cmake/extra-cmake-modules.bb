SUMMARY = "Extra modules and scripts for CMake"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING-CMAKE-SCRIPTS;md5=54c7042be62e169199200bc6477f04d1 \
    file://LICENSES/BSD-3-Clause.txt;md5=954f4d71a37096249f837652a7f586c0 \
"

PV = "6.28.0"
SRCREV = "01dc9a0c05dd4851b01b93e961c9aa33b1e96056"

# anongit.kde.org still redirects here, but KDE moved to invent.kde.org years ago
# so point at the real home rather than relying on the redirect.
SRC_URI = " \
    git://invent.kde.org/frameworks/extra-cmake-modules.git;branch=master;protocol=https \
    file://0001-FindQtWaylandScanner-Search-within-OE_QMAKE_PATH_EXT.patch \
"

EXTRA_OECMAKE += "-DBUILD_TESTING=off"

inherit cmake

FILES:${PN}-dev += "${datadir}/ECM"

# ${PN} package is empty
RDEPENDS:${PN}-dev = ""

# ECM ships helper scripts carrying a #!/usr/bin/python3 shebang - json-schema.py
# for the git commit hooks, check-outbound-license.py, generate-fastlane-metadata.py -
# and packaging QA turns that shebang into a runtime dependency nothing provides:
#
#   /usr/share/ECM/kde-modules/kde-git-commit-hooks/json-schema.py contained in
#   package extra-cmake-modules-dev requires /usr/bin/python3, but no provider
#
# The dependency is not a real one. This package exists to be staged into a
# sysroot and read by CMake at build time; those scripts only ever run on the
# build host, invoked by the CMake modules that reference them, so a python3 on
# the target would never be their interpreter. Satisfying it would drag python3
# into any image carrying -dev, which is precisely what emptying RDEPENDS above
# is avoiding.
#
# Deleting the scripts is not an option either: each one is referenced by a module
# ECM installs (KDEGitCommitHooks.cmake, ECMCheckOutboundLicense.cmake,
# ECMAddAndroidApk.cmake), so dropping them would quietly break those modules.
#
# This only began failing when something in an image's dependency tree started
# pulling ECM in - do_package_qa does not run when a recipe is merely a build
# dependency of another recipe.
INSANE_SKIP:${PN}-dev += "file-rdeps"
