# Imported from meta-sca as-is in:
# https://github.com/priv-kweihmann/meta-sca/commit/e8dba8bfd1ec4320bbc256427504f25271867492

SUMMARY = "NPM: node-gyp-build"
DESCRIPTION = "Build tool and bindings loader for node-gyp that supports prebuilds"
HOMEPAGE = "https://github.com/prebuild/node-gyp-build"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bb7eae1c2fbb280c72665db9a1efc896"

DEPENDS = ""

SRC_URI = "https://registry.npmjs.org/node-gyp-build/-/node-gyp-build-4.3.0.tgz"
SRC_URI[md5sum] = "e4925983abc8c997941bea15db423826"
SRC_URI[sha256sum] = "d9ffa0c0935751c786490d34fc8ee708a14404c760ebaaf248271f1b2ebb7bdd"

NPM_PKGNAME = "node-gyp-build"

inherit npmhelper
inherit native
