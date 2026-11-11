SUMMARY = "An extremely fast bundler for the web"
DESCRIPTION = "esbuild, the JavaScript and TypeScript bundler and transpiler. \
Chromium's devtools-frontend runs it at build time, in the version its DEPS \
pins; webruntime uses this build instead of the x86-64 binary that the \
chromium151 repo carries."
HOMEPAGE = "https://esbuild.github.io/"
LICENSE = "MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE.md;md5=46b907b175628fe6d2a5258b252970fa \
                    file://src/golang.org/x/sys/LICENSE;md5=5d4950ecb7b26d2c5e4e7b4e0dd74707"

GO_IMPORT = "github.com/evanw/esbuild"
# golang.org/x/sys at the revision esbuild's go.mod requires
# (v0.0.0-20220715151400-c0bba94af5f8); esbuild has no other dependency.
SRC_URI = "git://github.com/evanw/esbuild.git;protocol=https;nobranch=1;tag=v${PV};destsuffix=src/${GO_IMPORT} \
           git://github.com/golang/sys;name=sys;protocol=https;branch=master;destsuffix=src/golang.org/x/sys \
"
SRCREV = "6bfc1c13b4d986b86e8bc2035f00c337b0c1d007"
SRCREV_sys = "c0bba94af5f85fbad9f6dc2e04ed5b8fac9696cf"
SRCREV_FORMAT = "default_sys"

S = "${UNPACKDIR}"

inherit go

GO_INSTALL = "${GO_IMPORT}/cmd/esbuild"
CGO_ENABLED = "0"

do_compile:prepend() {
    # GOPATH build from the fetched trees; module mode would try to download
    # golang.org/x/sys.
    export GO111MODULE=off
}

BBCLASSEXTEND = "native"
