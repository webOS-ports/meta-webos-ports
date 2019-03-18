# Copyright (c) 2012-2014 LG Electronics, Inc.

#
# NOTE: Keep the lists in alphabetical order
#

# Until we fix kernel in some android devices to be compatible with 8.*
GCCVERSION = "7.%"

# Undo http://git.openembedded.org/openembedded-core/commit/?id=c62ac539e58b028b7508385c2c9be3561261f2e8
# Drop -fmacro-prefix-map=${WORKDIR}=/usr/src/debug/${PN}/${EXTENDPE}${PV}-${PR}
# because gcc-7 doesn't support it as shown in libgcc-initial:
# i586-webos-linux-gcc: error: unrecognized command line option '-fmacro-prefix-map=/home/jenkins/workspace/luneos-unstable/webos-ports/tmp-glibc/work/i586-webos-linux/libgcc-initial/7.3.0-r0=/usr/src/debug/libgcc-initial/7.3.0-r0'
# and kernel:
# aarch64-webos-linux-gcc: error: unrecognized command line option '-fmacro-prefix-map=/home/jenkins/workspace/luneos-unstable/webos-ports/tmp-glibc/work/tissot-webos-linux/linux-xiaomi-tissot/3.18.31+gitrAUTOINC+a8412322dc-r0=/usr/src/debug/linux-xiaomi-tissot/3.18.31+gitrAUTOINC+a8412322dc-r0'
DEBUG_PREFIX_MAP = " \
    -fdebug-prefix-map=${WORKDIR}=/usr/src/debug/${PN}/${EXTENDPE}${PV}-${PR} \
    -fdebug-prefix-map=${STAGING_DIR_HOST}= \
    -fdebug-prefix-map=${STAGING_DIR_NATIVE}= \
"
