TOOLCHAIN_TARGET_TASK = "packagegroup-webos-ports-sdk-target"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain-webos-ports-${DISTRO_VERSION}"

require recipes-core/meta/meta-toolchain.bb
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
