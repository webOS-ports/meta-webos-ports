TOOLCHAIN_TARGET_TASK = "packagegroup-luneos-sdk-target"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain-luneos-${DISTRO_VERSION}"

require recipes-core/meta/meta-toolchain.bb
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
